package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.time.ZonedDateTime;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

@Entity
@Table(name = "toodledo_tasks")
public class ToodledoTask {

  private static Log log = Logs.getLog(ToodledoTask.class);

  @Id
  Long id;
  String title;
  @Column(name = "added_at")
  ZonedDateTime addedAt;
  @Column(name = "modified_at")
  ZonedDateTime modifiedAt;
  @Column(name = "due_at")
  ZonedDateTime dueAt;
  @Column(name = "completed_at")
  ZonedDateTime completedAt;
  @Column(length = 10_000)
  String note;
  @Column(name = "folder_id")
  Long folderId;
  @Column(name = "context_id")
  Long contextId;
  @Column(name = "goal_id")
  Long goalId;
  @Column(name = "parent_id")
  Long parentId;
  @Enumerated(value = EnumType.STRING)
  Priority priority;
  String repeat;
  @Column(name = "length_minutes")
  Long lengthMinutes;

  public enum Priority {
    NEGATIVE(-1),
    LOW(0),
    MEDIUM(1),
    HIGH(2),
    TOP(3);

    private final Integer value;

    Priority(int value) {
      this.value = value;
    }

    public static Priority fromValue(int value) {
      return Arrays.stream(Priority.values())
          .filter(q -> q.value == value)
          .findAny()
          .orElseGet(() -> {
            log.error("Task has int value of %s for priority, but no corresponding Priority exists.", value);
            return LOW;
          });
    }
  }

  public static final class ToodledoTaskBuilder {

    Long id;
    String title;
    ZonedDateTime addedAt;
    ZonedDateTime modifiedAt;
    ZonedDateTime dueAt;
    ZonedDateTime completedAt;
    String note;
    Long folderId;
    Long contextId;
    Long goalId;
    Long parentId;
    Priority priority;
    String repeat;
    Long lengthMinutes;

    private ToodledoTaskBuilder() {}

    public static ToodledoTaskBuilder aToodledoTask() {
      return new ToodledoTaskBuilder();
    }

    public ToodledoTaskBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public ToodledoTaskBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public ToodledoTaskBuilder withAddedAt(ZonedDateTime addedAt) {
      this.addedAt = addedAt;
      return this;
    }

    public ToodledoTaskBuilder withModifiedAt(ZonedDateTime modifiedAt) {
      this.modifiedAt = modifiedAt;
      return this;
    }

    public ToodledoTaskBuilder withDueAt(ZonedDateTime dueAt) {
      this.dueAt = dueAt;
      return this;
    }

    public ToodledoTaskBuilder withCompletedAt(ZonedDateTime completedAt) {
      this.completedAt = completedAt;
      return this;
    }

    public ToodledoTaskBuilder withNote(String note) {
      this.note = note;
      return this;
    }

    public ToodledoTaskBuilder withFolderId(Long folderId) {
      this.folderId = folderId;
      return this;
    }

    public ToodledoTaskBuilder withContextId(Long contextId) {
      this.contextId = contextId;
      return this;
    }

    public ToodledoTaskBuilder withGoalId(Long goalId) {
      this.goalId = goalId;
      return this;
    }

    public ToodledoTaskBuilder withParentId(Long parentId) {
      this.parentId = parentId;
      return this;
    }

    public ToodledoTaskBuilder withPriority(Priority priority) {
      this.priority = priority;
      return this;
    }

    public ToodledoTaskBuilder withRepeat(String repeat) {
      this.repeat = repeat;
      return this;
    }

    public ToodledoTaskBuilder withLengthMinutes(Long lengthMinutes) {
      this.lengthMinutes = lengthMinutes;
      return this;
    }

    public ToodledoTask build() {
      ToodledoTask toodledoTask = new ToodledoTask();
      toodledoTask.note = this.note;
      toodledoTask.repeat = this.repeat;
      toodledoTask.dueAt = this.dueAt;
      toodledoTask.id = this.id;
      toodledoTask.addedAt = this.addedAt;
      toodledoTask.priority = this.priority;
      toodledoTask.completedAt = this.completedAt;
      toodledoTask.title = this.title;
      toodledoTask.folderId = this.folderId;
      toodledoTask.contextId = this.contextId;
      toodledoTask.goalId = this.goalId;
      toodledoTask.modifiedAt = this.modifiedAt;
      toodledoTask.lengthMinutes = this.lengthMinutes;
      toodledoTask.parentId = this.parentId;
      return toodledoTask;
    }
  }
}
