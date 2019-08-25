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
              .withAddedAt(ZonedDateTime.ofInstant(jsonTask.getAdded(), ZoneId.of("UTC")))
              .withModifiedAt(ZonedDateTime.ofInstant(jsonTask.getModified(), ZoneId.of("UTC")))
              .withDueAt(ZonedDateTime.ofInstant(jsonTask.getDuedate(), ZoneId.of("UTC")))
              .withCompletedAt(
                  completedIsNull ? null : ZonedDateTime.ofInstant(jsonTask.getCompleted(), ZoneId.of("UTC")))
              .withNote(jsonTask.getNote())
              .withParentId(Long.valueOf(jsonTask.getParent()))
              .withPriority(ToodledoTask.Priority.fromValue(jsonTask.getPriority()))
              .withRepeat(jsonTask.getRepeat())
              .withLengthMinutes(Long.valueOf(jsonTask.getLength()))
              .build();
        })
        .collect(Collectors.toList());
  }
}
