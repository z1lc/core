package com.robertsanek.data.etl.remote.google.sheets.health;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "health")
public class Health {

  @Id
  private LocalDate date;
  private BigDecimal cardio;
  private BigDecimal lifting;
  private BigDecimal total;
  private String comment;
  private String groceries;
  private Boolean cook;
  private Boolean cafeteria;
  @Column(name = "order_in")
  private Boolean orderIn;
  private Boolean restaurant;
  private BigDecimal alcohol;
  private String drugs;

  public LocalDate getDate() {
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

    private LocalDate date;
    private BigDecimal cardio;
    private BigDecimal lifting;
    private BigDecimal total;
    private String comment;
    private String groceries;
    private Boolean cook;
    private Boolean cafeteria;
    private Boolean orderIn;
    private Boolean restaurant;
    private BigDecimal alcohol;
    private String drugs;

    private HealthBuilder() {}

    public static HealthBuilder aHealth() {
      return new HealthBuilder();
    }

    public HealthBuilder withDate(LocalDate date) {
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

    public HealthBuilder withGroceries(String groceries) {
      this.groceries = groceries;
      return this;
    }

    public HealthBuilder withCook(Boolean cook) {
      this.cook = cook;
      return this;
    }

    public HealthBuilder withCafeteria(Boolean cafeteria) {
      this.cafeteria = cafeteria;
      return this;
    }

    public HealthBuilder withOrderIn(Boolean orderIn) {
      this.orderIn = orderIn;
      return this;
    }

    public HealthBuilder withRestaurant(Boolean restaurant) {
      this.restaurant = restaurant;
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
      health.total = this.total;
      health.cardio = this.cardio;
      health.cafeteria = this.cafeteria;
      health.alcohol = this.alcohol;
      health.lifting = this.lifting;
      health.cook = this.cook;
      health.restaurant = this.restaurant;
      health.drugs = this.drugs;
      health.comment = this.comment;
      health.date = this.date;
      health.groceries = this.groceries;
      health.orderIn = this.orderIn;
      return health;
    }
  }
}
