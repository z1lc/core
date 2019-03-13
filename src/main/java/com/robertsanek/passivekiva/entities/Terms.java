package com.robertsanek.passivekiva.entities;

import java.time.ZonedDateTime;
import java.util.List;

import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.robertsanek.passivekiva.entities.deserializers.MoneyDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Terms {

  @JsonProperty("disbursal_date")
  private ZonedDateTime disbursalDate;
  @JsonProperty("disbursal_currency")
  private String disbursalCurrency; //TODO: make type-safe
  @JsonProperty("disbursal_amount")
  @JsonDeserialize(using = MoneyDeserializer.class)
  private Money disbursalAmount;
  @JsonProperty("repayment_interval")
  private String repaymentInterval; // TODO: make type-safe
  @JsonProperty("repayment_term")
  private Integer repaymentTerm;
  @JsonProperty("loan_amount")
  @JsonDeserialize(using = MoneyDeserializer.class)
  private Money loanAmount;
  @JsonProperty("local_payments")
  private List<Payment> localPayments;
  @JsonProperty("scheduled_payments")
  private List<Payment> scheduledPayments;

  public Terms() {}

  public Terms(ZonedDateTime disbursalDate, String disbursalCurrency, Money disbursalAmount,
               String repaymentInterval, Integer repaymentTerm, Money loanAmount,
               List<Payment> localPayments, List<Payment> scheduledPayments) {
    this.disbursalDate = disbursalDate;
    this.disbursalCurrency = disbursalCurrency;
    this.disbursalAmount = disbursalAmount;
    this.repaymentInterval = repaymentInterval;
    this.repaymentTerm = repaymentTerm;
    this.loanAmount = loanAmount;
    this.localPayments = localPayments;
    this.scheduledPayments = scheduledPayments;
  }

  public ZonedDateTime getDisbursalDate() {
    return disbursalDate;
  }

  public String getDisbursalCurrency() {
    return disbursalCurrency;
  }

  public Money getDisbursalAmount() {
    return disbursalAmount;
  }

  public String getRepaymentInterval() {
    return repaymentInterval;
  }

  public Integer getRepaymentTerm() {
    return repaymentTerm;
  }

  public Money getLoanAmount() {
    return loanAmount;
  }

  public List<Payment> getLocalPayments() {
    return localPayments;
  }

  public List<Payment> getScheduledPayments() {
    return scheduledPayments;
  }
}
