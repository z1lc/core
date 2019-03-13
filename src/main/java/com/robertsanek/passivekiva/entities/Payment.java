package com.robertsanek.passivekiva.entities;

import java.time.ZonedDateTime;

import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.robertsanek.passivekiva.entities.deserializers.MoneyDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment {

  @JsonProperty("due_date")
  private ZonedDateTime dueDate;
  @JsonProperty("amount")
  @JsonDeserialize(using = MoneyDeserializer.class)
  private Money amount;

  public Payment() {}

  public Payment(ZonedDateTime dueDate, Money amount) {
    this.dueDate = dueDate;
    this.amount = amount;
  }

  public ZonedDateTime getDueDate() {
    return dueDate;
  }

  public Money getAmount() {
    return amount;
  }
}
