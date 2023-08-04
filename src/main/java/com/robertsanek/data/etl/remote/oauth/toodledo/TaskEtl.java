package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import com.robertsanek.data.etl.DoNotRun;
import com.robertsanek.data.etl.Etl;

@DoNotRun(explanation = "no longer use Toodledo")
public class TaskEtl extends Etl<ToodledoTask> {

  @Inject ToodledoConnector toodledoConnector;

  @Override
  public List<ToodledoTask> getObjects() {
    return toodledoConnector.getTasks().stream()
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
              .withParentId(Optional.ofNullable(jsonTask.getParent()).map(Long::valueOf).orElse(null))
              .withPriority(ToodledoTask.Priority.fromValue(jsonTask.getPriority()))
              .withRepeat(jsonTask.getRepeat())
              .withLengthMinutes(Long.valueOf(jsonTask.getLength()))
              .build();
        })
        .toList();
  }
}
