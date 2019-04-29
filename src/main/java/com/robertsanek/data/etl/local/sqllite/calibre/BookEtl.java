package com.robertsanek.data.etl.local.sqllite.calibre;

import java.sql.ResultSet;
import java.util.List;

import com.google.common.collect.Lists;

public class BookEtl extends CalibreEtl<Book> {

  @Override
  public List<Book> transformRow(ResultSet row) throws Exception {
    return Lists.newArrayList(Book.BookBuilder.aBook()
        .withId(row.getLong("id"))
        .withTitle(row.getString("title"))
        .build());
  }

  @Override
  public String getImportTableName() {
    return "books";
  }
}
