package com.robertsanek.data.quality.anki;

import static j2html.TagCreator.table;
import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;

import java.io.File;
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
import com.google.common.collect.Lists;
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

  //ETL'd Anki data for use by all subclasses of DQBase
  public static List<Model> allModels = Unchecked.get(() -> new ModelEtl().getObjects());
  static List<Model> modelsInUse = allModels.stream()
      .filter(model -> !model.getName().startsWith("\u23F8"))
      .filter(model -> !model.getName().startsWith("~"))
      .filter(model -> model.getId() != 1487029322167L)  //Will's Cloze
      .filter(model -> model.getId() != 1511625126772L)  //Will's Basic
      .collect(Collectors.toList());
  static Map<Long, List<Model>> modelsByModelId = allModels.stream()
      .collect(Collectors.groupingBy(Model::getId));
  static List<Template> allTemplates = Unchecked.get(() -> new TemplateEtl().getObjects());
  static List<Template> templatesInUse = allTemplates.stream()
      .filter(template -> !template.getName().startsWith("@D"))
      .collect(Collectors.toList());
  static List<Review> allReviews = Unchecked.get(() -> new ReviewEtl().getObjects());
  static List<Card> cards = getAllCards();
  static Map<Long, List<Card>> cardsByNoteId = cards.stream()
      .collect(Collectors.groupingBy(Card::getNote_id));
  static Map<Long, Card> cardByCardId = toMap(cards, Card::getId);
  static List<Deck> allDecks = getAllDecks();
  static List<Long> relevantDeckIds = getRelevantDeckIds(allDecks);
  static List<Note> allNotes = Unchecked.get(() -> new NoteEtl().getObjects());
  static Map<Long, Note> noteByNoteId = toMap(allNotes, Note::getId);
  static long BASIC_MODEL_ID = 1416785626019L;
  static long CLOZE_MODEL_ID = 1410460771936L;
  static long PERSON_MODEL_ID = 1436872005312L;
  static long SYNONYM_MODEL_ID = 1539762208227L;
  static List<Field> allFields = Unchecked.get(() -> new FieldEtl().getObjects());
  static List<Field> fieldsInUse = allFields.stream()
      .filter(field -> !field.getName().startsWith("@Deprecated"))
      .filter(field -> !field.getName().startsWith("@Unused"))
      .collect(Collectors.toList());
  static Map<Long, List<Field>> requiredFieldsByModelId = allFields.stream()
      .filter(field -> field.getName().startsWith("⭐"))
      .collect(Collectors.groupingBy(Field::getModel_id));

  public static Set<String> getExistingPeopleInAnkiDbLowerCased() {
    Long personModelId = Iterables.getOnlyElement(DataQualityBase.allModels.stream()
        .filter(model -> model.getName().contains("Person"))
        .collect(Collectors.toList()))
        .getId();
    return DataQualityBase.getAllNotesInRelevantDecks(personModelId).stream()
        .map(note -> DataQualityBase.splitCsvIntoCommaSeparatedList(note.getFields()))
        .filter(fieldList -> fieldList.size() > 0)
        .map(fieldList -> cleanName(fieldList.get(0)).toLowerCase())
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
    if (fields.isEmpty()) {
      return Lists.newArrayList();
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
        .filter(deck -> deck.getName().equals("z") || deck.getName().startsWith("z::"))
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

    private List<IndividualError> errors = Lists.newArrayList();
    private List<IndividualError> warnings = Lists.newArrayList();

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

    ContainerTag getErrorsAsTable() {
      return getGenericAsTable(errors);
    }

    ContainerTag getWarningsAsTable() {
      return getGenericAsTable(warnings);
    }

    @VisibleForTesting
    static ContainerTag getGenericAsTable(List<IndividualError> type) {
      ContainerTag table = table();
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
