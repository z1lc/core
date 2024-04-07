package com.robertsanek.passivekiva.entities;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.robertsanek.passivekiva.entities.deserializers.MoneyDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Loan implements Comparable<Loan> {

  @JsonProperty("id")
  private Long id;
  @JsonProperty("name")
  private String name;
  @JsonProperty("location")
  private Location location;
  @JsonProperty("posted_date")
  private ZonedDateTime postedDate;
  @JsonProperty("activity")
  private String activity;
  @JsonProperty("use")
  private String use;
  @JsonProperty("funded_amount")
  @JsonDeserialize(using = MoneyDeserializer.class)
  private Money fundedAmount;
  @JsonProperty("partner_id")
  private Long partnerId;
  @JsonProperty("borrower_count")
  private Long borrowerCount;
  @JsonProperty("loan_amount")
  @JsonDeserialize(using = MoneyDeserializer.class)
  private Money loanAmount;
  @JsonProperty("status")
  private Status status;
  @JsonProperty("sector")
  private String sector;
  @JsonProperty("terms")
  private Terms terms;
  private Optional<Double> duration = Optional.empty();
  private Optional<Double> xirr = Optional.empty();

  public Loan() {}

  public Loan(Long id, String name, Location location, String activity, String use, Money fundedAmount,
              Long partnerId, Long borrowerCount, Money loanAmount, Status status, String sector, Terms terms,
              Optional<Double> duration, Optional<Double> xirr) {
    this.id = id;
    this.name = name;
    this.location = location;
    this.activity = activity;
    this.use = use;
    this.fundedAmount = fundedAmount;
    this.partnerId = partnerId;
    this.borrowerCount = borrowerCount;
    this.loanAmount = loanAmount;
    this.status = status;
    this.sector = sector;
    this.terms = terms;
    this.duration = duration;
    this.xirr = xirr;
  }

  public Terms getTerms() {
    return terms;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Location getLocation() {
    return location;
  }

  public String getActivity() {
    return activity;
  }

  public String getUse() {
    return use;
  }

  public Money getFundedAmount() {
    return fundedAmount;
  }

  public Long getPartnerId() {
    return partnerId;
  }

  public Long getBorrowerCount() {
    return borrowerCount;
  }

  public Money getLoanAmount() {
    return loanAmount;
  }

  public Status getStatus() {
    return status;
  }

  public String getSector() {
    return sector;
  }

  public Optional<Double> getDuration() {
    return duration;
  }

  public Optional<Double> getXirr() {
    return xirr;
  }

  public void setDuration(double duration) {
    this.duration = Optional.of(duration);
  }

  public void setXirr(double xirr) {
    this.xirr = Optional.of(xirr);
  }

  public Optional<String> getLink() {
    if (id != null) {
      return Optional.of("https://www.kiva.org/lend/" + id);
    }
    return Optional.empty();
  }

  public Money getUnfundedAmount() {
    if (loanAmount != null && fundedAmount != null) {
      return loanAmount.minus(fundedAmount);
    }
    return Money.parse("USD 0");
  }

  @Override
  public int compareTo(@Nonnull Loan other) {
    if (this.getDuration().isPresent() && other.getDuration().isPresent()) {
      return this.getDuration().orElseThrow().compareTo(other.getDuration().orElseThrow());
    }
    return 0;
  }

}
