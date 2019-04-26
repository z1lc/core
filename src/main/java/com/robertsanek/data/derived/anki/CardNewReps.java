package com.robertsanek.data.derived.anki;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_new_card_reps")
public class CardNewReps {

  @Id
  Long card_id;
  Long reps_to_graduate;
  Long reps_to_mature;
  Long reps_to_90d;
  Long graduating_interval;
  boolean gotFirstReviewAfterGraduationCorrect;
  boolean reviewedGraduationRepetitionOnTime;

  public Long getCard_id() {
    return card_id;
  }

  public Long getReps_to_graduate() {
    return reps_to_graduate;
  }

  public Long getReps_to_mature() {
    return reps_to_mature;
  }

  public Long getReps_to_90d() {
    return reps_to_90d;
  }

  public Long getGraduating_interval() {
    return graduating_interval;
  }

  public boolean isGotFirstReviewAfterGraduationCorrect() {
    return gotFirstReviewAfterGraduationCorrect;
  }

  public boolean isReviewedGraduationRepetitionOnTime() {
    return reviewedGraduationRepetitionOnTime;
  }

  public static final class CardNewRepsBuilder {

    Long card_id;
    Long reps_to_graduate;
    Long reps_to_mature;
    Long reps_to_90d;
    Long graduating_interval;
    boolean gotFirstReviewAfterGraduationCorrect;
    boolean reviewedGraduationRepetitionOnTime;

    private CardNewRepsBuilder() {}

    public static CardNewRepsBuilder aCardNewReps() {
      return new CardNewRepsBuilder();
    }

    public CardNewRepsBuilder withCard_id(Long card_id) {
      this.card_id = card_id;
      return this;
    }

    public CardNewRepsBuilder withReps_to_graduate(Long reps_to_graduate) {
      this.reps_to_graduate = reps_to_graduate;
      return this;
    }

    public CardNewRepsBuilder withReps_to_mature(Long reps_to_mature) {
      this.reps_to_mature = reps_to_mature;
      return this;
    }

    public CardNewRepsBuilder withReps_to_90d(Long reps_to_90d) {
      this.reps_to_90d = reps_to_90d;
      return this;
    }

    public CardNewRepsBuilder withGraduating_interval(Long graduating_interval) {
      this.graduating_interval = graduating_interval;
      return this;
    }

    public CardNewRepsBuilder withGotFirstReviewAfterGraduationCorrect(boolean gotFirstReviewAfterGraduationCorrect) {
      this.gotFirstReviewAfterGraduationCorrect = gotFirstReviewAfterGraduationCorrect;
      return this;
    }

    public CardNewRepsBuilder withReviewedGraduationRepetitionOnTime(
        boolean reviewedGraduationRepetitionOnTime) {
      this.reviewedGraduationRepetitionOnTime = reviewedGraduationRepetitionOnTime;
      return this;
    }

    public CardNewReps build() {
      CardNewReps cardNewReps = new CardNewReps();
      cardNewReps.reps_to_90d = this.reps_to_90d;
      cardNewReps.reviewedGraduationRepetitionOnTime = this.reviewedGraduationRepetitionOnTime;
      cardNewReps.graduating_interval = this.graduating_interval;
      cardNewReps.reps_to_graduate = this.reps_to_graduate;
      cardNewReps.reps_to_mature = this.reps_to_mature;
      cardNewReps.card_id = this.card_id;
      cardNewReps.gotFirstReviewAfterGraduationCorrect = this.gotFirstReviewAfterGraduationCorrect;
      return cardNewReps;
    }
  }
}
