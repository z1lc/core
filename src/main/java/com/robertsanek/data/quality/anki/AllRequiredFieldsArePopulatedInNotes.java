package com.robertsanek.data.quality.anki;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.robertsanek.data.etl.local.sqllite.anki.Field;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllRequiredFieldsArePopulatedInNotes extends DataQualityBase {

  static final Log log = Logs.getLog(AllRequiredFieldsArePopulatedInNotes.class);
  private static final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1490728803434L,  //nslcd
      1479283324325L,  //launchd
      1475892009859L,  //collectd
      1539052554034L,  //Leitner couldn't find image
      0L
  );

  @Override
  void runDQ() {
    Set<String> noteIdViolationsList = Sets.newHashSet();
    allNotes.forEach(note -> {
      List<Field> requiredNoteFields = Optional.ofNullable(requiredFieldsByModelId.get(note.getModel_id()))
          .orElse(Lists.newArrayList());
      List<String> fieldsAsList = splitCsvIntoCommaSeparatedList(note.getFields());
      requiredNoteFields.forEach(field -> {
        if ((fieldsAsList.size() < field.getOrdinal() + 1 ||
            fieldsAsList.get(field.getOrdinal().intValue()).isEmpty())
            && !NOTE_ID_EXCLUSIONS.contains(note.getId())) {
          noteIdViolationsList.add("nid:" + note.getId());
        }
      });
    });
    if (noteIdViolationsList.size() > 0) {
      log.error("%s fields marked as required with no content:", noteIdViolationsList.size());
      dqInformation.error(this.getClass(), noteIdViolationsList);
    }
  }
}
