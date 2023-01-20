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
  @Column(name = "cards_in_backlog_7_day")
  Long cardsInBacklogSevenDays;
  @Column(name = "cards_in_backlog_30_day")
  Long cardsInBacklogThirtyDays;

  public static final class BacklogForDayBuilder {

    private LocalDate date;
    private Long cardsInBacklog;
    private Long cardsInBacklogSevenDays;
    private Long cardsInBacklogThirtyDays;

    private BacklogForDayBuilder() {}

    public static BacklogForDayBuilder aBacklogForDay() {
      return new BacklogForDayBuilder();
    }

    public BacklogForDayBuilder withDate(LocalDate date) {
      this.date = date;
      return this;
    }

    public BacklogForDayBuilder withCardsInBacklog(Long cardsInBacklog) {
      this.cardsInBacklog = cardsInBacklog;
      return this;
    }

    public BacklogForDayBuilder withCardsInBacklogSevenDays(Long cardsInBacklogSevenDays) {
      this.cardsInBacklogSevenDays = cardsInBacklogSevenDays;
      return this;
    }

    public BacklogForDayBuilder withCardsInBacklogThirtyDays(Long cardsInBacklogThirtyDays) {
      this.cardsInBacklogThirtyDays = cardsInBacklogThirtyDays;
      return this;
    }

    public BacklogForDay build() {
      BacklogForDay backlogForDay = new BacklogForDay();
      backlogForDay.cardsInBacklog = this.cardsInBacklog;
      backlogForDay.cardsInBacklogThirtyDays = this.cardsInBacklogThirtyDays;
      backlogForDay.date = this.date;
      backlogForDay.cardsInBacklogSevenDays = this.cardsInBacklogSevenDays;
      return backlogForDay;
    }
  }
}
