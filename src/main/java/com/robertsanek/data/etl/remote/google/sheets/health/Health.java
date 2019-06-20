package com.robertsanek.data.etl.remote.google.sheets.health;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "health")
public class Health {

  @Id
  private ZonedDateTime date;
  private BigDecimal cardio;
  private BigDecimal lifting;
  private BigDecimal total;
  private String comment;
  private BigDecimal alcohol;
  private String drugs;

  public ZonedDateTime getDate() {
    return date;
  }

  public BigDecimal getCardio() {
    return cardio;
  }

  public BigDecimal getLifting() {
    return lifting;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public String getComment() {
    return comment;
  }

  public BigDecimal getAlcohol() {
    return alcohol;
  }

  public String getDrugs() {
    return drugs;
  }

  public static final class HealthBuilder {

    private ZonedDateTime date;
    private BigDecimal cardio;
    private BigDecimal lifting;
    private BigDecimal total;
    private String comment;
    private BigDecimal alcohol;
    private String drugs;

    private HealthBuilder() {}

    public static HealthBuilder aHealth() {
      return new HealthBuilder();
    }

    public HealthBuilder withDate(ZonedDateTime date) {
      this.date = date;
      return this;
    }

    public HealthBuilder withCardio(BigDecimal cardio) {
      this.cardio = cardio;
      return this;
    }

    public HealthBuilder withLifting(BigDecimal lifting) {
      this.lifting = lifting;
      return this;
    }

    public HealthBuilder withTotal(BigDecimal total) {
      this.total = total;
      return this;
    }

    public HealthBuilder withComment(String comment) {
      this.comment = comment;
      return this;
    }

    public HealthBuilder withAlcohol(BigDecimal alcohol) {
      this.alcohol = alcohol;
      return this;
    }

    public HealthBuilder withDrugs(String drugs) {
      this.drugs = drugs;
      return this;
    }

    public Health build() {
      Health health = new Health();
      health.alcohol = this.alcohol;
      health.cardio = this.cardio;
      health.lifting = this.lifting;
      health.date = this.date;
      health.comment = this.comment;
      health.total = this.total;
      health.drugs = this.drugs;
      return health;
    }
  }
}
