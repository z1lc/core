package com.robertsanek.data.quality.anki;

import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;

import com.google.common.collect.Sets;
import com.robertsanek.data.etl.local.sqllite.anki.Field;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class NoSynonymIsAFullSubstringOfAnotherSynonym extends DataQualityBase {

  public static int MAX_SYNONYM_INDEX = (int) fieldsInUse.stream()
      .filter(field -> field.getModel_id() == SYNONYM_MODEL_ID)
      .filter(field -> field.getName().toLowerCase().contains("synonym"))
      .mapToLong(Field::getOrdinal)
      .max()
      .orElseThrow();

  static final Log log = Logs.getLog(NoSynonymIsAFullSubstringOfAnotherSynonym.class);

  public static List<String> getSublistOfSynonymFieldsWithoutContext(List<String> fields) {
    List<String> fieldsNoSynonym = fields.subList(0, Math.min(MAX_SYNONYM_INDEX + 1, fields.size()));
    return fieldsNoSynonym.stream().filter(item -> !item.isEmpty()).toList();
  }

  @Override
  void runDQ() {
    final Set<Long> NOTE_ID_EXCLUSIONS = Sets.newHashSet(
        1540249001626L,  //acid & LSD / LySergic acid Diethylamide
        1540249026737L,  //shrooms & magic mushrooms
        0L
    );
    getAllNotesInRelevantDecks(SYNONYM_MODEL_ID, NOTE_ID_EXCLUSIONS)
        .forEach(note -> {
          List<String> fieldsAsString = splitCsvIntoCommaSeparatedList(note.getFields());
          List<String> fields = getSublistOfSynonymFieldsWithoutContext(fieldsAsString)
              .stream()
              .map(synonym -> Jsoup.parse(synonym).text().replaceAll("\\[sound:.+]", ""))
              .filter(syn -> !syn.isEmpty())
              .toList();
          fields.forEach(syn1 -> fields.forEach(syn2 -> {
            if (syn1.contains(syn2) && !syn1.equals(syn2)) {
              log.error("Synonym '%s' is a full substring of synonym '%s'. " +
                  "This should likely be captured as an abbreviation instead.", syn2, syn1);
              violations.put(this.getClass(), "nid:" + note.getId());
            }
          }));
        });
  }
}
