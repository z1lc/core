package com.robertsanek.data.quality.anki;

import java.util.List;

import org.jsoup.Jsoup;

import com.google.common.collect.ImmutableSet;
import com.robertsanek.data.etl.local.sqllite.anki.Card;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class TermDefinitionPairsDoNotHaveTermInDefinition extends DataQualityBase {

  static final Log log = Logs.getLog(TermDefinitionPairsDoNotHaveTermInDefinition.class);
  private static final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1540284130111L,
      1540995981721L,
      1531431249784L,
      1523341432815L,
      1529612417940L,
      1523814710740L,
      1542494392949L, //sorption
      1549084693051L, //python mapping
      0L
  );

  @Override
  void runDQ() {
    getAllNotesInRelevantDecks(BASIC_MODEL_ID, NOTE_ID_EXCLUSIONS).stream()
        .filter(note -> !note.getTags().contains("z::Languages::Spanish"))
        .filter(note -> {
          List<Card> cards = cardsByNoteId.get(note.getId());
          return cards.stream().anyMatch(card -> relevantDeckIds.contains(card.getDeck_id()));
        })
        .filter(note -> {
          List<String> fields = splitCsvIntoCommaSeparatedList(note.getFields());
          if (fields.size() > 2 && !fields.get(2).isEmpty()) {
            String term = Jsoup.parse(fields.get(0)).text().toLowerCase();
            String definition = Jsoup.parse(fields.get(1)).text().toLowerCase();
            return !term.isEmpty() && definition.contains(term);
          }
          return false;
        })
        .forEach(note -> {
          violations.put(this.getClass(), "nid:" + note.getId());
        });
  }
}
