package com.robertsanek;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.google.common.collect.ImmutableMap;
import com.robertsanek.data.EtlAndDqJob;
import com.robertsanek.data.etl.local.sqllite.anki.AnkiSyncer;
import com.robertsanek.data.etl.remote.fitbit.SleepEtl;
import com.robertsanek.data.etl.remote.google.fit.BloodPressureEtl;
import com.robertsanek.data.etl.remote.google.sheets.budget.BudgetGetter;
import com.robertsanek.data.etl.remote.oauth.toodledo.ToodledoConnector;
import com.robertsanek.data.quality.anki.DataQualityRunner;
import com.robertsanek.here.HereConnector;
import com.robertsanek.lifx.Lifx;
import com.robertsanek.passivekiva.KivaApiConnector;
import com.robertsanek.process.Command;
import com.robertsanek.process.HabiticaLoadAssessment;
import com.robertsanek.process.SuccessType;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.inject.InjectUtils;
import com.robertsanek.util.platform.CrossPlatformUtils;
import com.robertsanek.wikipedia.WikipediaConnector;

public class Main {

  private static final Log log = Logs.getLog(Main.class);
  private static String target = CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "_SUCCESS/";

  public static void main(String[] args) throws IOException, InterruptedException, SchedulerException {
    log.info("core is running on %s at %s.", System.getProperty("os.name"), ZonedDateTime.now());
    log.info("Home dir is %s.", System.getProperty("user.home"));
    log.info("Maximum heap size is %s.", FileUtils.byteCountToDisplaySize(Runtime.getRuntime().maxMemory()));
    log.info("Raw command-line arguments were %s.", Arrays.toString(args));

    CLI.CliArgs cliArgs = InjectUtils.inject(CLI.class).getCliArgs(args);
    if (cliArgs.getCommand().isPresent()) {
      Command command = cliArgs.getCommand().orElseThrow();
      JobDataMap etlJobDataMap = new JobDataMap(ImmutableMap.of(
          "parallel", cliArgs.isParallel(),
          "fastrun", cliArgs.isFastRun(),
          "machine_type", cliArgs.getMachineType().toString()
      ));

      switch (command) {
        case DAEMON:
          log.info("Running command %s.", command);

          JobDetail kivaJob = JobBuilder.newJob(KivaApiConnector.class).build();
          String every10MinutesStartingAtTopOfHourCron = "0 0/10 * 1/1 * ? *";
          Trigger kivaTrigger = TriggerBuilder.newTrigger()
              .withSchedule(CronScheduleBuilder.cronSchedule(every10MinutesStartingAtTopOfHourCron))
              .build();
          if (cliArgs.isForce()) {
            kivaTrigger = TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(10))
                .build();
          }
          JobDetail etlAndDq = JobBuilder
              .newJob(EtlAndDqJob.class)
              .usingJobData(etlJobDataMap)
              .build();
          //http://www.cronmaker.com/
          String everyHourAtTopOfHourCron = "0 0 0/1 1/1 * ? *";
          Trigger etlAndDqTrigger = TriggerBuilder.newTrigger()
              .withSchedule(CronScheduleBuilder.cronSchedule(everyHourAtTopOfHourCron))
              .build();
          if (cliArgs.isForce()) {
            etlAndDqTrigger = TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever())
                .build();
          }

          JobDetail lifxDay = JobBuilder
              .newJob(Lifx.class)
              .usingJobData("action", Lifx.Action.DAY.toString()).build();
          String nineCron = "0 50 8 1/1 * ? *";
          Trigger coreDayTrigger = TriggerBuilder.newTrigger()
              .withSchedule(CronScheduleBuilder.cronSchedule(nineCron))
              .build();

          JobDetail lifxEarlyNight = JobBuilder
              .newJob(Lifx.class)
              .withIdentity(UUID.randomUUID().toString())
              .usingJobData("action", Lifx.Action.EARLY_NIGHT.toString())
              .build();
          LocalTime sundown = InjectUtils.inject(HereConnector.class).getTodaysSundownTimeForSanFrancisco()
              .minus(Duration.ofMinutes(10));
          String eighteenCron = String.format("0 %s %s 1/1 * ? *", sundown.getMinute(), sundown.getHour());
          Trigger earlyNightTrigger = TriggerBuilder.newTrigger()
              .withSchedule(CronScheduleBuilder.cronSchedule(eighteenCron))
              .build();

          JobDetail lifxNight = JobBuilder
              .newJob(Lifx.class)
              .withIdentity(UUID.randomUUID().toString())
              .usingJobData("action", Lifx.Action.NIGHT.toString())
              .build();
          String twentyThreeCron = "0 0 23 1/1 * ? *";
          Trigger coreNightTrigger = TriggerBuilder.newTrigger()
              .withSchedule(CronScheduleBuilder.cronSchedule(twentyThreeCron))
              .build();

          JobDetail lifxBreathe = JobBuilder
              .newJob(Lifx.class)
              .withIdentity(UUID.randomUUID().toString())
              .usingJobData("action", Lifx.Action.BREATHE.toString())
              .build();
          String zeroThirtyCron = "0 30 0 1/1 * ? *";
          Trigger breatheWarningTrigger = TriggerBuilder.newTrigger()
              .withSchedule(CronScheduleBuilder.cronSchedule(zeroThirtyCron))
              .build();

          Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
          scheduler.scheduleJob(etlAndDq, etlAndDqTrigger);
          scheduler.scheduleJob(kivaJob, kivaTrigger);
          scheduler.scheduleJob(lifxDay, coreDayTrigger);
          scheduler.scheduleJob(lifxEarlyNight, earlyNightTrigger);
          scheduler.scheduleJob(lifxNight, coreNightTrigger);
          scheduler.scheduleJob(lifxBreathe, breatheWarningTrigger);

          scheduler.start();
          break;
        case DQ:
          log.info("Running command %s.", command);
          InjectUtils.inject(DataQualityRunner.class).exec(etlJobDataMap);
          break;
        case ETL:
          log.info("Running command %s.", command);
          InjectUtils.inject(EtlAndDqJob.class).exec(etlJobDataMap);
          break;
        case ETL_SETUP:
          log.info("Setting up Google Sheets credentials...");
          InjectUtils.inject(BudgetGetter.class).getData();
          log.info("Credentials for Google Sheets set up successfully.");

          log.info("Setting up Google Fit credentials...");
          InjectUtils.inject(BloodPressureEtl.class).getObjects();
          log.info("Credentials for Google Fit set up successfully.");

          log.info("Testing Anki sync...");
          if (AnkiSyncer.syncLocalCollectionIfOutOfDate("z1lc")) {
            log.info("Anki sync successful.");
          } else {
            log.error("Anki sync unsuccessful!");
          }

          log.info("Setting up Toodledo...");
          if (InjectUtils.inject(ToodledoConnector.class).getTasks().size() > 0) {
            log.info("Successfully set up Toodledo.");
          } else {
            log.error("Toodledo setup unsuccessful!");
          }

          log.info("Setting up FitBit...");
          if (InjectUtils.inject(SleepEtl.class).getObjects().size() > 0) {
            log.info("Successfully set up FitBit.");
          } else {
            log.error("FitBit setup unsuccessful!");
          }
          break;
        case HABITICA:
          log.info("Running command %s.", command);
          InjectUtils.inject(HabiticaLoadAssessment.class).generateHtmlSummary();
          break;
        case KIVA:
          log.info("Running command %s.", command);
          boolean didAlert = InjectUtils.inject(KivaApiConnector.class).doStuff();
          writeFile(Command.KIVA, SuccessType.SUCCESS);
          if (didAlert) {
            writeFile(Command.KIVA, SuccessType.ALERT);
          }
          break;
        case WIKI:
          InjectUtils.inject(WikipediaConnector.class).outputTop30PeopleInLastYear();
          break;
        default:
          throw new RuntimeException();
      }
    }
  }

  public static void writeFile(Command command, SuccessType successType) {
    File successTarget = new File(target + command.toString() + "." + successType.toString().toLowerCase());
    ZonedDateTime now = ZonedDateTime.now();
    log.info("Writing ZonedDateTime '%s' for command '%s' with success '%s' to path '%s'.",
        now, command, successType, successTarget);
    try (PrintWriter writer = Unchecked.get(() -> new PrintWriter(successTarget, StandardCharsets.UTF_8))) {
      writer.print(now);
    }
  }

}
