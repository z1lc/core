package com.robertsanek.passivekiva.entities;

import java.util.Arrays;

public enum Status {
  FUNDRAISING,
  FUNDED,
  IN_REPAYMENT,
  PAID,
  DEFAULTED,
  REFUNDED;

  public static Status fromString(String s) {
    return Arrays.stream(Status.values())
        .filter(status -> status.toString().toLowerCase().equals(s))
        .findFirst()
        .orElseThrow();
  }
}
