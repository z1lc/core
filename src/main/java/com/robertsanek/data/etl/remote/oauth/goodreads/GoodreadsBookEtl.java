package com.robertsanek.data.etl.remote.oauth.goodreads;

import static com.robertsanek.util.SecretType.GOODREADS_API_KEY;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.utils.URIBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.quality.anki.DataQualityBase;
import com.robertsanek.util.SecretProvider;

public class GoodreadsBookEtl extends Etl<GoodreadsBook> {

  private static final String LIST_ID = "4081882";
  private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss XXXX uuuu");
  private static final int REVIEWS_PER_PAGE = 20;
  private static final ImmutableMap<String, String> AUTHOR_NAME_REMAP = ImmutableMap.of(
      "J.K. Rowling", "J. K. Rowling",
      "Frederick P. Brooks Jr.", "Fred Brooks",
      "J.D. Salinger", "J. D. Salinger");
  @Inject SecretProvider secretProvider;

  @Override
  public List<GoodreadsBook> getObjects() {
    Set<String> existingPeopleInAnkiDb = DataQualityBase.getExistingPeopleInAnkiDbLowerCased();
    return IntStream.range(1, extractTotalPages(getApiResponse(1, 1)) + 1).parallel()
        .boxed()
        .flatMap(pageNum -> {
          Document parse = getApiResponse(pageNum, REVIEWS_PER_PAGE);
          NodeList reviews = parse.getElementsByTagName("review");
          return IntStream.range(0, reviews.getLength())
              .boxed()
              .map(i -> {
                Element review = (Element) reviews.item(i);
                Element book = (Element) review.getElementsByTagName("book").item(0);
                Element author = (Element) ((Element) book.getElementsByTagName("authors").item(0))
                    .getElementsByTagName("author").item(0);
                String maybeYearPublished = getChildAsText(book, "published");
                String authorName = DataQualityBase.cleanName(cleanGoodreadsAuthor(getChildAsText(author, "name")));
                return GoodreadsBook.BookBuilder.aBook()
                    .withId(Long.valueOf(getChildAsText(book, "id")))
                    .withIsbn13(getChildAsText(book, "isbn13"))
                    .withTitle(getChildAsText(book, "title"))
                    .withAuthorName(authorName)
                    .withAuthorImageUrl(getChildAsText(author, "image_url").trim())
                    .withYearPublished(maybeYearPublished.isEmpty() ? null : Long.valueOf(maybeYearPublished))
                    .withAddedOn(ZonedDateTime.parse(getChildAsText(review, "date_added"), dtFormatter))
                    .withFoundInAnki(existingPeopleInAnkiDb.contains(authorName.toLowerCase()))
                    .build();
              });
        })
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  static String cleanGoodreadsAuthor(String author) {
    return Optional.ofNullable(AUTHOR_NAME_REMAP.get(author))
        .orElseGet(() -> {
          String noJr = author.replaceAll("(,)? Jr\\.", "");
          List<String> parts = Splitter.on(' ').splitToList(noJr);
          return parts.size() == 1 ? parts.get(0) : String.format("%s %s", parts.get(0), parts.get(parts.size() - 1));
        });
  }

  private Document getApiResponse(long pageNumber, long limitPerPage) {
    try {
      URI uri = new URIBuilder()
          .setScheme("https")
          .setHost("goodreads.com")
          .setPath(String.format("/review/list/%s.xml", LIST_ID))
          .setParameter("v", "2")
          .setParameter("per_page", String.valueOf(limitPerPage))
          .setParameter("page", String.valueOf(pageNumber))
          .setParameter("key", secretProvider.getSecret(GOODREADS_API_KEY))
          .build();
      String rawXml = Unirest.get(uri.toString()).asString().getBody();
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      return dBuilder.parse(new InputSource(new StringReader(rawXml)));
    } catch (URISyntaxException | IOException | UnirestException | SAXException | ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  private String getChildAsText(Element element, String tagName) {
    return element.getElementsByTagName(tagName).item(0).getTextContent();
  }

  private int extractTotalPages(Document response) {
    return (int) Math.ceil(Double.parseDouble(
        response.getElementsByTagName("reviews").item(0).getAttributes().getNamedItem("total").getTextContent()) /
        REVIEWS_PER_PAGE);
  }

}
