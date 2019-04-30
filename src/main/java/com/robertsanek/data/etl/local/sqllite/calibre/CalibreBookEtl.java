package com.robertsanek.data.etl.local.sqllite.calibre;

import java.sql.ResultSet;
import java.util.List;

import com.google.common.collect.Lists;

public class CalibreBookEtl extends CalibreEtl<CalibreBook> {

  @Override
  public List<CalibreBook> transformRow(ResultSet row) throws Exception {
    return Lists.newArrayList(CalibreBook.BookBuilder.aBook()
        .withId(row.getLong("id"))
        .withTitle(row.getString("title"))
        .build());
  }

  @Override
  public String getImportTableName() {
    return "books";
  }
}
