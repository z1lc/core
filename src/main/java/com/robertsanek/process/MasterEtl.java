package com.robertsanek.process;

import static com.robertsanek.util.SecretType.GOOGLE_CLOUD_SQL_CRONUS_POSTGRES_USERNAME;
import static com.robertsanek.util.SecretType.GOOGLE_CLOUD_SQL_RSANEK_POSTGRES_PASSWORD;
import static com.robertsanek.util.SecretType.GOOGLE_CLOUD_SQL_RSANEK_POSTGRES_USERNAME;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;
import org.quartz.JobExecutionContext;
import org.reflections.Reflections;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sqladmin.SQLAdmin;
import com.google.api.services.sqladmin.model.DatabaseInstance;
import com.google.api.services.sqladmin.model.Operation;
import com.google.api.services.sqladmin.model.Settings;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import com.robertsanek.data.etl.DoNotRun;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.EtlRun;
import com.robertsanek.data.etl.SlowEtl;
import com.robertsanek.data.etl.UsesLocalFiles;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.InjectUtils;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.NotificationSender;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

public class MasterEtl implements QuartzJob {

  @VisibleForTesting static final String REFLECTIONS_PREFIX = "com.robertsanek.data";
  private static final Log log = Logs.getLog(MasterEtl.class);
  private static final AtomicLong ETL_RUN_ID_GENERATOR = new AtomicLong(1);
  private static final int ROW_LIMIT = 1_000_000;
  private static final Duration TRANSACTION_TIMEOUT = Duration.ofMinutes(10);
  private static final Duration INSTANCE_STARTUP_WAIT_TIME = Duration.ofMinutes(5);
  private static final String SERVICE_ACCOUNT_FILENAME = "z1lc-qs.json";
  private static final RetryPolicy<EtlRun> individualEtlRetry = new RetryPolicy<EtlRun>()
      .handle(Throwable.class)
      .onRetry(event -> {
        log.error("%s failed on try #%s. Error:", event.getLastResult().getClass_name(), event.getAttemptCount());
        log.error(event.getLastFailure());
      })
      .withDelay(Duration.ofSeconds(3))
      .withMaxRetries(2);

  private final Configuration config = new Configuration().configure();
  private boolean fastRun = false;
  private boolean parallel = true;

  @Inject NotificationSender notificationSender;

  private SessionFactory getSessionFactory(Hbm2ddlType hbm2ddlType, ConnectionType connectionType)
      throws HibernateException, IOException, GeneralSecurityException, InterruptedException {
    try {
      config.setProperty("hibernate.hbm2ddl.auto", hbm2ddlType.getText());
      String username;
      String password;
      String instanceName;
      if (connectionType.equals(ConnectionType.RSANEK)) {
        username = CommonProvider.getSecret(GOOGLE_CLOUD_SQL_RSANEK_POSTGRES_USERNAME);
        password = CommonProvider.getSecret(GOOGLE_CLOUD_SQL_RSANEK_POSTGRES_PASSWORD);
        instanceName = "rsanek-db";
      } else if (connectionType.equals(ConnectionType.CRONUS)) {
        username = CommonProvider.getSecret(GOOGLE_CLOUD_SQL_CRONUS_POSTGRES_USERNAME);
        password = CommonProvider.getSecret(GOOGLE_CLOUD_SQL_CRONUS_POSTGRES_USERNAME);
        instanceName = "cronus-pg-primary";
      } else {
        throw new RuntimeException(String.format("Don't know configuration for connection type %s.", connectionType));
      }
      config.setProperty("hibernate.connection.username", username);
      config.setProperty("hibernate.connection.password", password);
      config.setProperty("connection.url",
          "jdbc:postgresql://google/postgres?socketFactory=com.google.cloud.sql.postgres.SocketFactory&cloudSqlInstance=arctic-rite-143002:us-west1:" +
              instanceName);
      return config.buildSessionFactory();
    } catch (ServiceException e) {
      log.info("Failed to connect to Cloud SQL Instance. Assuming instance is stopped, attempting to start...");
      String project = "arctic-rite-143002";
      String instance = "rsanek-db";

      DatabaseInstance requestBody = new DatabaseInstance();
      requestBody.setSettings(new Settings().setActivationPolicy("ALWAYS"));
      SQLAdmin sqlAdminService = createSqlAdminService();
      SQLAdmin.Instances.Patch request =
          sqlAdminService.instances().patch(project, instance, requestBody);

      Operation response = request.execute();

      log.info(response.toPrettyString());
      log.info("Starting instance successful. Waiting for %s seconds to let instance start.",
          INSTANCE_STARTUP_WAIT_TIME.toMinutes());
      Thread.sleep(INSTANCE_STARTUP_WAIT_TIME.toMillis());
      return config.buildSessionFactory();
    }
  }

  private SQLAdmin createSqlAdminService() throws IOException, GeneralSecurityException {
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    @SuppressWarnings("deprecation")  //https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory/issues/66
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(
        CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + SERVICE_ACCOUNT_FILENAME));
    if (credential.createScopedRequired()) {
      credential = credential.createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
    }

    return new SQLAdmin.Builder(httpTransport, jsonFactory, credential)
        .setApplicationName("Google-SQLAdminSample/0.1")
        .build();
  }

  private Long getBatchingSize() {
    return Long.valueOf(config.getProperty("hibernate.jdbc.batch_size"));
  }

  @SuppressWarnings({"rawtypes", "try"})
  public boolean runEtls(boolean fastRun, boolean parallel) {
    this.fastRun = fastRun;
    this.parallel = parallel;
    Stopwatch total = Stopwatch.createStarted();
    List<Class<? extends Etl>> concreteEtls = getConcreteEtls(fastRun);
    log.info("Creating connection to Cloud SQL and re-generating table schemas... (this may take up to 3 minutes)");
    try (SessionFactory ignored = Unchecked.get(() -> getSessionFactory(Hbm2ddlType.CREATE, ConnectionType.RSANEK));
         SessionFactory noneSf = Unchecked.get(() -> getSessionFactory(Hbm2ddlType.NONE, ConnectionType.RSANEK))) {
      log.info("Schema re-generation complete, taking %s seconds. Beginning ETL with parallel = %s.",
          total.elapsed().getSeconds(), parallel);
      Stream<Class<? extends Etl>> stream = parallel ? concreteEtls.parallelStream() : concreteEtls.stream();

      List<EtlRun> etlRuns = stream
          .map(etlClazz -> runIndividualEtl(etlClazz, noneSf))
          .collect(Collectors.toList());
      try (Session session = noneSf.openSession()) {
        Transaction transaction = session.beginTransaction();
        etlRuns.forEach(session::save);
        session.flush();
        session.clear();
        transaction.commit();
      } catch (Exception e) {
        log.error(e);
      }
      log.info("Completed %s ETLs in %s seconds.",
          concreteEtls.size(),
          total.elapsed().getSeconds());
      return etlRuns.stream().allMatch(etlRun -> etlRun.getWas_successful() && etlRun.getRows_generated() > 0);
    }
  }

  @Override
  public void exec(JobExecutionContext context) {
    runEtls(false, true);
  }

  @SuppressWarnings("rawtypes")
  private EtlRun runIndividualEtl(Class<? extends Etl> etlClazz, SessionFactory sf) {
    Stopwatch thisEtlStopwatch = Stopwatch.createStarted();
    ZonedDateTime thisEtlStarted = ZonedDateTime.now();
    AtomicInteger max = new AtomicInteger(0);
    AtomicBoolean successful = new AtomicBoolean(false);
    AtomicLong secondsEtl = new AtomicLong(0);
    Exception exceptionDuringEtl = new RuntimeException("dummy exception -- didn't catch any other exceptions");
    AtomicLong tryNumber = new AtomicLong(0);

    try (Session session = sf.openSession()) {
      Transaction insertTransaction = session.beginTransaction();
      insertTransaction.setTimeout((int) TRANSACTION_TIMEOUT.getSeconds());
      Failsafe
          .with(individualEtlRetry)
          .run(() -> {
            log.info("Running ETL with class %s (try #%s).", etlClazz.getName(), tryNumber.incrementAndGet());
            Etl instance = InjectUtils.inject(etlClazz);
            List objects = instance.getObjects();
            max.set(Math.min(ROW_LIMIT, objects.size()));
            if (max.get() == ROW_LIMIT) {
              throw new RuntimeException(String.format("ETL %s has %d+ rows. " +
                  "You should probably do some kind of pre-aggregation.", etlClazz.getName(), ROW_LIMIT));
            }
            secondsEtl.set(thisEtlStopwatch.elapsed().getSeconds());
            thisEtlStopwatch.reset().start();
            IntStream.range(0, max.get())
                .forEach(i -> {
                  try {
                    session.save(objects.get(i));
                  } catch (NonUniqueObjectException e) {
                    log.error(e.toString());
                  }
                  if (i % getBatchingSize() == 0) {
                    session.flush();
                    session.clear();
                  }
                });
            insertTransaction.commit();
            successful.set(true);
          });
    } catch (Exception e) {
      log.error(e);
      exceptionDuringEtl = e;
    }

    long secondsTransaction = thisEtlStopwatch.elapsed().getSeconds();

    long totalRuntime = secondsEtl.get() + secondsTransaction;
    String template = String.format("ETL with class %s completed %s in %s %s on try #%s, generating %s rows.",
        etlClazz.getName(), successful.get() ? "successfully" : "unsuccessfully",
        totalRuntime, totalRuntime == 1 ? "second" : "seconds", tryNumber.get(), max);
    if (successful.get()) {
      log.info(template);
      if (max.get() == 0) {
        notificationSender.sendNotificationDefault(
            String.format("%s generated 0 rows at %s!", etlClazz.getSimpleName(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))), "Check output.");
      }
    } else {
      notificationSender.sendNotificationDefault(
          String.format("%s failed at %s!", etlClazz.getSimpleName(),
              LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))),
          template + "\n\n" + ExceptionUtils.getStackTrace(exceptionDuringEtl));
      log.error(template);
    }

    return EtlRun.EtlRunBuilder.anEtlRun()
        .withId(ETL_RUN_ID_GENERATOR.getAndIncrement())
        .withClass_name(etlClazz.getName())
        .withStart_time(thisEtlStarted)
        .withEnd_time(ZonedDateTime.now())
        .withRows_generated((long) max.get())
        .withThread_name(Thread.currentThread().getName())
        .withUsing_parallel(true)
        .withWas_successful(successful.get())
        .withIs_slow(etlClazz.getAnnotation(SlowEtl.class) != null)
        .withUses_local_files(etlClazz.getAnnotation(UsesLocalFiles.class) != null)
        .withSeconds_in_extract_and_transform(secondsEtl.get())
        .withSeconds_in_load(secondsTransaction)
        .withTry_number(tryNumber.get())
        .build();
  }

  @VisibleForTesting
  @SuppressWarnings("rawtypes")
  List<Class<? extends Etl>> getConcreteEtls(boolean fastRun) {
    //    return Lists.newArrayList(LeetCodeQuestionEtl.class);
    Reflections reflections = new Reflections(REFLECTIONS_PREFIX);
    Set<Class<? extends Etl>> subTypesOf = reflections.getSubTypesOf(Etl.class);
    log.info("Flag fastRun is set to %s. Will%s include slow ETLs in run.", fastRun, fastRun ? " not" : "");
    return subTypesOf.stream()
        .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
        .filter(clazz -> {
          if (clazz.getAnnotation(DoNotRun.class) != null) {
            log.info("Will not run class %s because %s.", clazz.getName(),
                clazz.getAnnotation(DoNotRun.class).explanation());
            return false;
          }
          if (fastRun && clazz.getAnnotation(SlowEtl.class) != null) {
            log.info("Will not run class %s because it is annotated @%s " +
                    "and we are running with fastRun flag set to %s.",
                clazz.getName(),
                SlowEtl.class.getSimpleName(),
                fastRun);
            return false;
          }
          return true;
        })
        .sorted(Comparator.comparing(Class::getName))
        .collect(Collectors.toList());
  }

  /* https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#configurations-hbmddl
   * 'create' forces drop of existing schema+data and creation of new schema when connection happens. So tables will
   * all be emptied and then filled in as the ETL goes on.
   * 'validate' ensures the schemas in code and in the DB match. Will not drop anything and will throw exception
   * if there is a schema mismatch (so you have to manually manage schemas yourself).
   * 'update' will do nothing if the schemas match and will update them if it finds differences.
   * This is good if you want to keep existing data.
   */
  private enum Hbm2ddlType {
    CREATE("create"),              //Database dropping will be generated followed by database creation.
    CREATE_DROP("create-drop"),    /*Drop the schema and recreate it on SessionFactory startup.
                                          Additionally, drop the schema on SessionFactory shutdown. */
    CREATE_ONLY("create-only"),    //Database creation will be generated.
    DROP("drop"),                  //Database dropping will be generated.
    NONE("none"),                  //No action will be performed.
    UPDATE("update"),              //Update the database schema
    VALIDATE("validate");          //Validate the database schema

    private final String text;

    Hbm2ddlType(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }
  }

  private enum ConnectionType {
    CRONUS,
    RSANEK
  }

}
