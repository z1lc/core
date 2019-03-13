package com.robertsanek.passivekiva.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanListResponse {

  @JsonProperty("loans")
  private List<Loan> loans;

  public List<Loan> getLoans() {
    return loans;
  }

}
