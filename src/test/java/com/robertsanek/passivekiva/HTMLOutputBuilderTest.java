package com.robertsanek.passivekiva;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.robertsanek.passivekiva.entities.Loan;
import com.robertsanek.passivekiva.entities.LoanBuilder;

import j2html.tags.ContainerTag;

public class HTMLOutputBuilderTest {

  private HTMLOutputBuilder outputBuilder;

  @BeforeEach
  public void before() {
    outputBuilder = new FakeHTMLOutputBuilder();
    outputBuilder.now = ZonedDateTime.of(2017, 2, 4, 0, 0, 0, 0, ZoneOffset.ofHours(-8));
  }

  @Test
  public void realTest() {
    ContainerTag<?> containerTag = new HTMLOutputBuilder().buildHTML(Collections.emptyList());
    assertTrue(containerTag.render().contains("sans-serif"));
  }

  @Test
  public void buildHTML_empty() {
    String expected = "<html><head><meta charset=\"UTF-8\"><style>{}</style><title>Top 0 PassiveKiva Loans</title>" +
        "</head><body><p>Created at 2017-02-04T00:00-08:00</p><table align=\"center\"><tr><td>Loan</td>" +
        "<td>Duration▼</td><td>Unfunded Amount</td></tr></table></body></html>";
    assertEquals(expected, outputBuilder.buildHTML(Collections.emptyList()).render());
  }

  @Test
  public void buildHTML_loans() {
    List<Loan> loans = Lists.newArrayList(
        new LoanBuilder()
            .setName("Name 1")
            .setId(1L)
            .setDuration(Optional.of(130.3))
            .createLoan()
    );
    String expected = "<html><head><meta charset=\"UTF-8\"><style>{}</style><title>Top 1 PassiveKiva Loans</title>" +
        "</head><body><p>Created at 2017-02-04T00:00-08:00</p><table align=\"center\"><tr><td>Loan</td>" +
        "<td>Duration▼</td><td>Unfunded Amount</td></tr><tr><td><a href=\"https://www.kiva.org/lend/1\">Name 1</a>" +
        "</td><td>130.3</td><td align=\"right\">$0</td></tr></table></body></html>";
    assertEquals(expected, outputBuilder.buildHTML(loans).render());
  }

  public static class FakeHTMLOutputBuilder extends HTMLOutputBuilder {

    @Override
    public String getStyleSheet() {
      return "{}";
    }
  }

}