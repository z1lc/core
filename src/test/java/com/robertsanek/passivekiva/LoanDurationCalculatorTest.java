package com.robertsanek.passivekiva;

import static junit.framework.TestCase.assertEquals;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.joda.money.Money;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.robertsanek.passivekiva.entities.Loan;
import com.robertsanek.passivekiva.entities.LoanBuilder;
import com.robertsanek.passivekiva.entities.PaymentBuilder;
import com.robertsanek.passivekiva.entities.TermsBuilder;

public class LoanDurationCalculatorTest {

  private ZonedDateTime now = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

  @Test
  public void getDuration_simple() {
    Loan loan = new LoanBuilder()
        .setTerms(
            new TermsBuilder()
                .setScheduledPayments(Lists.newArrayList(
                    new PaymentBuilder()
                        .setAmount(Money.parse("USD 100"))
                        .setDueDate(ZonedDateTime.of(2017, 2, 1, 0, 0, 0, 0, ZoneOffset.UTC))
                        .createPayment()
                ))
                .createTerms()
        )
        .setLoanAmount(Money.parse("USD 100"))
        .createLoan();
    assertEquals(17.0, LoanDurationCalculator.getDuration(now, loan), 0.0);
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
    assertEquals(47.0, LoanDurationCalculator.getDuration(now, loan), 0.0);
  }

}