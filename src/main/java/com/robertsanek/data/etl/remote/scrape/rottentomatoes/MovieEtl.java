package com.robertsanek.data.etl.remote.scrape.rottentomatoes;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Unchecked;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MovieEtl extends Etl<Movie> {

  private static final String USER_ID = "977226125";
  private static final AtomicLong ID_ISSUER = new AtomicLong(1);
  private static final Pattern yearRegex = Pattern.compile("\\d\\d\\d\\d");

  @Override
  public List<Movie> getObjects() {
    URI wantToSeeUri = Unchecked.get(() -> new URIBuilder()
        .setScheme("https")
        .setHost("rottentomatoes.com")
        .setPath(String.format("user/id/%s/wts/", USER_ID))
        .setParameter("mediaType", "1")
        .setParameter("wtsni", "wts")
        .build());
    try (WebClient webClient = CommonProvider.getHtmlUnitWebClient()) {
      HtmlPage page = CommonProvider.retrying().get(() -> webClient.getPage(wantToSeeUri.toURL()));
      List<HtmlListItem> ul = page.getByXPath("//li[contains(@class, 'bottom_divider')]");
      return ul.stream()
          .map(li -> {
            HtmlElement link = li.getElementsByTagName("a").get(0);
            String href = link.getAttribute("href");
            String title = link.getAttribute("title");
            String score = li.getElementsByTagName("span").stream()
                .filter(elem -> elem.getAttribute("class").equals("tMeterScore"))
                .findFirst().orElseThrow().asText();
            Matcher matcher = yearRegex.matcher(li.asText());
            Long year = null;
            if (matcher.find()) {
              year = Long.parseLong(matcher.group());
            }
            return Movie.MovieBuilder.aMovie()
                .withId(ID_ISSUER.getAndIncrement())
                .withTitle(title)
                .withUrl("https://www.rottentomatoes.com/" + href)
                .withRating(Long.valueOf(score.replace("%", "")))
                .withYear(year)
                .build();
          })
          .collect(Collectors.toList());
    }
  }
}
