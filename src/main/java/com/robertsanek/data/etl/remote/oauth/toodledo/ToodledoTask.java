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

  ZonedDateTime added_at;

  ZonedDateTime modified_at;

  ZonedDateTime due_at;

  ZonedDateTime completed_at;

  @Column(length = 10_000)
  String note;

  Long folder_id;

  Long context_id;

  Long goal_id;

  Long parent_id;

  @Enumerated(value = EnumType.STRING)
  Priority priority;

  String repeat;

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
    ZonedDateTime added_at;
    ZonedDateTime modified_at;
    ZonedDateTime due_at;
    ZonedDateTime completed_at;
    String note;
    Long folder_id;
    Long context_id;
    Long goal_id;
    Long parent_id;
    Priority priority;
    String repeat;

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

    public ToodledoTaskBuilder withAdded_at(ZonedDateTime added_at) {
      this.added_at = added_at;
      return this;
    }

    public ToodledoTaskBuilder withModified_at(ZonedDateTime modified_at) {
      this.modified_at = modified_at;
      return this;
    }

    public ToodledoTaskBuilder withDue_at(ZonedDateTime due_at) {
      this.due_at = due_at;
      return this;
    }

    public ToodledoTaskBuilder withCompleted_at(ZonedDateTime completed_at) {
      this.completed_at = completed_at;
      return this;
    }

    public ToodledoTaskBuilder withNote(String note) {
      this.note = note;
      return this;
    }

    public ToodledoTaskBuilder withFolder_id(Long folder_id) {
      this.folder_id = folder_id;
      return this;
    }

    public ToodledoTaskBuilder withContext_id(Long context_id) {
      this.context_id = context_id;
      return this;
    }

    public ToodledoTaskBuilder withGoal_id(Long goal_id) {
      this.goal_id = goal_id;
      return this;
    }

    public ToodledoTaskBuilder withParent_id(Long parent_id) {
      this.parent_id = parent_id;
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

    public ToodledoTask build() {
      ToodledoTask toodledoTask = new ToodledoTask();
      toodledoTask.modified_at = this.modified_at;
      toodledoTask.priority = this.priority;
      toodledoTask.id = this.id;
      toodledoTask.repeat = this.repeat;
      toodledoTask.note = this.note;
      toodledoTask.added_at = this.added_at;
      toodledoTask.parent_id = this.parent_id;
      toodledoTask.due_at = this.due_at;
      toodledoTask.goal_id = this.goal_id;
      toodledoTask.completed_at = this.completed_at;
      toodledoTask.folder_id = this.folder_id;
      toodledoTask.context_id = this.context_id;
      toodledoTask.title = this.title;
      return toodledoTask;
    }
  }
}
