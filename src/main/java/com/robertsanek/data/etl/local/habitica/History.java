package com.robertsanek.data.etl.local.habitica;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "habitica_histories")
public class History {

  @Id
  String id;  //random uuid

  String task_id;

  ZonedDateTime date;

  Boolean completed;

  ZonedDateTime etl_date;

  public String getId() {
    return id;
  }

  public String getTask_id() {
    return task_id;
  }

  public ZonedDateTime getDate() {
    return date;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public ZonedDateTime getEtl_date() {
    return etl_date;
  }

  public static final class HistoryBuilder {

    String id;
    String task_id;
    ZonedDateTime date;
    Boolean completed;
    ZonedDateTime etl_date;

    private HistoryBuilder() {}

    public static HistoryBuilder aHistory() {
      return new HistoryBuilder();
    }

    public HistoryBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public HistoryBuilder withTask_id(String task_id) {
      this.task_id = task_id;
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

    public HistoryBuilder withEtl_date(ZonedDateTime etl_date) {
      this.etl_date = etl_date;
      return this;
    }

    public History build() {
      History history = new History();
      history.etl_date = this.etl_date;
      history.id = this.id;
      history.completed = this.completed;
      history.date = this.date;
      history.task_id = this.task_id;
      return history;
    }
  }
}
