package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.robertsanek.data.etl.Etl;

public class TaskEtl extends Etl<ToodledoTask> {

  @Override
  public List<ToodledoTask> getObjects() {
    return new ToodledoConnector().getTasks().stream()
        .map(jsonTask -> {
          boolean completedIsNull = jsonTask.getCompleted().equals(Instant.ofEpochMilli(0));
          return ToodledoTask.ToodledoTaskBuilder.aToodledoTask()
              .withId(Long.valueOf(jsonTask.getId()))
              .withTitle(jsonTask.getTitle())
              .withAdded_at(ZonedDateTime.ofInstant(jsonTask.getAdded(), ZoneId.of("UTC")))
              .withModified_at(ZonedDateTime.ofInstant(jsonTask.getModified(), ZoneId.of("UTC")))
              .withDue_at(ZonedDateTime.ofInstant(jsonTask.getDuedate(), ZoneId.of("UTC")))
              .withCompleted_at(completedIsNull ? null : ZonedDateTime.ofInstant(jsonTask.getCompleted(), ZoneId.of("UTC")))
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
