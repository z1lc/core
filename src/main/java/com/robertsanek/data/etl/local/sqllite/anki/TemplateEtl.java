package com.robertsanek.data.etl.local.sqllite.anki;

import java.sql.ResultSet;
import java.util.List;

import com.google.common.collect.Lists;

public class TemplateEtl extends AnkiEtl<Template> {

  @Override
  public List<Template> transformRow(ResultSet row) throws Exception {
    return Lists.newArrayList(Template.TemplateBuilder.aTemplate()
        .withId(row.getLong("ntid") + row.getLong("ord"))
        .withModel_id(row.getLong("ntid"))
        .withName(row.getString("name"))
        .withOrdinal(row.getLong("ord"))
        // deck id, front & back html have moved to binary blob 'config' column
        .build());
  }

  @Override
  public String getImportTableName() {
    return "templates";
  }
}
