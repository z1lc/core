package com.robertsanek.data.quality.anki;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class EnsureDatesInPersonCardAreNonDecreasingAcrossLinesAndIncreasingPerLine extends DataQualityBase {

  static final Log log = Logs.getLog(EnsureDatesInPersonCardAreNonDecreasingAcrossLinesAndIncreasingPerLine.class);
  private static final ImmutableSet<Long> EXCLUDED_NOTES = ImmutableSet.of(
      1528745129254L,  //hoobastank
      1532211947963L,  //jacob wetterling
      1536998993927L,  //orwell, 1984
      1532212591438L,  //bo bunham
      0L
  );

  @Override
  void runDQ() {
    Pattern fourDigitYear = Pattern.compile("\\d{4}");
    allNotes.stream()
        .filter(note -> !EXCLUDED_NOTES.contains(note.getId()))
        .filter(note -> note.getModel_id().equals(1436872005312L))
        .forEach(note -> {
          List<String> noteFields = splitCsvIntoCommaSeparatedList(note.getFields());
          String name = noteFields.get(0);
          String currentYear = String.valueOf(LocalDateTime.now().getYear());
          String knownFor = noteFields.get(4).replace("Present)", currentYear).replace("Přítomně)", currentYear);
          if (knownFor.length() > 0) {
            List<String> lines = Arrays.stream(knownFor.split("</div>"))
                .flatMap(line -> Stream.of(line.split("<div>")))
                .flatMap(line -> Stream.of(line.split("<br>")))
                .flatMap(line -> Stream.of(line.split("<br />")))
                .flatMap(line -> Stream.of(line.split("<li>")))
                .toList();
            List<Integer> maxYearsPerLine = lines.stream()
                .filter(line -> line.length() > 0)
                .flatMap(line -> {
                  List<Integer> yearsAsList = fourDigitYear.matcher(line).results()
                      .map(MatchResult::group)
                      .mapToInt(Integer::valueOf)
                      .boxed()
                      .toList();
                  if (!Ordering.natural().isOrdered(yearsAsList)) {
                    log.error("Person '%s': individual line does not have increasing dates when reading " +
                        "left-to-right: '%s'", name, line);
                    violations.put(this.getClass(), "nid:" + note.getId());
                  }
                  return yearsAsList.stream().max(Comparator.comparing(Integer::valueOf)).stream();
                })
                .toList();
            if (!Ordering.natural().reverse().isOrdered(maxYearsPerLine)) {
              log.error("Person '%s': does not have generally decreasing dates across lines when reading " +
                  "top-to-bottom: '%s'", name, maxYearsPerLine.stream()
                  .map(Object::toString)
                  .collect(Collectors.joining("-")));
              violations.put(this.getClass(), "nid:" + note.getId());
            }
          }
        });
  }
}
