package com.robertsanek.wikipedia;

import static j2html.TagCreator.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.robertsanek.data.etl.remote.wikipedia.WikiPerson;
import com.robertsanek.data.quality.anki.DataQualityBase;
import com.robertsanek.passivekiva.HTMLOutputBuilder;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;

public class WikipediaConnector {

  private static final Log log = Logs.getLog(WikipediaConnector.class);
  private static final ObjectMapper mapper = CommonProvider.getObjectMapper();
  private static final ImmutableSet<Pattern> EXCLUDED_ARTICLE_TITLE_REGEXES = ImmutableSet.of(
      Pattern.compile("^Main_Page$"),
      Pattern.compile("^Special:.*$"),
      Pattern.compile("^Portal:.*$"),
      Pattern.compile("^User:.*$"),
      Pattern.compile("^File:.*$"),
      Pattern.compile("^List_of_.*$")
  );
  private static final ImmutableSet<String> EXCLUDED_ARTICLE_TITLES = ImmutableSet.of(
      "Black_Panther_(film)",
      "Captain_Marvel_(film)",
      "Beto_O'Rourke",
      "Mary,_Queen_of_Scots",
      "Anne,_Queen_of_Great_Britain",
      "Charles,_Prince_of_Wales",
      "Diana,_Princess_of_Wales",
      "Kayden_Boche",
      "Jimmy_Carter"
  );
  private static final Long IMAGE_WIDTH = 1000L;
  private static final String WIKIDATA_URL_TEMPLATE =
      "https://www.wikidata.org/w/api.php?action=wbgetclaims&entity=%s&property=%s&format=json";
  private static final String WIKIDATA_IMAGE_PROPERTY = "P18";
  private static final String WIKIDATA_BIRTHDAY_PROPERTY = "P569";
  private static final String WIKIDATA_DEATHDAY_PROPERTY = "P570";
  private static final AtomicLong counter = new AtomicLong(0);

  public enum Language {
    CZ,
    EN
  }

  public enum Granularity {
    DAILY,
    MONTHLY
  }

  public List<WikiPerson> outputTop30PeopleInLastYear() {
    List<WikiPerson> mostViewedPeople =
        Unchecked.get(() -> getMostViewedPeople(
            Language.EN,
            LocalDate.now().minusMonths(60),
            Granularity.MONTHLY,
            100,
            true));
    log.info("Successfully extracted information for %s people.", mostViewedPeople.size());
    writeHtmlFile(mostViewedPeople);
    return mostViewedPeople;
  }

  private static List<LocalDate> getListOfDatesForApiRequest(LocalDate sinceDateInclusive, Granularity granularity) {
    final List<LocalDate> localDates = Lists.newArrayList();
    while (sinceDateInclusive.isBefore(LocalDate.now())) {
      LocalDate newDate;
      switch (granularity) {
        case DAILY:
          newDate = sinceDateInclusive.plusDays(1);
          break;
        case MONTHLY:
          newDate = sinceDateInclusive.plusMonths(1).withDayOfMonth(1);
          break;
        default:
          throw new RuntimeException(String.format("Don't have URL scheme defined for granularity '%s'.", granularity));
      }
      localDates.add(newDate);
      sinceDateInclusive = newDate;
    }

    if (localDates.size() < 200) {
      log.info("Will make %s requests to WikiMedia metrics API.", localDates.size());
    } else {
      throw new RuntimeException(String.format("Using these settings would require %s requests to the WikiMedia API. " +
          "Try decreasing the date range or decreasing granularity.", localDates.size()));
    }
    return localDates;
  }

  public static synchronized List<WikiPerson> getMostViewedPeople(Language language, LocalDate sinceDateInclusive,
                                                                  Granularity granularity,
                                                                  long peopleToOutput,
                                                                  boolean shouldWriteToDisk) throws IOException {
    counter.set(0);
    log.info("Getting top Wikipedia articles on %s.wikipedia.org since %s with granularity %s.",
        language.toString().toLowerCase(), sinceDateInclusive.toString(), granularity.toString());

    final Map<String, Long> pageToViewCountMap = Collections.synchronizedMap(Maps.newHashMap());
    getListOfDatesForApiRequest(sinceDateInclusive, granularity).parallelStream()
        .forEach(date -> {
          try {
            JsonObject apiResponse = new JsonParser()
                .parse(getWithCustomHttpClient(String
                    .format("https://wikimedia.org/api/rest_v1/metrics/pageviews/top/%s.wikipedia/all-access/%s",
                        language.toString().toLowerCase(), formatDate(date, granularity))))
                .getAsJsonObject();
            if (!apiResponse.toString().contains("Not found.")) {
              String articles =
                  apiResponse.getAsJsonArray("items").get(0).getAsJsonObject().getAsJsonArray("articles").toString();
              Arrays.stream(mapper.readValue(articles, JsonArticle[].class))
                  .filter(jsonArticle -> EXCLUDED_ARTICLE_TITLE_REGEXES.stream()
                      .noneMatch(regex -> regex.matcher(jsonArticle.getArticle()).matches()))
                  .filter(jsonArticle -> EXCLUDED_ARTICLE_TITLES.stream()
                      .noneMatch(articleName -> articleName.equals(jsonArticle.getArticle())))
                  .forEach(jsonArticle -> {
                    Long currentViews = pageToViewCountMap.getOrDefault(jsonArticle.getArticle(), 0L);
                    pageToViewCountMap.put(jsonArticle.getArticle(), currentViews + jsonArticle.getViews());
                  });
            }
          } catch (Exception e) {
            log.error("Couldn't get top article data for date %s.", date);
          }
        });

    log.info("Found %s top articles.", pageToViewCountMap.size());

    AtomicLong count = new AtomicLong(1);
    List<WikiArticle> sortedArticles = Lists.newArrayList();
    List<Map.Entry<String, Long>> sortedEntries = pageToViewCountMap.entrySet().stream()
        .sorted(((Comparator<Map.Entry<String, Long>>) (o1, o2) -> {
          return o1.getValue().compareTo(o2.getValue());
        }).reversed())
        .collect(Collectors.toList());
    for (Map.Entry<String, Long> entry : sortedEntries) {
      if (sortedArticles.size() < peopleToOutput * 3) {
        sortedArticles.add(new WikiArticle(entry.getKey(), entry.getValue(), count.getAndIncrement()));
      }
    }

    Set<String> existingPeopleInAnkiDb = DataQualityBase.getExistingPeopleInAnkiDbLowerCased();
    log.info("Found %s existing people in Anki database.", existingPeopleInAnkiDb.size());

    log.info("Will filter top %s articles for people only, and search for an image and birth/death day for each " +
        "(this may take a while).", sortedArticles.size());

    return sortedArticles.parallelStream()
        .flatMap(wikiArticle -> getWikiDataEntityIdIfPerson(wikiArticle)
            .map(Unchecked.function(wikiDataEntityId -> {
              boolean foundInAnki = existingPeopleInAnkiDb.contains(wikiArticle.getPrettyTitle().toLowerCase());
              return WikiPerson.WikiPersonBuilder.aWikiPerson()
                  .withRank(wikiArticle.getRank())
                  .withHits_in_past_year(wikiArticle.getHits())
                  .withWikipedia_url_title(wikiArticle.getUrlTitle())
                  .withFound_in_anki(foundInAnki)
                  .build();
            }))
            .filter(wikiPerson -> !shouldWriteToDisk || !wikiPerson.isFound_in_anki())
            .stream())
        .collect(Collectors.toList());
  }

  private static String formatDate(LocalDate date, Granularity granularity) {
    switch (granularity) {
      case DAILY:
        return DateTimeFormatter.ofPattern("yyyy/MM/dd").format(date);
      case MONTHLY:
        return DateTimeFormatter.ofPattern("yyyy/MM").format(date) + "/all-days";
      default:
        throw new RuntimeException(String.format("Don't have URL scheme defined for granularity '%s'.", granularity));
    }
  }

  private static Optional<LocalDate> getDate(String wikiDataEntityId, String property) throws IOException {
    return getInnerObjectFromWikiDataApi(getWikiDataApiResponse(wikiDataEntityId, property), property)
        //sometimes dates like 2019-00-00 are returned, so we'll just convert them to 2019-01-01
        .map(valueObj -> valueObj.getAsJsonObject().get("time").getAsString().replace("-00", "-01"))
        .map(birthdayString -> LocalDate.parse(
            birthdayString.substring(1, 11),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")));
  }

  private static Optional<String> getWikiDataEntityIdIfPerson(WikiArticle wikiArticle) {
    String page = "";
    try {
      page = getWithCustomHttpClient(String.format("http://dbpedia.org/page/%s", wikiArticle.getUrlTitle()));
    } catch (Exception ignored) {

    }

    if (page.contains("schema.org/Person")) {
      String afterSameAsSection = page.substring(page.indexOf("rel=\"owl:sameAs\""));
      Pattern wikiDataUrlRegex = Pattern.compile("\"http://www\\.wikidata\\.org/entity/(.*?)\"");
      Matcher matcher = wikiDataUrlRegex.matcher(afterSameAsSection);
      if (matcher.find()) {
        return Optional.ofNullable(matcher.group(1));
      }
    }
    return Optional.empty();
  }

  private static Optional<String> getPersonImageFilename(String wikiDataEntityId) throws IOException {
    JsonObject imageResponse = getWikiDataApiResponse(wikiDataEntityId, WIKIDATA_IMAGE_PROPERTY);
    return getInnerObjectFromWikiDataApi(imageResponse, WIKIDATA_IMAGE_PROPERTY)
        .map(JsonElement::getAsString);
  }

  private static String getUrl(String personImageFilename) {
    return String.format("https://commons.wikimedia.org/w/thumb.php?width=%s&f=%s",
        IMAGE_WIDTH, personImageFilename.replace(" ", "_"));
  }

  private static Optional<File> saveImage(WikiArticle wikiArticle, String personImageFileName) {
    String directory = CrossPlatformUtils.getDesktopPathIncludingTrailingSlash().orElseThrow() + "pictures/";
    if (!new File(directory).exists() && !new File(directory).mkdirs()) {
      throw new RuntimeException("Failed to create directory on desktop!");
    }
    File destination = new File(
        CrossPlatformUtils.getDesktopPathIncludingTrailingSlash().orElseThrow() + "pictures/" +
            "Person_" + DataQualityBase.cleanName(wikiArticle.getUrlTitle()) + "_1." +
            FilenameUtils.getExtension(personImageFileName));
    try {
      Request.Get(getUrl(personImageFileName))
          .execute()
          .saveContent(destination);
      return Optional.of(destination);
    } catch (Exception ignored) {
    }
    return Optional.empty();
  }

  private static JsonObject getWikiDataApiResponse(String wikiDataEntityId, String property) {
    return new JsonParser().parse(
        getWithCustomHttpClient(String.format(WIKIDATA_URL_TEMPLATE, wikiDataEntityId, property)))
        .getAsJsonObject();
  }

  private static Optional<JsonElement> getInnerObjectFromWikiDataApi(JsonObject apiResponse, String property) {
    try {
      return Optional.of(apiResponse.getAsJsonObject("claims")
          .getAsJsonArray(property)
          .get(0).getAsJsonObject()
          .getAsJsonObject("mainsnak")
          .getAsJsonObject("datavalue")
          .get("value"));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private void writeHtmlFile(List<WikiPerson> sortedPeople) {
    ContainerTag html = html()
        .with(head()
            .with(style().with(new DomContent() {
              @Override
              public void renderModel(Appendable writer, Object model) throws IOException {
                writer.append(new HTMLOutputBuilder().getStyleSheet());
              }
            }))
            .with(meta().attr("charset", "UTF-8"))
            .with(title("Top " + sortedPeople.size() + " Most-visited English Wikipedia Articles")))
        .with(body()
            .with(
                table().attr("align", "center")
                    .with(tr().with(
                        th("WikiArticle"),
                        th("Hits▼")
                    ))
                    .with(sortedPeople.stream()
                        .map(WikiPerson::toArticle)
                        .map(wikiArticle -> tr().with(
                            td(a(wikiArticle.getPrettyTitle()).withHref(wikiArticle.getURL()))
                                .withClass("wiki_article_title"),
                            td(NumberFormat.getNumberInstance(Locale.US).format(wikiArticle.getHits()))
                                .withClass("wiki_article_hits")
                        ))
                        .collect(Collectors.toList()))
            )
            .with(sortedPeople.stream()
                .map(WikiPerson::toArticle)
                .map(article -> p(article.getURL()))
                .collect(Collectors.toList())));
    File loansTarget =
        new File(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/wikipedia.html");
    log.info("Writing to %s", loansTarget.getAbsolutePath());
    try (PrintWriter writer = Unchecked.get(() -> new PrintWriter(loansTarget, StandardCharsets.UTF_8))) {
      writer.print(html.render());
    }
  }

  private static String getWithCustomHttpClient(String url) {
    return Unchecked.get(() -> EntityUtils.toString(getHttpClient().execute(new HttpGet(url)).getEntity()));
  }

  private static CloseableHttpClient getHttpClient() {
    RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
    return HttpClients.custom().setDefaultRequestConfig(customizedRequestConfig).build();
  }

}
