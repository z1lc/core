package com.robertsanek.passivekiva;

import static j2html.TagCreator.*;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.robertsanek.passivekiva.entities.Loan;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;

public class HTMLOutputBuilder {

  public ZonedDateTime now = ZonedDateTime.now();
  private DecimalFormat df = new DecimalFormat("#.0", DecimalFormatSymbols.getInstance(Locale.of("en")));

  public ContainerTag<?> buildHTML(List<Loan> loans) {
    String styleSheet = getStyleSheet();
    return html()
        .with(head()
            .with(meta().attr("charset", "UTF-8"))
            .with(style()
                .with(new DomContent() {
                  @Override
                  public String render() {
                    return styleSheet;
                  }

                  @Override
                  @SuppressWarnings("deprecation")
                  public void renderModel(Appendable writer, Object model) throws IOException {
                    writer.append(styleSheet);
                  }
                }))
            .with(title("Top " + loans.size() + " PassiveKiva Loans")))
        .with(body()
            .with(p("Created at " + now))
            .with(table().attr("align", "center")
                .with(tr().with(
                    td("Loan"),
                    td("Duration▼"),
                    td("Unfunded Amount")
                ))
                .with(
                    loans.stream()
                        .map(loan ->
                            tr().with(
                                td().with(a(loan.getName()).withHref(loan.getLink().orElseThrow())),
                                td(df.format(loan.getDuration().orElseThrow())),
                                td("$" + loan.getUnfundedAmount().getAmountMajorInt())
                                    .attr("align", "right")
                            ))
                        .toList()
                )
            ));
  }

  public String getStyleSheet() {
    try (InputStream styleSheetStream = HTMLOutputBuilder.class.getClassLoader()
        .getResourceAsStream("com/robertsanek/passivekiva/style.css")) {
      return new Scanner(styleSheetStream, UTF_8).useDelimiter("\\Z").next();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
