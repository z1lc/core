package com.robertsanek;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import com.google.inject.Injector;

//via http://blog.timmattison.com/archives/2014/08/05/using-guice-dependency-injection-with-quartz-schedulding/
final class GuiceJobFactory implements JobFactory {

  private final Injector guice;

  @Inject
  GuiceJobFactory(final Injector guice) {
    this.guice = guice;
  }

  @Override
  public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
    // Get the job detail so we can get the job class
    JobDetail jobDetail = triggerFiredBundle.getJobDetail();
    Class<?> jobClass = jobDetail.getJobClass();

    try {
      // Get a new instance of that class from Guice so we can do dependency injection
      return (Job) guice.getInstance(jobClass);
    } catch (Exception e) {
      // Something went wrong.  Print out the stack trace here so SLF4J doesn't hide it.
      e.printStackTrace();

      // Rethrow the exception as an UnsupportedOperationException
      throw new UnsupportedOperationException(e);
    }
  }
}
