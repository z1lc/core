package com.robertsanek.data.quality.anki;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;

import com.google.common.collect.ImmutableSet;
import com.robertsanek.data.etl.local.sqllite.anki.Note;
import com.robertsanek.util.CommonProvider;

@IgnoreDQ(explanation = "in development")
@Deprecated
public class AllMentionsOfPersonsUseFullName extends DataQualityBase {

  ImmutableSet<Long> EXCLUDED_MODEL_IDS = ImmutableSet.of(
      1397938086594L,  //geography
      1436872005312L,  //person
      0L
  );

  @Override
  void runDQ() {
    ImmutableSet<String> names = CommonProvider.getCommonNames();
    List<Pair<Note, String>> collect = getAllNotesInRelevantDecks().parallelStream()
        .filter(note -> !EXCLUDED_MODEL_IDS.contains(note.getModel_id()))
        .map(note -> {
          String textInFields = DataQualityBase.splitCsvIntoCommaSeparatedList(note.getFields()).stream()
              .map(field -> Jsoup.parse(field).text())
              .collect(Collectors.joining(""));
          return Pair.of(note, textInFields);
        })
        .flatMap(noteAndFields -> names.stream()
            .flatMap(name -> {
              if (noteAndFields.getRight().contains(name)) {
                return Stream.of(Pair.of(noteAndFields.getLeft(), name));
              } else {
                return Stream.empty();
              }
            }))
        .filter(noteAndName -> {
          String name = noteAndName.getRight();
          Note note = noteAndName.getLeft();
          int nameStartIndex = note.getFields().indexOf(name);
          return !note.getFields().substring(nameStartIndex + name.length()).matches(" [A-Z].*");
        })
        .collect(Collectors.toList());
  }
}
