package com.robertsanek.data.quality.anki;

import static com.robertsanek.data.etl.local.sqllite.anki.NoteEtl.FIELD_SEPARATOR;
import static j2html.TagCreator.table;
import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Streams;
import com.robertsanek.data.etl.local.sqllite.anki.*;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

import j2html.tags.ContainerTag;

public abstract class DataQualityBase {

  static final Log log = Logs.getLog(DataQualityBase.class);
  static final Pattern CODE_TABLE = Pattern.compile("<table>[\\s\\S]*highlight[\\s\\S]*</table>");
  static final File MEDIA_FOLDER =
      new File(CrossPlatformUtils.getAnkiMediaFolderForUserz1lcIncludingTrailingSlash().orElseThrow());
  static final Multimap<Class<? extends DataQualityBase>, String> violations =
      Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
  static final DQInformation dqInformation = new DQInformation();
  static final long BASIC_MODEL_ID = 1416785626019L;
  static final long CLOZE_MODEL_ID = 1410460771936L;
  static final long PERSON_MODEL_ID = 1436872005312L;
  static final long WORK_OF_ART_MODEL_ID = 1410538725370L;
  static final long SYNONYM_MODEL_ID = 1539762208227L;
  static final long SOFTWARE_MODEL_ID = 1521236334913L;
  static final long PROGRAMMING_LANGUAGE_FUNCTION_MODEL_ID = 1465970513428L;
  static final long INTERVIEW_QUESTION_MODEL_ID = 1548677631975L;
  static final long VENUE_MODEL_ID = 1537578027284L;

  //ETL'd Anki data for use by all subclasses of DQBase
  static List<Model> allModels;
  static List<Model> modelsInUse;
  static Map<Long, List<Model>> modelsByModelId;
  static List<Template> allTemplates;
  static List<Template> templatesInUse;
  static List<Review> allReviews;
  static List<Card> cards;
  static Map<Long, List<Card>> cardsByNoteId;
  static Map<Long, Card> cardByCardId;
  static List<Deck> allDecks;
  static List<Long> relevantDeckIds;
  static List<Note> allNotes;
  static Map<Long, Note> noteByNoteId;
  static List<Field> allFields;
  static List<Field> fieldsInUse;
  static Map<Long, List<Field>> requiredFieldsByModelId;
  static Map<Long, List<Field>> fieldsByModelId;

  static {
    recomputeCachedFields();
  }

  public static void recomputeCachedFields() {
    if (CrossPlatformUtils.isRunningInsideDocker()) {
      return;
    }
    allModels = Unchecked.get(() -> new ModelEtl().getObjects());
    modelsInUse = allModels.stream()
        .filter(model -> !model.getName().startsWith("\u23F8"))
        .filter(model -> !model.getName().startsWith("~"))
        .filter(model -> model.getId() != 1487029322167L)  //Will's Cloze
        .filter(model -> model.getId() != 1511625126772L)  //Will's Basic
        .collect(Collectors.toList());
    modelsByModelId = allModels.stream()
        .collect(Collectors.groupingBy(Model::getId));
    allTemplates = Unchecked.get(() -> new TemplateEtl().getObjects());
    templatesInUse = allTemplates.stream()
        .filter(template -> !template.getName().startsWith("@D"))
        .collect(Collectors.toList());
    allReviews = Unchecked.get(() -> new ReviewEtl().getObjects());
    cards = getAllCards();
    cardsByNoteId = cards.stream()
        .collect(Collectors.groupingBy(Card::getNote_id));
    cardByCardId = toMap(cards, Card::getId);
    allDecks = getAllDecks();
    relevantDeckIds = getRelevantDeckIds(allDecks);
    allNotes = Unchecked.get(() -> new NoteEtl().getObjects());
    noteByNoteId = toMap(allNotes, Note::getId);
    allFields = Unchecked.get(() -> new FieldEtl().getObjects());
    fieldsInUse = allFields.stream()
        .filter(field -> !field.getName().startsWith("@Deprecated"))
        .filter(field -> !field.getName().startsWith("@Unused"))
        .collect(Collectors.toList());
    fieldsByModelId = allFields.stream()
        .collect(Collectors.groupingBy(Field::getModel_id));
    requiredFieldsByModelId = allFields.stream()
        .filter(field -> field.getName().startsWith("⭐"))
        .collect(Collectors.groupingBy(Field::getModel_id));
  }

  public static List<Model> getAllModels() {
    return allModels;
  }

  public static Map<Long, List<Field>> getFieldsByModelId() {
    return fieldsByModelId;
  }

  public static Map<Long, List<Model>> getModelsByModelId() {
    return modelsByModelId;
  }

  public static List<Note> getExistingPeopleInAnkiDb() {
    Long personModelId = Iterables.getOnlyElement(DataQualityBase.allModels.stream()
        .filter(model -> model.getName().contains("3 Person"))
        .collect(Collectors.toList()))
        .getId();
    return DataQualityBase.getAllNotesInRelevantDecks(personModelId);
  }

  public static Set<String> getExistingPeopleInAnkiDbLowerCased() {
    return getExistingPeopleInAnkiDb().stream()
        .map(note -> DataQualityBase.splitCsvIntoCommaSeparatedList(note.getFields()))
        .filter(fieldList -> fieldList.size() > 0)
        .map(fieldList -> cleanName(fieldList.get(0)).toLowerCase())
        .collect(Collectors.toSet());
  }

  public static Set<String> getExistingWorksOfArtInAnkiDbLowerCased() {
    Long workOfArtModelId = Iterables.getOnlyElement(DataQualityBase.allModels.stream()
        .filter(model -> model.getName().contains("Work of Art"))
        .collect(Collectors.toList()))
        .getId();
    return DataQualityBase.getAllNotesInRelevantDecks(workOfArtModelId).stream()
        .map(note -> DataQualityBase.splitCsvIntoCommaSeparatedList(note.getFields()))
        .filter(fieldList -> fieldList.size() > 0)
        .map(fieldList -> Jsoup.parse(fieldList.get(0)).text().toLowerCase())
        .collect(Collectors.toSet());
  }

  //If you change this, also change _AnkiLibrary.js#_cleanName
  public static String cleanName(String dirtyHtmlName) {
    return Jsoup.parse(dirtyHtmlName).text()
        .replaceAll(" \\(n[é|e]e.+\\)", "")
        .replaceAll(" or .*", "")
        .replaceAll(".+ / ", "")
        .replace("ø", "o")
        .replace("\"", "")
        //usually a forward slash is used in the case of DHH / David Heinemeier Hansson, but we may also find
        //it in a situation without a space after it. In this case, just replace both.
        .replace("/ ", "")
        .replace("/", "")
        .replace("& ", "")
        .replace("&", "")
        .replace("*", "")
        .replace(":", "")
        .replace(",", "")
        //two different types of apostrophes
        .replace("'", "")
        .replace("’", "");
  }

  public static void prepareForDQ() {
    violations.clear();
    dqInformation.clear();
  }

  public static Map<Long, Note> getNotesByNoteId() {
    return noteByNoteId;
  }

  public static Map<Long, Card> getCardsByCardId() {
    return cardByCardId;
  }

  public static Map<Long, List<Card>> getCardsByNoteId() {
    return cardsByNoteId;
  }

  public static List<Review> getAllReviews() {
    return allReviews;
  }

  public static List<Note> getAllNotesInRelevantDecks() {
    return getAllNotesInRelevantDecks(null);
  }

  public static List<Note> getAllNotesInRelevantDecks(Long modelId) {
    return getAllNotesInRelevantDecks(modelId, Collections.emptySet());
  }

  public static List<Note> getAllNotesInRelevantDecks(Long modelId, Set<Long> noteIdExclusions) {
    return allNotes.stream()
        .filter(note -> modelId == null || note.getModel_id().equals(modelId))
        .filter(note -> !noteIdExclusions.contains(note.getId()))
        .filter(note -> {
          List<Card> cards = cardsByNoteId.get(note.getId());
          return cards.stream().anyMatch(card -> relevantDeckIds.contains(card.getDeck_id()));
        })
        .collect(Collectors.toList());
  }

  public static List<String> splitCsvIntoCommaSeparatedList(String fields) {
    if (fields == null || fields.isEmpty()) {
      return new ArrayList<>();
    }
    List<String> fieldsList = Arrays.asList(fields.split("\",\""));
    int lastIndex = fieldsList.size() - 1;
    fieldsList.set(0, fieldsList.get(0).substring(1));
    fieldsList.set(lastIndex, fieldsList.get(lastIndex).substring(0, fieldsList.get(lastIndex).length() - 1));
    return fieldsList;
  }

  public static List<Deck> getAllDecks() {
    return Unchecked.get(() -> new DeckEtl().getObjects());
  }

  public static List<Long> getRelevantDeckIds(List<Deck> allDecks) {
    return allDecks.stream()
        .filter(deck -> deck.getName().equals("z") || deck.getName().startsWith("z" + FIELD_SEPARATOR))
        .map(Deck::getId)
        .collect(Collectors.toList());
  }

  public static List<Card> getAllCards() {
    return Unchecked.get(() -> new CardEtl().getObjects());
  }

  public static <T> Map<Long, T> toMap(List<T> list, Function<T, Long> mappingFunction) {
    return list.stream()
        .collect(Collectors.toMap(mappingFunction, Function.identity()));
  }

  abstract void runDQ();

  static class IndividualError {

    private String name;
    private String search;

    IndividualError(String name, String search) {
      this.name = name;
      this.search = search;
    }

    @Override
    public String toString() {
      return String.format("%s: (%s)", name, search);
    }

    public String getName() {
      return name;
    }

    public String getSearch() {
      return search;
    }
  }

  static class DQInformation {

    private List<IndividualError> errors = new ArrayList<>();
    private List<IndividualError> warnings = new ArrayList<>();

    void clear() {
      errors.clear();
      warnings.clear();
    }

    void error(String sqlFileName, Collection<String> noteOrCardIdList) {
      dqInformation.error(new IndividualError(sqlFileName, String.join(" or ", noteOrCardIdList)));
    }

    void error(Class<? extends DataQualityBase> clazz, Collection<String> noteOrCardIdList) {
      if (noteOrCardIdList.size() > 0) {
        dqInformation.error(new IndividualError(clazz.getSimpleName(), String.join(" or ", noteOrCardIdList)));
      }
    }

    void warn(Class<? extends DataQualityBase> clazz, Collection<String> noteOrCardIdList) {
      if (noteOrCardIdList.size() > 0) {
        dqInformation.warn(new IndividualError(clazz.getSimpleName(), String.join(" or ", noteOrCardIdList)));
      }
    }

    void error(String template, Object... objects) {
      String str = String.format(template, objects);
      errors.add(new IndividualError(str, ""));
      log.error(str);
    }

    void warn(String template, Object... objects) {
      String str = String.format(template, objects);
      warnings.add(new IndividualError(str, ""));
      log.warn(str);
    }

    void error(IndividualError individualError) {
      errors.add(individualError);
      log.error(individualError.toString());
    }

    void warn(IndividualError individualError) {
      warnings.add(individualError);
      log.warn(individualError.toString());
    }

    List<String> getErrors() {
      return errors.stream().map(IndividualError::toString).collect(Collectors.toList());
    }

    ContainerTag<?> getErrorsAsTable() {
      return getGenericAsTable(errors);
    }

    ContainerTag<?> getWarningsAsTable() {
      return getGenericAsTable(warnings);
    }

    @VisibleForTesting
    static ContainerTag<?> getGenericAsTable(List<IndividualError> type) {
      ContainerTag<?> table = table();
      type.forEach(individualErrorOrWarning ->
          table.with(tr().with(
              td(individualErrorOrWarning.getName()),
              td(individualErrorOrWarning.getSearch()))));
      return table;
    }

    List<String> getWarnings() {
      return warnings.stream().map(IndividualError::toString).collect(Collectors.toList());
    }

    List<String> getErrorsAndWarnings() {
      return Streams.concat(getErrors().stream(), getWarnings().stream())
          .map(StringEscapeUtils::escapeHtml4)
          .collect(Collectors.toList());
    }

    boolean isErrorAndWarningFree() {
      return errors.size() == 0 && warnings.size() == 0;
    }

    boolean isErrorFree() {
      return errors.size() == 0;
    }
  }

}
