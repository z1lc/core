package com.robertsanek.data.etl.remote.google.sheets.creditscores;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "credit_scores")
public class CreditScore {

  @Id
  private ZonedDateTime date;
  @Column(name = "credit_karma")
  private BigDecimal creditKarma;
  @Column(name = "credit_sesame")
  private BigDecimal creditSesame;
  @Column(name = "credit_view_dashboard")
  private BigDecimal creditViewDashboard;
  private BigDecimal mint;
  private BigDecimal quizzle;
  @Column(name = "bank_rate")
  private BigDecimal bankRate;
  private BigDecimal citi;

  public static final class CreditScoreBuilder {

    private ZonedDateTime date;
    private BigDecimal creditKarma;
    private BigDecimal creditSesame;
    private BigDecimal creditViewDashboard;
    private BigDecimal mint;
    private BigDecimal quizzle;
    private BigDecimal bankRate;
    private BigDecimal citi;

    private CreditScoreBuilder() {}

    public static CreditScoreBuilder aCreditScore() {
      return new CreditScoreBuilder();
    }

    public CreditScoreBuilder withDate(ZonedDateTime date) {
      this.date = date;
      return this;
    }

    public CreditScoreBuilder withCreditKarma(BigDecimal creditKarma) {
      this.creditKarma = creditKarma;
      return this;
    }

    public CreditScoreBuilder withCreditSesame(BigDecimal creditSesame) {
      this.creditSesame = creditSesame;
      return this;
    }

    public CreditScoreBuilder withCreditViewDashboard(BigDecimal creditViewDashboard) {
      this.creditViewDashboard = creditViewDashboard;
      return this;
    }

    public CreditScoreBuilder withMint(BigDecimal mint) {
      this.mint = mint;
      return this;
    }

    public CreditScoreBuilder withQuizzle(BigDecimal quizzle) {
      this.quizzle = quizzle;
      return this;
    }

    public CreditScoreBuilder withBankRate(BigDecimal bankRate) {
      this.bankRate = bankRate;
      return this;
    }

    public CreditScoreBuilder withCiti(BigDecimal citi) {
      this.citi = citi;
      return this;
    }

    public CreditScore build() {
      CreditScore creditScore = new CreditScore();
      creditScore.mint = this.mint;
      creditScore.date = this.date;
      creditScore.bankRate = this.bankRate;
      creditScore.creditKarma = this.creditKarma;
      creditScore.quizzle = this.quizzle;
      creditScore.creditSesame = this.creditSesame;
      creditScore.citi = this.citi;
      creditScore.creditViewDashboard = this.creditViewDashboard;
      return creditScore;
    }
  }
}
