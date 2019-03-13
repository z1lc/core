package com.robertsanek.passivekiva.entities;

import java.time.ZonedDateTime;
import java.util.List;

import org.joda.money.Money;

public class TermsBuilder {

  private ZonedDateTime disbursalDate;
  private String disbursalCurrency;
  private Money disbursalAmount;
  private String repaymentInterval;
  private Integer repaymentTerm;
  private Money loanAmount;
  private List<Payment> localPayments;
  private List<Payment> scheduledPayments;

  public TermsBuilder setDisbursalDate(ZonedDateTime disbursalDate) {
    this.disbursalDate = disbursalDate;
    return this;
  }

  public TermsBuilder setDisbursalCurrency(String disbursalCurrency) {
    this.disbursalCurrency = disbursalCurrency;
    return this;
  }

  public TermsBuilder setDisbursalAmount(Money disbursalAmount) {
    this.disbursalAmount = disbursalAmount;
    return this;
  }

  public TermsBuilder setRepaymentInterval(String repaymentInterval) {
    this.repaymentInterval = repaymentInterval;
    return this;
  }

  public TermsBuilder setRepaymentTerm(Integer repaymentTerm) {
    this.repaymentTerm = repaymentTerm;
    return this;
  }

  public TermsBuilder setLoanAmount(Money loanAmount) {
    this.loanAmount = loanAmount;
    return this;
  }

  public TermsBuilder setLocalPayments(List<Payment> localPayments) {
    this.localPayments = localPayments;
    return this;
  }

  public TermsBuilder setScheduledPayments(List<Payment> scheduledPayments) {
    this.scheduledPayments = scheduledPayments;
    return this;
  }

  public Terms createTerms() {
    return new Terms(disbursalDate, disbursalCurrency, disbursalAmount, repaymentInterval, repaymentTerm, loanAmount,
        localPayments, scheduledPayments);
  }
}