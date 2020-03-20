package com.robertsanek.data.derived.anki;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_backlogs_by_day")
public class BacklogForDay {

  @Id
  LocalDate date;
  @Column(name = "cards_in_backlog")
  Long cardsInBacklog;

  public static final class BacklogForDayBuilder {

    LocalDate date;
    Long cardsInBacklog;

    private BacklogForDayBuilder() {}

    public static BacklogForDayBuilder aBacklogForDay() { return new BacklogForDayBuilder(); }

    public BacklogForDayBuilder withDate(LocalDate date) {
      this.date = date;
      return this;
    }

    public BacklogForDayBuilder withCardsInBacklog(Long cardsInBacklog) {
      this.cardsInBacklog = cardsInBacklog;
      return this;
    }

    public BacklogForDay build() {
      BacklogForDay backlogForDay = new BacklogForDay();
      backlogForDay.cardsInBacklog = this.cardsInBacklog;
      backlogForDay.date = this.date;
      return backlogForDay;
    }
  }
}
