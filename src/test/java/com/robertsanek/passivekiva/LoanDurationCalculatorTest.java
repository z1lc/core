package com.robertsanek.passivekiva;

import static junit.framework.TestCase.assertEquals;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.joda.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.robertsanek.passivekiva.entities.Loan;
import com.robertsanek.passivekiva.entities.LoanBuilder;
import com.robertsanek.passivekiva.entities.PaymentBuilder;
import com.robertsanek.passivekiva.entities.TermsBuilder;

public class LoanDurationCalculatorTest {

  private final ZonedDateTime now = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

  @Test
  public void getDuration_simple() {
    Loan loan = new LoanBuilder()
        .setTerms(
            new TermsBuilder()
                .setScheduledPayments(Lists.newArrayList(
                    new PaymentBuilder()
                        .setAmount(Money.parse("USD 100"))
                        .setDueDate(ZonedDateTime.of(2017, 5, 1, 0, 0, 0, 0, ZoneOffset.UTC))
                        .createPayment()
                ))
                .createTerms()
        )
        .setLoanAmount(Money.parse("USD 100"))
        .createLoan();
    Assertions.assertEquals(107.0, LoanCalculator.getDuration(now, loan), 0.0);
    Assertions.assertEquals(0.0732, LoanCalculator.calculateReturn(now, loan, 0.03), 0.001);
  }

  @Test
  public void getDuration_multiplePayments_allSameAmount() {
    Loan loan = new LoanBuilder()
        .setTerms(
            new TermsBuilder()
                .setScheduledPayments(Lists.newArrayList(
                    new PaymentBuilder()
                        .setAmount(Money.parse("USD 100"))
                        .setDueDate(ZonedDateTime.of(2017, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC))
                        .createPayment(),
                    new PaymentBuilder()
                        .setAmount(Money.parse("USD 100"))
                        .setDueDate(ZonedDateTime.of(2017, 3, 1, 0, 0, 0, 0, ZoneOffset.UTC))
                        .createPayment(),
                    new PaymentBuilder()
                        .setAmount(Money.parse("USD 100"))
                        .setDueDate(ZonedDateTime.of(2017, 4, 1, 0, 0, 0, 0, ZoneOffset.UTC))
                        .createPayment()
                ))
                .createTerms()
        )
        .setLoanAmount(Money.parse("USD 300"))
        .createLoan();
    Assertions.assertEquals(47.0, LoanCalculator.getDuration(now, loan), 0.0);
  }
}