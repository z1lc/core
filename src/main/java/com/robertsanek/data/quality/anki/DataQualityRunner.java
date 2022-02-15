package com.robertsanek.data.quality.anki;

import static com.robertsanek.data.quality.anki.DataQualityBase.dqInformation;
import static com.robertsanek.util.PostgresConnection.QUERY_TIMEOUT;
import static com.robertsanek.util.SecretType.GOOGLE_CLOUD_SQL_RSANEK_POSTGRES_PASSWORD;
import static com.robertsanek.util.SecretType.GOOGLE_CLOUD_SQL_RSANEK_POSTGRES_USERNAME;
import static j2html.TagCreator.b;
import static j2html.TagCreator.br;
import static j2html.TagCreator.div;
import static j2html.TagCreator.join;
import static j2html.TagCreator.p;

import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.reflections.Reflections;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.robertsanek.process.QuartzJob;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.NotificationSender;
import com.robertsanek.util.QuoteUtils;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.inject.InjectUtils;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import j2html.tags.ContainerTag;

public class DataQualityRunner implements QuartzJob {

  private static final Log log = Logs.getLog(DataQualityRunner.class);
  private static final String REFLECTIONS_PREFIX = "com.robertsanek.data.quality.anki";
  //don't want to run RenameAllPersonImagesToUniformNaming in parallel with other DQ checks
  private static final boolean PARALLEL = false;
  private static final int MINIMUM_VIOLATIONS = 3;
  private static final ImmutableList<String> DATA_QUALITY_SQL_FILE_NAMES = ImmutableList.of(
      "AllAnswerTemplatesHaversAnswerClassAndAvoidUsageOfClozeClass.sql",
      "AllCardsHaveTextPartBeforeImagePart.sql",
      "AllCardsWithEquationsHaveMathJaxInCorrespondingTemplate.sql",
      "AllContextsUsesAnkiContextClass.sql",
      "AllCoreCardsHaveKeyFields.sql",
      "AllDaggerAccentedDecksHaveMoreThan12NewCards.sql",
      "AllDecksLabeledAutoAddHaveAutoAddOptionsGroup.sql",
      //"AllKeyFieldsAreSticky.sql", sticky no longer ETL'd
      "AllModelsHaveMainDeckAsDefault.sql",
      "AllPronounceCardsHaveUnderlineSpanClassOrUHtmlTag.sql",
      "AllRequiredFieldsUseStarEmojiAndNotAsterisk.sql",
      "AllTemplateNamesAreCustom.sql",
      "AllTemplatesIncludeJavaScriptLibrariesAndTags.sql",
      "AllWorkOfArtAndSpotifyTrackNotesHaveCorrespondingPersonCardForAuthor.sql",
      "NoImageSourcesAreExternal.sql",
      "NoFieldsHaveTrailingBr.sql",
      "NoTemplatesUseConditionalReplacementForRequiredFields.sql",
      "NoTemplatesUseDeckOverride.sql"
  );
  private static final ImmutableSet<String> ANKI_SEARCH_STRINGS_WITHIN_SQL = ImmutableSet.of(
      "nid:",
      "cid:",
      "mid",
      "note:",
      "card:",
      "deck:",
      "tag:",
      "is:",
      "prop:",
      "added:",
      "rated:"
  );

  @Inject NotificationSender notificationSender;
  @Inject SecretProvider secretProvider;

  @VisibleForTesting
  static ContainerTag getEmailContent(DataQualityBase.DQInformation dqInformation) {
    ContainerTag htmlEmailContent = div();
    if (dqInformation.getErrors().size() > 0) {
      htmlEmailContent.with(b("Errors:")).with(br());
      htmlEmailContent.with(dqInformation.getErrorsAsTable());
    }
    if (dqInformation.getWarnings().size() > 0) {
      if (dqInformation.getErrors().size() > 0) {
        htmlEmailContent.with(br(), br());
      }
      htmlEmailContent.with(b("Warnings:")).with(br());
      htmlEmailContent.with(dqInformation.getWarningsAsTable());
    }
    return htmlEmailContent;
  }

  @VisibleForTesting
  static String getEmailSubject(int errors, int warnings) {
    StringBuilder emailSubject = new StringBuilder("Anki DQ: ");
    if (errors <= 0 && warnings <= 0) {
      emailSubject.append("All checks passed!");
    } else {
      if (errors > 0) {
        emailSubject.append(errors);
        emailSubject.append(" error");
        if (errors > 1) {
          emailSubject.append("s");
        }
      }
      if (warnings > 0) {
        if (errors > 0) {
          emailSubject.append(", ");
        }
        emailSubject.append(warnings);
        emailSubject.append(" warning");
        if (warnings > 1) {
          emailSubject.append("s");
        }
      }
      emailSubject.append(" found!");
    }
    return emailSubject.toString();
  }

  private void runAdvancedWarnings() {
    List<Class<? extends DataQualityBase>> concreteDQs = getConcreteDQs();
    log.info("Will run %s Java-based DQ checks.", concreteDQs.size());
    Stream<Class<? extends DataQualityBase>> stream = PARALLEL ? concreteDQs.parallelStream() : concreteDQs.stream();
    stream.forEach(dqClazz -> {
      DataQualityBase instance = InjectUtils.inject(dqClazz);
      log.info("Running %s...", dqClazz.getSimpleName());
      instance.runDQ();
    });
    DataQualityBase.violations.asMap()
        .forEach((a, b) -> dqInformation.error(a, Sets.newHashSet(b)));
  }

  private List<Class<? extends DataQualityBase>> getConcreteDQs() {
    Reflections reflections = new Reflections(REFLECTIONS_PREFIX);
    Set<Class<? extends DataQualityBase>> subTypesOf = reflections.getSubTypesOf(DataQualityBase.class);
    return subTypesOf.stream()
        .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
        .filter(clazz -> clazz.getAnnotation(IgnoreDQ.class) == null)
        .sorted(Comparator.comparing(Class::getName))
        .collect(Collectors.toList());
  }

  public void exec(JobDataMap jobDataMap) {
    Stopwatch startSW = Stopwatch.createStarted();
    ZonedDateTime startZdt = ZonedDateTime.now();
    log.info("Preparing to run Anki data quality checks (using parallel = %s)...", PARALLEL);
    DataQualityBase.prepareForDQ();
    String jdbcUrl = "jdbc:postgresql://google/postgres?socketFactory=com.google.cloud.sql.postgres.SocketFactory" +
        "&cloudSqlInstance=arctic-rite-143002:us-west1:rsanek-db";
    String username = secretProvider.getSecret(GOOGLE_CLOUD_SQL_RSANEK_POSTGRES_USERNAME);
    String password = secretProvider.getSecret(GOOGLE_CLOUD_SQL_RSANEK_POSTGRES_PASSWORD);

    Unchecked.run(() -> Class.forName("org.postgresql.Driver"));
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
      Stream<String> stream =
          PARALLEL ? DATA_QUALITY_SQL_FILE_NAMES.parallelStream() : DATA_QUALITY_SQL_FILE_NAMES.stream();
      stream.forEach(sqlFileName -> {
        log.info("Running %s...", sqlFileName);
        String sql = Unchecked.get(() -> Resources.toString(
            Resources.getResource("com/robertsanek/data/quality/anki/" + sqlFileName), Charsets.UTF_8));

        try (Statement statement = connection.createStatement()) {
          statement.setQueryTimeout((int) QUERY_TIMEOUT.getSeconds());
          if (sqlFileName.equals("SetTimezone.sql")) {
            statement.execute(sql);
          } else {
            ResultSet resultSet = statement.executeQuery(sql);
            Set<String> errors = new HashSet<>();
            while (resultSet.next()) {
              String firstCo = resultSet.getString(1);
              if (ANKI_SEARCH_STRINGS_WITHIN_SQL.stream().anyMatch(firstCo::contains)) {
                if (!firstCo.contains("\"")) {
                  firstCo = QuoteUtils.doubleQuote(firstCo);
                }
                errors.add(firstCo);
              } else {
                errors.add("SQL results do not output an Anki search!");
              }
            }
            if (errors.size() > 0) {
              dqInformation.error(sqlFileName, errors);
            }
          }
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      });
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    runAdvancedWarnings();
    log.info("Anki data quality checks complete.");
    int errors = dqInformation.getErrors().size();
    int warnings = dqInformation.getWarnings().size();
    String toOutput = String.format("✘ %s errors and %s warnings found.",
        errors, warnings);
    if (dqInformation.isErrorAndWarningFree()) {
      log.info("✔ All %s checks passed.", DATA_QUALITY_SQL_FILE_NAMES.size() + getConcreteDQs().size());
    } else {
      if (dqInformation.isErrorFree()) {
        log.warn(toOutput);
      } else {
        log.error(toOutput);
      }
      String emailSubject = getEmailSubject(errors, warnings);
      ContainerTag emailIntro = p(join(
          "System: ", b(String.format("%s / %s", System.getProperty("os.arch"), System.getProperty("os.name"))), br(),
          "Time: ", b(startZdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", new Locale("en")))), br(),
          "Duration :", b(String.valueOf(startSW.elapsed().getSeconds())), "seconds"));
      ContainerTag emailContent = getEmailContent(dqInformation);
      if (errors + warnings >= MINIMUM_VIOLATIONS &&
          jobDataMap.getString("machine_type").toLowerCase().contains("manual")) {
        log.info("Will notify because the total number of errors and warnings (%s) is at least as large as the " +
            "configured alert minimum (%s).", errors + warnings, MINIMUM_VIOLATIONS);
        notificationSender.sendEmail(
            new Email("dq@robertsanek.com", "Data Quality Runner"),
            new Email(CommonProvider.getEmailAddress()),
            emailSubject,
            new Content("text/html", div(join(emailIntro, br(), br(), emailContent)).render()));
      } else {
        log.info("Won't send notification because the total number of errors and warnings (%s) is less than the " +
            "configured alert minimum (%s).", errors + warnings, MINIMUM_VIOLATIONS);
      }
    }
  }

  @Override
  public void exec(JobExecutionContext context) {
    exec(context.getMergedJobDataMap());
  }
}
