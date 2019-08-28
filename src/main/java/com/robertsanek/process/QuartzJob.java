package com.robertsanek.process;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.robertsanek.util.NotificationSender;
import com.robertsanek.util.inject.InjectUtils;

@DisallowConcurrentExecution
public interface QuartzJob extends Job {

  @Override
  default void execute(JobExecutionContext context) {
    try {
      exec(context);
    } catch (Exception e) {
      InjectUtils.inject(NotificationSender.class)
          .sendNotificationDefault("Exception during process run!", ExceptionUtils.getStackTrace(e));
    }
  }

  void exec(JobExecutionContext context);
}
