package com.robertsanek.data.etl.local.habitica;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "habitica_tasks")
public class Task {

  @Id
  String id;

  String name;

  Long time_in_minutes;

  public static final class TaskBuilder {

    String id;
    String name;
    Long time_in_minutes;

    private TaskBuilder() {}

    public static TaskBuilder aTask() {
      return new TaskBuilder();
    }

    public TaskBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public TaskBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public TaskBuilder withTime_in_minutes(Long time_in_minutes) {
      this.time_in_minutes = time_in_minutes;
      return this;
    }

    public Task build() {
      Task task = new Task();
      task.name = this.name;
      task.time_in_minutes = this.time_in_minutes;
      task.id = this.id;
      return task;
    }
  }
}
