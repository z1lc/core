package com.robertsanek.data.etl.local.habitica;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "habitica_histories")
public class History {

  @Id
  String id;  //random uuid
  @Column(name = "task_id")
  String taskId;
  ZonedDateTime date;
  Boolean completed;

  public String getId() {
    return id;
  }

  public String getTaskId() {
    return taskId;
  }

  public ZonedDateTime getDate() {
    return date;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public static final class HistoryBuilder {

    String id;
    String taskId;
    ZonedDateTime date;
    Boolean completed;

    private HistoryBuilder() {}

    public static HistoryBuilder aHistory() {
      return new HistoryBuilder();
    }

    public HistoryBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public HistoryBuilder withTaskId(String taskId) {
      this.taskId = taskId;
      return this;
    }

    public HistoryBuilder withDate(ZonedDateTime date) {
      this.date = date;
      return this;
    }

    public HistoryBuilder withCompleted(Boolean completed) {
      this.completed = completed;
      return this;
    }

    public History build() {
      History history = new History();
      history.id = this.id;
      history.completed = this.completed;
      history.date = this.date;
      history.taskId = this.taskId;
      return history;
    }
  }
}
