package com.robertsanek.data.etl.local.habitica;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "habitica_tasks")
public class Task {

  @Id
  String id;
  String name;
  @Column(name = "time_in_minutes")
  Double timeInMinutes;

  public static final class TaskBuilder {

    String id;
    String name;
    Double timeInMinutes;

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

    public TaskBuilder withTimeInMinutes(Double timeInMinutes) {
      this.timeInMinutes = timeInMinutes;
      return this;
    }

    public Task build() {
      Task task = new Task();
      task.name = this.name;
      task.timeInMinutes = this.timeInMinutes;
      task.id = this.id;
      return task;
    }
  }
}
