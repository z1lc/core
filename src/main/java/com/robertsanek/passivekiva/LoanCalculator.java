package com.robertsanek.passivekiva;

import static java.time.temporal.ChronoUnit.DAYS;

import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.decampo.xirr.Transaction;
import org.decampo.xirr.Xirr;

import com.robertsanek.passivekiva.entities.Loan;

public class LoanCalculator {

  // Accurate as of 2024-03-27: 0.89% default + 0.1% currency loss
  private static final double CURRENCY_EXCHANGE_LOSS_AND_DEFAULT_PERCENTAGE_TOTAL = 0.0099;

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

  public static List<Transaction> getTransactionsForXIRR(Loan loan) {
    double amortizedLoss = loan.getLoanAmount().getAmount().doubleValue() *
        CURRENCY_EXCHANGE_LOSS_AND_DEFAULT_PERCENTAGE_TOTAL / loan.getTerms().getScheduledPayments().size();
    return loan.getTerms().getScheduledPayments().stream()
        .map(payment -> new Transaction(payment.getAmount().getAmount().doubleValue() - amortizedLoss, payment.getDueDate().minusMonths(1).plusDays(16).toLocalDate()))
        .collect(Collectors.toList());
  }

  public static double calculateReturn(ZonedDateTime now, Loan loan, Double initialCashBack) {
    Transaction firstTransaction = new Transaction(-loan.getLoanAmount().getAmount().doubleValue() * (1 - initialCashBack), now.toLocalDate());
    List<Transaction> transactionsForXIRR = getTransactionsForXIRR(loan);
    transactionsForXIRR.add(firstTransaction);
    if (transactionsForXIRR.size() < 2) {
      return 0.0;
    }
    return new Xirr(transactionsForXIRR).xirr();
  }

}
