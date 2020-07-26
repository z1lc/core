package com.robertsanek.data.etl.local.sqllite.anki;

import java.sql.ResultSet;
import java.util.List;

import com.google.common.collect.Lists;

public class FieldEtl extends AnkiEtl<Field> {

  @Override
  public List<Field> transformRow(ResultSet row) throws Exception {
    return Lists.newArrayList(Field.FieldBuilder.aField()
        .withId(row.getLong("ntid") + row.getInt("ord"))
        .withModel_id(row.getLong("ntid"))
        .withName(row.getString("name"))
        .withOrdinal(row.getLong("ord"))
        // could parse 'config' column (binary blob) here, but probably not worth it
        .build());
  }

  @Override
  public String getImportTableName() {
    return "fields";
  }
}
