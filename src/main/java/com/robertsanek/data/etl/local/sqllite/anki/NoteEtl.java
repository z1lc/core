package com.robertsanek.data.etl.local.sqllite.anki;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.robertsanek.util.DateTimeUtils;
import com.robertsanek.util.Lists;

public class NoteEtl extends AnkiEtl<Note> {

  public static String FIELD_SEPARATOR = "\u001F";

  @Override
  public List<Note> transformRow(ResultSet row) throws SQLException {
    return Collections.singletonList(Note.NoteBuilder.aNote()
        .withId(row.getLong("id"))
        .withCreated_at(DateTimeUtils.toZonedDateTime(Instant.ofEpochMilli(row.getLong("id"))))
        .withModel_id(row.getLong("mid"))
        .withModified_at(DateTimeUtils.toZonedDateTime(Instant.ofEpochSecond(row.getLong("mod"))))
        .withTags(Lists.quoteListItemsAndJoinWithComma(Arrays.stream(row.getString("tags")
            .split(" "))
            .filter(s -> !s.equals(""))
            .toList(), FIELDS_LIMIT))
        .withFields(Lists.quoteListItemsAndJoinWithComma(Arrays.stream(row.getString("flds")
            .split(FIELD_SEPARATOR))
            .toList(), FIELDS_LIMIT))
        .withSortField(StringUtils.left(row.getString("sfld"), FIELDS_LIMIT))
        .build());
  }

  @Override
  public String getImportTableName() {
    return "notes";
  }
}
