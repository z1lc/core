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
    Review.ReviewBuilder reviewBuilder = Review.ReviewBuilder.aReview();
    reviewBuilder.withId(row.getLong("id"));
    reviewBuilder.withCreated_at(DateTimeUtils.toZonedDateTime(Instant.ofEpochMilli(row.getLong("id"))));
    reviewBuilder.withCard_id(row.getLong("cid"));
    reviewBuilder.withEase(row.getLong("ease"));
    reviewBuilder.withInterval(row.getLong("ivl"));
    reviewBuilder.withLast_interval(row.getLong("lastIvl"));
    reviewBuilder.withFactor(row.getLong("factor"));
    reviewBuilder.withTime_ms(row.getLong("time"));
    return Collections.singletonList(reviewBuilder.build());
  }

  @Override
  public String getImportTableName() {
    return "revlog";
  }
}
