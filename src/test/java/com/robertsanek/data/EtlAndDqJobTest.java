package com.robertsanek.data;

import static org.quartz.TriggerBuilder.newTrigger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class EtlAndDqJobTest {

  @Test
  @Disabled("integration")
  public void name() throws SchedulerException, InterruptedException {
    JobDetail job = JobBuilder
        .newJob(EtlAndDqJob.class)
        .withIdentity("a", "b")
        .usingJobData("parallel", false)
        .build();
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    Trigger trigger = newTrigger()
        .withIdentity("trigger1", "group1")
        .startNow()
        .build();

    sched.scheduleJob(job, trigger);
    sched.start();
    Thread.sleep(90L * 1000L);
  }

  @Test
  @Disabled("integration")
  public void name2() {
    new EtlAndDqJob().triggerKlipfolioRefresh();
  }
}
