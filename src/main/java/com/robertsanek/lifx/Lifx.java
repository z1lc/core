package com.robertsanek.lifx;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.robertsanek.process.QuartzJob;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class Lifx implements QuartzJob {

  private static Log log = Logs.getLog(Lifx.class);

  @Override
  public void exec(JobExecutionContext context) {
    Action action = Action.NONE;
    if (context != null) {
      JobDataMap dataMap = context.getMergedJobDataMap();
      action = Action.valueOf(dataMap.getString("action"));
    }

    log.info("Triggering LIFX with action %s", action);
    boolean triggered;
    switch (action) {
      case DAY:
        triggered = new LifxConnector().triggerCoreDay();
        break;
      case EARLY_NIGHT:
        triggered = new LifxConnector().triggerEarlyNight();
        break;
      case NIGHT:
        triggered = new LifxConnector().triggerCoreNight();
        break;
      case BREATHE:
        triggered = new LifxConnector().triggerBreathe();
        break;
      case NONE:
      default:
        throw new RuntimeException(String.format("No trigger is associated with action '%s'.", action));
    }

    if (!triggered) {
      log.error("Was unable to trigger LIFX action %s.", action);
    }
  }

  public enum Action {
    DAY,
    EARLY_NIGHT,
    NIGHT,
    BREATHE,
    NONE
  }
}
