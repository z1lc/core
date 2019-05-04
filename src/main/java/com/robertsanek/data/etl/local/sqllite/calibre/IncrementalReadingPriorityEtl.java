package com.robertsanek.data.etl.local.sqllite.calibre;

import java.sql.ResultSet;
import java.util.List;

import com.google.common.collect.Lists;

public class IncrementalReadingPriorityEtl extends CalibreEtl<IncrementalReadingPriority> {

  @Override
  public List<IncrementalReadingPriority> transformRow(ResultSet row) throws Exception {
    return Lists.newArrayList(IncrementalReadingPriority.IncrementalReadingPriorityBuilder.anIncrementalReadingPriority()
        .withId(row.getLong("id"))
        .withBookId(row.getLong("book"))
        .withValue(row.getLong("value"))
        .build()
    );
  }

  @Override
  public String getImportTableName() {
    return "custom_column_2";
  }
}
