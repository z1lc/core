package com.robertsanek.data.quality.anki;

import java.util.List;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.robertsanek.data.etl.local.sqllite.anki.Note;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllNotesWithEnumsConformToSet extends DataQualityBase {

  static final Log log = Logs.getLog(AllNotesWithEnumsConformToSet.class);
  private static final String ENUM_SYMBOL = "\uD83D\uDD33";
  final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1525679058418L,
      1525679159684L,
      1525679224698L,
      1542341171861L,
      1490892718619L,
      1528425911054L,
      1538514663195L,
      1545656297838L,
      1552519997695L, 1552519925954L, //Dynamo get / put
      1519086428411L,
      0L
  );

  @Override
  void runDQ() {
    List<EnumField> enumFields = allFields.stream()
        .filter(field -> field.getName().contains(ENUM_SYMBOL))
        .map(field -> {
          int left = field.getName().indexOf("(") + 1;
          int right = field.getName().indexOf(")");
          List<String> enums = Lists.newArrayList(field.getName().substring(left, right).split(" / "));
          return new EnumField(field.getName(), field.getModel_id(), field.getOrdinal().intValue(), enums);
        })
        .toList();

    //TODO: assumes all enum fields are required (can result in array out of bounds otherwise)
    enumFields.forEach(enumField -> {
      List<Note> correspondingNotes = getAllNotesInRelevantDecks(enumField.getModelId());
      correspondingNotes.stream()
          .filter(note -> !NOTE_ID_EXCLUSIONS.contains(note.getId()))
          .forEach(note -> {
            List<String> fields = splitCsvIntoCommaSeparatedList(note.getFields());
            String individualNoteEnum = fields.get(enumField.getOrdinal());
            if (!enumField.getEnums().contains(individualNoteEnum)) {
              log.error("Field '%s' in card '%s' is not one of '%s'.",
                  individualNoteEnum, fields.get(0), String.join(" / ", enumField.getEnums()));
              violations.put(this.getClass(), "nid:" + note.getId());
            }
          });
    });
  }

  static class EnumField {

    String name;
    long modelId;
    int ordinal;
    List<String> enums;

    EnumField(String name, long modelId, int ordinal, List<String> enums) {
      this.name = name;
      this.modelId = modelId;
      this.ordinal = ordinal;
      this.enums = enums;
    }

    public String getName() {
      return name;
    }

    public long getModelId() {
      return modelId;
    }

    public int getOrdinal() {
      return ordinal;
    }

    public List<String> getEnums() {
      return enums;
    }
  }
}
