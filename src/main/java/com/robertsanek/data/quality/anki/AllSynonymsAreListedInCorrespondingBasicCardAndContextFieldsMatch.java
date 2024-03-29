package com.robertsanek.data.quality.anki;

import static com.robertsanek.data.quality.anki.AllNotesWithMultipleNamesShouldHaveCorrespondingSynonymCard.getIndividualNames;
import static com.robertsanek.data.quality.anki.AllNotesWithMultipleNamesShouldHaveCorrespondingSynonymCard.getPrimaryTextFromFields;
import static com.robertsanek.data.quality.anki.NoSynonymIsAFullSubstringOfAnotherSynonym.getSublistOfSynonymFieldsWithoutContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;

import com.google.common.collect.ImmutableSet;
import com.robertsanek.data.etl.local.sqllite.anki.Field;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllSynonymsAreListedInCorrespondingBasicCardAndContextFieldsMatch extends DataQualityBase {

  public static int SYNONYM_INDEX_CONTEXT = (int) fieldsInUse.stream()
      .filter(field -> field.getModel_id() == SYNONYM_MODEL_ID)
      .filter(field -> field.getName().equals("Context \uD83D\uDCA1"))
      .mapToLong(Field::getOrdinal)
      .max()
      .orElseThrow();
  public static int BASIC_INDEX_CONTEXT = (int) fieldsInUse.stream()
      .filter(field -> field.getModel_id() == BASIC_MODEL_ID)
      .filter(field -> field.getName().equals("Context \uD83D\uDCA1"))
      .mapToLong(Field::getOrdinal)
      .max()
      .orElseThrow();
  static final Log log = Logs.getLog(AllSynonymsAreListedInCorrespondingBasicCardAndContextFieldsMatch.class);
  private static final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1282537182547L,
      1511629255890L,
      1512096009344L,
      1516119723486L,
      1523822719466L,
      1524678898907L,
      1527370950739L,
      1529220826990L,
      1530130969056L,
      1541283597222L,
      1542667552632L,
      1471022039419L,
      1419223095794L,
      1550217019710L, //node
      1557435608561L,
      1530572200296L,
      1420173931436L,
      1566588027914L,
      1574719411307L,
      1583888550469L,
      1631767315766L,
      // Czech imported start
      1459400148719L,
      1532180104197L,
      1469971057949L,
      1469971060470L,
      1502693103032L,
      1532180104407L,
      1502693101744L,
      1532180104275L,
      1469971059545L,
      1502693102454L,
      1532180104017L,
      1469971057672L,
      1469971060712L,
      // Czech imported end
      1510330338872L,
      0L
  );

  private static Pair<String, List<String>> getCleanedIndividualSynonyms(List<String> fields) {
    String primary = Jsoup.parse(fields.get(0)).text().replaceAll("\\[sound:.+]", "");
    List<String> all = new ArrayList<>();
    all.add(primary);
    for (int i = 1; i < fields.size(); i++) {
      all.add(Jsoup.parse(fields.get(i)).text().replaceAll("\\[sound:.+]", ""));
    }
    return Pair.of(primary, all);
  }

  @Override
  void runDQ() {
    Map<String, SynonymInformation> primarySynonymToRestOfSynonymsMap =
        getAllNotesInRelevantDecks(SYNONYM_MODEL_ID).stream()
            .map(note -> {
              List<String> fields = splitCsvIntoCommaSeparatedList(note.getFields());
              Pair<String, List<String>> cleanedIndividualSynonyms =
                  getCleanedIndividualSynonyms(getSublistOfSynonymFieldsWithoutContext(fields));
              return new SynonymInformation(cleanedIndividualSynonyms.getLeft(), cleanedIndividualSynonyms.getRight(),
                  fields);
            })
            .collect(Collectors.toMap(SynonymInformation::getPrimarySynonym, Function.identity()));

    getAllNotesInRelevantDecks(BASIC_MODEL_ID, NOTE_ID_EXCLUSIONS)
        .forEach(note -> {
          List<String> fields = splitCsvIntoCommaSeparatedList(note.getFields());
          List<String> individualNames = getIndividualNames(fields);
          String primary = getPrimaryTextFromFields(fields, note.getModel_id());
          SynonymInformation correspondingSynonym = primarySynonymToRestOfSynonymsMap.get(primary);
          if (correspondingSynonym != null) {
            if (!correspondingSynonym.getAllSynonyms().subList(1, correspondingSynonym.getAllSynonyms().size())
                .equals(individualNames.subList(1, individualNames.size()))) {
              log.error(
                  "Primary term '%s' has inconsistent list of synonyms between Synonym and Basic card types.\n" +
                      "Basic:   %s\n" +
                      "Synonym: %s",
                  primary, individualNames, correspondingSynonym.getAllSynonyms());
              violations.put(this.getClass(), "nid:" + note.getId().toString());
            } else {
              List<String> allSynFields = correspondingSynonym.getAllFields();
              if (fields.size() > BASIC_INDEX_CONTEXT && allSynFields.size() > SYNONYM_INDEX_CONTEXT) {
                String basicContext = fields.get(BASIC_INDEX_CONTEXT);
                String synContext = allSynFields.get(SYNONYM_INDEX_CONTEXT);
                if (!basicContext.equals(synContext)) {
                  log.error("Primary term-synonym pair '%s' does not have matching context fields.\n" +
                          "Basic:   %s\n" +
                          "Synonym: %s",
                      primary, basicContext, synContext);
                  violations.put(this.getClass(), "nid:" + note.getId().toString());
                }
              }
            }
          }
        });
  }

  static class SynonymInformation {

    String primarySynonym;
    List<String> allSynonyms;
    List<String> allFields;

    SynonymInformation(String primarySynonym, List<String> allSynonyms, List<String> allFields) {
      this.primarySynonym = primarySynonym;
      this.allSynonyms = allSynonyms;
      this.allFields = allFields;
    }

    public String getPrimarySynonym() {
      return primarySynonym;
    }

    public List<String> getAllSynonyms() {
      return allSynonyms;
    }

    public List<String> getAllFields() {
      return allFields;
    }
  }
}
