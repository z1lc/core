package com.robertsanek.data.quality.anki;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.robertsanek.data.etl.local.sqllite.anki.connect.AnkiConnectUtils;

public class FindReplaceAllBadStrings extends DataQualityBase {

  private static final Map<String, String> findToReplaceMap = ImmutableMap.<String, String>builder()
      .put("<br></td>", "</td>")
      .put("<br></th>", "</th>")
      .put("<img>", "")
      .put("&nbsp;", " ")
      .build();
  private static final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1548653635284L //regex spaces &nbsp;
  );

  @Inject AnkiConnectUtils ankiConnectUtils;

  @Override
  void runDQ() {
    AtomicLong violations = new AtomicLong(0);
    findToReplaceMap.forEach((find, replace) ->
        ankiConnectUtils.getNoteIdsForSearch(find).stream()
            .filter(noteId -> !NOTE_ID_EXCLUSIONS.contains(noteId))
            .forEach(badNoteId -> {
              Map<String, String> fieldsToUpdate = ankiConnectUtils.getFieldsForNote(badNoteId).entrySet().stream()
                  .filter(entry -> entry.getValue().contains(find))
                  .map(entry -> Map.entry(entry.getKey(), entry.getValue().replaceAll(find, replace)))
                  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
              log.info("Found violation '%s' in note id %s. Will update fields %s",
                  find, badNoteId, fieldsToUpdate.keySet());
              if (ankiConnectUtils.updateNoteFields(badNoteId, fieldsToUpdate)) {
                violations.incrementAndGet();
              } else {
                log.error("Failed to update fields for note id %s.", badNoteId);
              }
            }));
    if (violations.get() > 0) {
      log.info("Fixed %s total violations.", violations.get());
      ankiConnectUtils.triggerSync();
    }
  }
}
