package com.robertsanek.data.quality.anki;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;

import com.google.common.collect.ImmutableSet;
import com.robertsanek.data.etl.local.sqllite.anki.Note;
import com.robertsanek.data.etl.local.sqllite.calibre.CalibreBookEtl;
import com.robertsanek.data.etl.local.sqllite.calibre.IncrementalReadingPriority;
import com.robertsanek.data.etl.local.sqllite.calibre.IncrementalReadingPriorityEtl;
import com.robertsanek.util.Unchecked;

public class AllCalibreBooksAndAuthorsAreInAnki extends DataQualityBase {

  public static ImmutableSet<String> EXCEPTIONS = ImmutableSet.of(
      "Jabra Elite Active 65t User Manual",
      "Frommer's India",
      "Buzzed",
      "The Ultimate Bar Book",
      "Residential Air Cleaners: A Technical Summary",
      "Confidence Cheat Sheet",
      "Little Black Book of Scams",
      "How the European Union Works",
      "2019 Email Deliverability Guide"
  );

  @Override
  void runDQ() {
    Set<String> inAnki = getExistingWorksOfArtInAnkiDbLowerCased();
    Map<Long, Long> bookIdToIRPriority = Unchecked.get(() -> new IncrementalReadingPriorityEtl().getObjects()).stream()
        .collect(Collectors.toMap(IncrementalReadingPriority::getBookId, IncrementalReadingPriority::getValue));
    Set<String> lowerCasePersonKnownFor = getExistingPeopleInAnkiDb().stream()
        .map(Note::getFields)
        .filter(fields -> fields.length() >= 2)
        .map(fields -> Jsoup.parse(DataQualityBase.splitCsvIntoCommaSeparatedList(fields).get(2))
            .text()
            .toLowerCase())
        .collect(Collectors.toSet());
    Unchecked.get(() -> new CalibreBookEtl().getObjects()).stream()
        .filter(book -> !EXCEPTIONS.contains(book.getTitle()))
        .forEach(book -> {
          String bookTitle = book.getTitle();
          Optional<Long> maybeIRPriority = Optional.ofNullable(bookIdToIRPriority.get(book.getId()));
          if (maybeIRPriority.orElse(-1L) >= 0) {
            if (!inAnki.contains(bookTitle.toLowerCase())) {
              log.error("Didn't find corresponding Work of Art note for book with title %s", bookTitle);
              violations.put(this.getClass(), bookTitle);
            }
            if (lowerCasePersonKnownFor.stream().noneMatch(fields -> fields.contains(bookTitle.toLowerCase()))) {
              log.error("Didn't find any Person note type that mentioned book with title %s", bookTitle);
              violations.put(this.getClass(), bookTitle);
            }
          }
        });
  }

}
