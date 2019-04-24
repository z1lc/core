package com.robertsanek.data.quality.anki;

import java.util.List;

import org.jsoup.Jsoup;

import com.google.common.collect.ImmutableSet;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class ProgrammingLanguageFunctionNameDoesNotAppearInDescription extends DataQualityBase {

  static final Log log = Logs.getLog(ProgrammingLanguageFunctionNameDoesNotAppearInDescription.class);
  private static final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1515700596299L,  //classtag
      1515700825883L,  //typetage
      1539303935432L,  //natural ordering
      1541167534874L,  //jQuery html
      1541167397507L,  //jQuery attr
      1541167127435L,  //jQuery children
      1541166471486L,  //jQuery CSS
      1547512310591L,  //Python copy
      1547512235362L,  //Python index
      1548466245527L,  //Python add
      1549325202594L,  //Python iter
      1549661107791L,  //Python Queue
      1549661169660L,  //Python insert
      1555884188762L,  //Python permutations
      0L
  );

  @Override
  void runDQ() {
    getAllNotesInRelevantDecks(1465970513428L, NOTE_ID_EXCLUSIONS)
        .forEach(note -> {
          List<String> fields = splitCsvIntoCommaSeparatedList(note.getFields());
          String functionName = Jsoup.parse(fields.get(0)).text();
          String functionDescription = Jsoup.parse(fields.get(4)).text().toLowerCase();
          if (functionDescription.contains(functionName.toLowerCase())) {
            log.error("Programming language function '%s': function description contains function name.",
                functionName);
            violations.put(this.getClass(), "nid:" + note.getId());
          }
        });
  }
}
