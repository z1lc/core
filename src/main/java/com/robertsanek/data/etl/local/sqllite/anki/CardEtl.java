package com.robertsanek.data.etl.local.sqllite.anki;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import com.robertsanek.util.DateTimeUtils;

public class CardEtl extends AnkiEtl<Card> {

  @Override
  public List<Card> transformRow(ResultSet row) throws SQLException {
    long id = row.getLong("id");
    return Collections.singletonList(Card.CardBuilder.aCard()
        .withId(id)
        .withCreated_at(DateTimeUtils.toZonedDateTime(Instant.ofEpochMilli(id)))
        .withNote_id(row.getLong("nid"))
        .withDeck_id(row.getLong("did"))
        .withTemplate_ordinal(row.getLong("ord"))
        .withLast_modified_at(DateTimeUtils.toZonedDateTime(Instant.ofEpochMilli(row.getLong("mod"))))
        .withType(Card.Type.fromValue(id, row.getInt("type")))
        .withQueue(Card.Queue.fromValue(id, row.getInt("queue")))
        .withInterval_days(row.getLong("ivl"))
        .withFactor(row.getLong("factor"))
        .withNumber_reviews(row.getLong("reps"))
        .withNumber_lapses(row.getLong("lapses"))
        .build());
  }

  @Override
  public String getImportTableName() {
    return "cards";
  }

}
