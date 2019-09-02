package com.robertsanek.data.quality.anki;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.robertsanek.data.etl.local.sqllite.anki.Field;
import com.robertsanek.data.etl.local.sqllite.anki.Model;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

/* Splits fields into regular and subclass (annotated with "@SC") types, then checks ordering. In each case:
 * 1. Required fields (annotated with ⭐ [required] or ✨ [subclass required])
 * 2. Fields that control card generation (diamond)
 * 3. Other fields
 * 4. Deprecated fields
 */
public class FieldsShouldBeOrdered extends DataQualityBase {

  private static final Log log = Logs.getLog(FieldsShouldBeOrdered.class);
  private static final ImmutableSet<String> NOTE_EXCLUSIONS = ImmutableSet.of(
      "1 Basic",
      "Command-Line",
      "_Java Primitive Data Type",
      "_Power of Two",
      "_SI Prefix"
  );
  private static final ImmutableSet<String> FIELD_EXCLUSIONS = ImmutableSet.of(
      "\uD83D\uDD39Name Pronunciation \uD83D\uDD0A",
      "\uD83D\uDD39Pronunciation \uD83D\uDD0A",
      "\uD83D\uDD39Add Reverse \uD83D\uDD00"
  );
  private static final ImmutableSet<Pair<String, String>> NOTE_FIELD_EXCLUSIONS =
      ImmutableSet.of(
          Pair.of("4 Work of Art: Book Film Painting", "Author Count"),
          Pair.of("7 Programming Language Function", "\uD83D\uDD39Library/Package"),
          Pair.of("7 Programming Language Function", "\uD83D\uDD39Time Complexity"),
          Pair.of("Algorithm", "\uD83D\uDD39Average Case Time Complexity"),
          Pair.of("Geography Countries and Counties", "\uD83D\uDD39Currency Name"),
          Pair.of("Geography Countries and Counties", "\uD83D\uDD39Czech Name"),
          Pair.of("Geography Countries and Counties", "\uD83D\uDD39Currency Pronunciation"),
          Pair.of("8 Interview Question", "Additional Criteria"),
          Pair.of("8 Interview Question", "Insight Explanation"),
          Pair.of("8 Interview Question", "Complexity specifications")
      );

  @Override
  void runDQ() {
    List<Triple<String, String, Long>> notesWithIssues = new ArrayList<>();
    allFields.stream()
        .collect(Collectors.groupingBy(Field::getModel_id))
        .values()
        .forEach(fields -> {
          List<Field> subClassFields = fields.stream()
              .filter(field -> field.getName().contains("@SC"))
              .collect(Collectors.toList());
          List<Field> primaryFields = fields.stream()
              .filter(field -> !field.getName().contains("@SC"))
              .collect(Collectors.toList());
          notesWithIssues.addAll(doshit(subClassFields));
          notesWithIssues.addAll(doshit(primaryFields));
        });

    notesWithIssues.stream()
        .sorted()
        .forEach(noteFieldNamesPair -> {
          log.warn("Note '%s' does not have the expected ordering of fields: " +
              "did not expect location of '%s'.", noteFieldNamesPair.getLeft(), noteFieldNamesPair.getMiddle());
          violations.put(this.getClass(), String.format("\"note:%s\"", noteFieldNamesPair.getLeft()));
        });
  }

  private List<Triple<String, String, Long>> doshit(List<Field> fields) {
    List<Triple<String, String, Long>> notesWithIssues = new ArrayList<>();
    if (fields.size() > 0) {
      fields.sort(Comparator.comparing(Field::getOrdinal));
      AtomicBoolean requiredDone = new AtomicBoolean(false);
      AtomicBoolean diamondDone = new AtomicBoolean(false);
      AtomicBoolean otherDone = new AtomicBoolean(false);
      Model model = Iterables.getOnlyElement(modelsByModelId.get(fields.get(0).getModel_id()));
      boolean issue = false;
      for (Field field : fields) {
        if (field.getName().contains("\uD83D\uDDBC️") || //image emoji
            NOTE_EXCLUSIONS.contains(model.getName()) ||
            FIELD_EXCLUSIONS.contains(field.getName()) ||
            NOTE_FIELD_EXCLUSIONS.contains(Pair.of(model.getName(), field.getName()))) {
          continue;
        } else if (field.getName().startsWith("⭐") || field.getName().startsWith("✨")) {
          if (requiredDone.get()) {
            issue = true;
          }
        } else if (field.getName().startsWith("\uD83D\uDD39")) {
          requiredDone.set(true);
          if (diamondDone.get()) {
            issue = true;
          }
        } else if (field.getName().startsWith("@Deprecated") || field.getName().startsWith("@Unused")) {
          requiredDone.set(true);
          diamondDone.set(true);
          otherDone.set(true);
        } else {
          requiredDone.set(true);
          diamondDone.set(true);
          if (otherDone.get()) {
            issue = true;
          }
        }
        if (issue && !model.getName().startsWith("\u23F8") && !model.getName().startsWith("~")) {
          notesWithIssues.add(Triple.of(model.getName(), field.getName(), model.getId()));
          break;
        }
      }
    }

    return notesWithIssues;
  }
}
