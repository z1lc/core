package com.robertsanek.data.etl.remote.oauth.toodledo;

import com.robertsanek.data.etl.remote.oauth.toodledo.jsonentities.JsonTask;
import com.robertsanek.util.DateTimeUtils;

public class ToodledoUtils {

  static ToodledoTask convertJsonTaskToHibernateTask(JsonTask jsonTask) {
    return ToodledoTask.ToodledoTaskBuilder.aToodledoTask()
        .withId(Long.valueOf(jsonTask.getId()))
        .withTitle(jsonTask.getTitle())
        .withAdded_at(DateTimeUtils.toZonedDateTime(jsonTask.getAdded()))
        .withModified_at(DateTimeUtils.toZonedDateTime(jsonTask.getModified()))
        .withDue_at(DateTimeUtils.toZonedDateTime(jsonTask.getDuedate()))
        .withCompleted_at(DateTimeUtils.toZonedDateTime(jsonTask.getCompleted()))
        .withNote(jsonTask.getNote())
        .withFolder_id(Long.valueOf(jsonTask.getFolder()))
        .withContext_id(Long.valueOf(jsonTask.getContext()))
        .withGoal_id(Long.valueOf(jsonTask.getGoal()))
        .withParent_id(Long.valueOf(jsonTask.getParent()))
        .withPriority(ToodledoTask.Priority.fromValue(jsonTask.getPriority()))
        .withRepeat(jsonTask.getRepeat())
        .build();
  }

}
