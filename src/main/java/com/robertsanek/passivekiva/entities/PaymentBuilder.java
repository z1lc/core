package com.robertsanek.passivekiva.entities;

import java.time.ZonedDateTime;

import org.joda.money.Money;

public class PaymentBuilder {

  private ZonedDateTime dueDate;
  private Money amount;

  public PaymentBuilder setDueDate(ZonedDateTime dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  public PaymentBuilder setAmount(Money amount) {
    this.amount = amount;
    return this;
  }

  public Payment createPayment() {
    return new Payment(dueDate, amount);
  }
}