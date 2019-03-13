package com.robertsanek.data.etl.local.sqllite.anki;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.google.common.collect.Lists;
import com.robertsanek.util.DateTimeUtils;
import com.robertsanek.util.Unchecked;

public class ModelEtl extends AnkiEtl<Model> {

  @Override
  public List<Model> transformRow(ResultSet row) throws SQLException {
    List<Model> allModels = Lists.newArrayList();
    String models = row.getString("models");
    try (JsonReader jsonReader = Unchecked
        .get(() -> Json.createReader(new ByteArrayInputStream(models.getBytes(StandardCharsets.UTF_8.name()))))) {
      JsonObject object = jsonReader.readObject();
      object.forEach((key, value) -> {
        Model.ModelBuilder builder = Model.ModelBuilder.aModel();
        JsonObject individualModel = object.getJsonObject(key);
        builder.withId(Long.valueOf(key));
        builder.withName(individualModel.getString("name"));
        builder.withCreated_at(DateTimeUtils.toZonedDateTime(Instant.ofEpochMilli(Long.valueOf(key))));
        builder.withModified_at(DateTimeUtils.toZonedDateTime(Instant.ofEpochSecond(individualModel.getInt("mod"))));
        builder.withDeck_id(individualModel.getJsonNumber("did").longValue());
        JsonArray fields = individualModel.getJsonArray("flds");
        builder.withFields(com.robertsanek.util.Lists.quoteListItemsAndJoinWithComma(fields.stream()
            .map(field -> field.asJsonObject().getString("name"))
            .collect(Collectors.toList()), FIELDS_LIMIT));
        JsonArray templates = individualModel.getJsonArray("tmpls");
        builder.withTemplates(com.robertsanek.util.Lists.quoteListItemsAndJoinWithComma(templates.stream()
            .map(field -> field.asJsonObject().getString("name"))
            .collect(Collectors.toList()), FIELDS_LIMIT));
        allModels.add(builder.build());
      });
    }
    return allModels;
  }

  @Override
  public String getImportTableName() {
    return "col";
  }
}
