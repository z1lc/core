package com.robertsanek.data.etl.local.sqllite.anki;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DeckEtl extends AnkiEtl<Deck> {

  @Override
  public List<Deck> transformRow(ResultSet row) throws Exception {
    List<Deck> allDecks = new ArrayList<>();
    allDecks.add(Deck.DeckBuilder.aDeck()
        .withId(row.getLong("id"))
        .withName(row.getString("name"))
        .build());
    return allDecks;
  }

  @Override
  public String getImportTableName() {
    return "decks";
  }
}
