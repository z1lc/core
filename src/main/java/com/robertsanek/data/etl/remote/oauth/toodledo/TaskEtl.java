package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.DateTimeUtils;

public class TaskEtl extends Etl<ToodledoTask> {

  @Override
  public List<ToodledoTask> getObjects() {
    return new ToodledoConnector().getTasks().stream()
        .map(jsonTask -> {
          boolean completedIsNull = jsonTask.getCompleted().equals(Instant.ofEpochMilli(0));
          return ToodledoTask.ToodledoTaskBuilder.aToodledoTask()
              .withId(Long.valueOf(jsonTask.getId()))
              .withTitle(jsonTask.getTitle())
              .withAdded_at(DateTimeUtils.toZonedDateTime(jsonTask.getAdded()))
              .withModified_at(DateTimeUtils.toZonedDateTime(jsonTask.getModified()))
              .withDue_at(DateTimeUtils.toZonedDateTime(jsonTask.getDuedate()))
              .withCompleted_at(completedIsNull ? null : DateTimeUtils.toZonedDateTime(jsonTask.getCompleted()))
              .withNote(jsonTask.getNote())
              .withFolder_id(Long.valueOf(jsonTask.getFolder()))
              .withContext_id(Long.valueOf(jsonTask.getContext()))
              .withGoal_id(Long.valueOf(jsonTask.getGoal()))
              .withParent_id(Long.valueOf(jsonTask.getParent()))
              .withPriority(ToodledoTask.Priority.fromValue(jsonTask.getPriority()))
              .withRepeat(jsonTask.getRepeat())
              .build();
        })
        .collect(Collectors.toList());
  }
}
