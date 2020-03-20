package com.robertsanek.data.etl.local.sqllite.anki;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import com.robertsanek.data.etl.SlowEtl;
import com.robertsanek.util.DateTimeUtils;

@SlowEtl
public class ReviewEtl extends AnkiEtl<Review> {

  @Override
  public List<Review> transformRow(ResultSet row) throws SQLException {
    return Collections.singletonList(Review.ReviewBuilder.aReview()
        .withId(row.getLong("id"))
        .withCreated_at(DateTimeUtils.toZonedDateTime(Instant.ofEpochMilli(row.getLong("id"))))
        .withCard_id(row.getLong("cid"))
        .withEase(row.getLong("ease"))
        .withInterval(row.getLong("ivl"))
        .withLast_interval(row.getLong("lastIvl"))
        .withFactor(row.getLong("factor"))
        .withTime_ms(row.getLong("time"))
        .build());
  }

  @Override
  public String getImportTableName() {
    return "revlog";
  }
}
