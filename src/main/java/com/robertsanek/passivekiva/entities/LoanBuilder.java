package com.robertsanek.passivekiva.entities;

import java.util.Optional;

import org.joda.money.Money;

public class LoanBuilder {

  private Long id;
  private String name;
  private Location location;
  private String activity;
  private String use;
  private Money fundedAmount;
  private Long partnerId;
  private Long borrowerCount;
  private Money loanAmount;
  private Status status;
  private String sector;
  private Terms terms;
  private Optional<Double> duration;
  private Optional<String> link;

  public LoanBuilder setId(Long id) {
    this.id = id;
    return this;
  }

  public LoanBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public LoanBuilder setLocation(Location location) {
    this.location = location;
    return this;
  }

  public LoanBuilder setActivity(String activity) {
    this.activity = activity;
    return this;
  }

  public LoanBuilder setUse(String use) {
    this.use = use;
    return this;
  }

  public LoanBuilder setFundedAmount(Money fundedAmount) {
    this.fundedAmount = fundedAmount;
    return this;
  }

  public LoanBuilder setPartnerId(Long partnerId) {
    this.partnerId = partnerId;
    return this;
  }

  public LoanBuilder setBorrowerCount(Long borrowerCount) {
    this.borrowerCount = borrowerCount;
    return this;
  }

  public LoanBuilder setLoanAmount(Money loanAmount) {
    this.loanAmount = loanAmount;
    return this;
  }

  public LoanBuilder setStatus(Status status) {
    this.status = status;
    return this;
  }

  public LoanBuilder setSector(String sector) {
    this.sector = sector;
    return this;
  }

  public LoanBuilder setTerms(Terms terms) {
    this.terms = terms;
    return this;
  }

  public LoanBuilder setDuration(Optional<Double> duration) {
    this.duration = duration;
    return this;
  }

  public LoanBuilder setLink(Optional<String> link) {
    this.link = link;
    return this;
  }

  public Loan createLoan() {
    return new Loan(id, name, location, activity, use, fundedAmount, partnerId, borrowerCount, loanAmount, status,
        sector, terms, duration);
  }
}