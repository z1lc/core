package com.robertsanek.passivekiva;

import static java.time.temporal.ChronoUnit.DAYS;

import java.math.RoundingMode;
import java.time.ZonedDateTime;

import com.robertsanek.passivekiva.entities.Loan;

public class LoanDurationCalculator {

  public static double getDuration(ZonedDateTime now, Loan loan) {
    return loan.getTerms().getScheduledPayments().stream()
        .mapToDouble(payment -> {
          //repayments are typically transferred on the 17th of the month preceding the due date.
          long daysUntilPayment = DAYS.between(now, payment.getDueDate().minusMonths(1)) + 17;
          return payment.getAmount()
              .multipliedBy(daysUntilPayment)
              .dividedBy(loan.getLoanAmount().getAmount(), RoundingMode.HALF_EVEN)
              .getAmount()
              .doubleValue();
        })
        .sum();
  }

}
