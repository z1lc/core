package com.robertsanek.data.quality.anki;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import com.google.inject.Inject;
import com.robertsanek.data.etl.local.sqllite.anki.Note;
import com.robertsanek.data.etl.local.sqllite.anki.connect.AnkiConnectUtils;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllNotesWithMultipleNamesShouldHaveCorrespondingSynonymCard extends DataQualityBase {

  static final Log log = Logs.getLog(AllNotesWithMultipleNamesShouldHaveCorrespondingSynonymCard.class);
  private static final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1404606499435L,
      1410152894223L,
      1413332193963L,
      1416504398686L,
      1417879628916L,
      1484426154286L,
      1498513464485L,
      1510330771803L,
      1511201390721L,
      1511454747577L,
      1511886614702L,
      1513901636841L,
      1515772913667L,
      1515787277118L,
      1516390264190L,
      1518115468852L,
      1518804700876L,
      1519226206159L,
      1519707908028L,
      1521318205496L,
      1525055798613L,
      1528488919177L,
      1528571439912L,
      1528780454187L,
      1530571111779L,
      1531169716077L,
      1531170357897L,
      1533479800958L,
      1535011706006L,
      1537577749003L,
      1538439199350L,
      1417827045884L,
      1475390568156L,
      1488868306846L,
      1504570896383L,
      1512096009344L,
      1519662260429L,
      1519662260504L,
      1520548516762L,
      1524678898907L,
      1527370950739L,
      1539116602699L,
      1540254370471L,  //goiter spellings
      1501218242438L,  //Target Retirement/Date fund 2 abbrevs
      1523823255658L,  //kitchen patrol abbrev does not match up
      1540891062025L,  //Rockefeller Commission
      1541282237764L,  //grower or shower
      1539990213928L,  //fed reserve
      1543120272629L,
      1548026908032L,
      1541197458235L,  //creative/deliberate procrastination
      1549661615135L,  //india train station
      1511886826563L,  //snapshot/repeatable read
      1550216794965L,  //docker
      1550216794959L,  //docker

      //Japanese Emperors
      1532774175301L,
      1532774135965L,
      1532774106415L,
      1532774000812L,

      1553746047485L,
      1545076640994L,
      1556400664224L, //MMDB
      1504548042442L,
      1504573008645L,
      1557517888678L,
      1559631381236L,
      1560116533529L,
      1560894873886L,
      1561587972179L,
      1566496220590L,
      1518193619848L,
      1574475618434L,
      1574475618433L,
      1574475618432L,
      1575594534141L,
      1578434308516L,
      1584649312985L,
      1586369991165L,
      1583958540419L,
      1408840156470L,
      1574095336750L,
      1574095336754L,
      1593436805748L,
      1543315636834L,
      1282537182547L,
      1511195789510L,
      1583888550469L,
      1549661605993L,
      1544122448691L,
      1573959880666L,
      1611078621906L,
      1612995185918L,
      1631767315766L,
      1637623255521L,
      1667681216880L,
      1515263963068L,
      0L
  );
  private static final boolean shouldGenerateCsvOutput = false;
  private static final int MIN_CHARACTERS_TO_CONSIDER_SENTENCE = 100;

  @Inject AnkiConnectUtils ankiConnectUtils;

  static List<String> getIndividualNames(List<String> fields) {
    String front = Jsoup.parse(fields.get(0)).text();
    return Lists.newArrayList(front.split(" or "));
  }

  static String getPrimaryTextFromFields(List<String> fields, Long modelId) {
    String abbrev = "";
    if (fields.size() >= 6 && modelId == BASIC_MODEL_ID) {
      abbrev = Jsoup.parse(fields.get(5)).text();
    }
    List<String> individualOr = getIndividualNames(fields);
    return abbrev.isEmpty() ? individualOr.get(0) : abbrev + " / " + individualOr.get(0);
  }

  @Override
  void runDQ() {
    Map<String, Note> primarySynonymTermToNote = getAllNotesInRelevantDecks(SYNONYM_MODEL_ID).stream()
        .collect(Collectors.toMap(note -> {
          List<String> fields = splitCsvIntoCommaSeparatedList(note.getFields());
          return Jsoup.parse(fields.get(0)).text().replaceAll("\\[sound:.+]", "");
        }, Functions.identity()));

    ankiConnectUtils.loadProfile("z1lc");

    Streams.concat(
        getAllNotesInRelevantDecks(BASIC_MODEL_ID, NOTE_ID_EXCLUSIONS).stream(),
        getAllNotesInRelevantDecks(VENUE_MODEL_ID, NOTE_ID_EXCLUSIONS).stream(),
        getAllNotesInRelevantDecks(PERSON_MODEL_ID, NOTE_ID_EXCLUSIONS).stream())
        .forEach(note -> {
          List<String> fields = splitCsvIntoCommaSeparatedList(note.getFields());
          String front = Jsoup.parse(fields.get(0)).text().replaceAll("^Visualize ", "");
          boolean frontContainsOr = front.contains(" or ");
          List<String> individualOr = Lists.newArrayList(front.split(" or "));
          String primary = getPrimaryTextFromFields(fields, note.getModel_id()).replaceAll("^Visualize ", "");
          boolean synonymAlreadyExists = primarySynonymTermToNote.containsKey(primary);
          boolean probablySentence = front.endsWith(".") || front.endsWith("?")
              || front.length() >= MIN_CHARACTERS_TO_CONSIDER_SENTENCE;

          if (synonymAlreadyExists && !probablySentence) {
            Note synonymNote = primarySynonymTermToNote.get(primary);
            List<String> synonymFields = splitCsvIntoCommaSeparatedList(synonymNote.getFields());
            if (note.getModel_id() == BASIC_MODEL_ID) {
              Map<String, String> fieldUpdates = Maps.newHashMap();
              if (synonymFields.size() < 12 || Jsoup.parse(synonymFields.get(11)).text().isEmpty()) {
                // means we don't have anything in the extra field in the synonym card
                String maybeExtra = fields.get(1);
                if (!maybeExtra.equals("Identify:")) {
                  fieldUpdates.put("Extra ➕", fields.get(1));
                }
              }
              if (synonymFields.size() < 13 || Jsoup.parse(synonymFields.get(12)).text().isEmpty()) {
                // means we don't have anything in the extra image field in the synonym card
                getFirstNonNull(fields, List.of(6, 7, 8))
                    .ifPresent(image -> fieldUpdates.put("Extra Image \uD83D\uDDBC️", image));
              }
              if (!ankiConnectUtils.updateNoteFields(synonymNote.getId(), fieldUpdates)) {
                throw new RuntimeException(String.format(
                    "Failed to use anki-connect to update Synonym with note id %s.", synonymNote.getId()));
              }
            }
          }

          if (frontContainsOr && !synonymAlreadyExists && !probablySentence) {
            String context = "";
            int contextField = -1;
            if (note.getModel_id() == BASIC_MODEL_ID) {
              contextField = 3;
            } else if (note.getModel_id() == PERSON_MODEL_ID) {
              contextField = 5;
            } else if (note.getModel_id() == VENUE_MODEL_ID) {
              contextField = 8;
            }
            if (fields.size() > contextField) {
              context = Jsoup.parse(fields.get(contextField)).text().replace("%", "");
            }
            violations.put(this.getClass(), "nid:" + note.getId().toString());
            if (shouldGenerateCsvOutput) {
              int left = 10 - individualOr.size();
              log.info("\"%s\",\"%s\",\"%s\"", String.join("\",\"", individualOr), String.join("\",\"",
                  Collections.nCopies(Math.max(left, 0), "")), context);

            } else {
              log.error("Note '%s' does not have a corresponding synonym note.", front.replace("%", ""));
            }
          }
        });
    ankiConnectUtils.triggerSync();
  }

  Optional<String> getFirstNonNull(List<String> fields, List<Integer> fieldIds) {
    return IntStream.range(0, fields.size())
        .filter(fieldIds::contains)
        .mapToObj(fields::get)
        .filter(field -> !field.isBlank())
        .findFirst();
  }

}
