package com.robertsanek.data.etl.local.sqllite.anki;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.robertsanek.util.Unchecked;

public class DeckEtl extends AnkiEtl<Deck> {

  @Override
  public List<Deck> transformRow(ResultSet row) throws Exception {
    List<Deck> allDecks = new ArrayList<>();
    String models = row.getString("decks");
    try (JsonReader jsonReader = Unchecked
        .get(() -> Json.createReader(new ByteArrayInputStream(models.getBytes(StandardCharsets.UTF_8.name()))))) {
      JsonObject object = jsonReader.readObject();
      object.forEach((key, value) -> {
        JsonObject individualDeck = object.getJsonObject(key);
        allDecks.add(Deck.DeckBuilder.aDeck()
            .withId(Long.valueOf(key))
            .withOption_group_id((long) individualDeck.getInt("conf"))
            .withName(individualDeck.getString("name"))
            .withCollapsed(individualDeck.getBoolean("collapsed"))
            .withDescription(individualDeck.getString("desc"))
            .build());
      });
    }
    return allDecks;
  }

  @Override
  public String getImportTableName() {
    return "col";
  }
}
