package com.robertsanek.data.etl.local.sqllite.anki;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import com.google.common.collect.Lists;
import com.robertsanek.util.DateTimeUtils;

public class ModelEtl extends AnkiEtl<Model> {

  @Override
  public List<Model> transformRow(ResultSet row) throws SQLException {
    return Lists.newArrayList(Model.ModelBuilder.aModel()
        .withId(row.getLong("id"))
        .withName(row.getString("name"))
        .withCreated_at(DateTimeUtils.toZonedDateTime(Instant.ofEpochMilli(row.getLong("id"))))
        .withModified_at(DateTimeUtils.toZonedDateTime(Instant.ofEpochSecond(row.getInt("mtime_secs"))))
        // deck ID, fields, and templates have moved into binary blob 'config' column
        .build());
  }

  @Override
  public String getImportTableName() {
    return "notetypes";
  }
}
