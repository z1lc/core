package com.robertsanek.data.etl.local.sqllite.anki;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.robertsanek.util.Unchecked;

public class FieldEtl extends AnkiEtl<Field> {

  @Override
  public List<Field> transformRow(ResultSet row) throws Exception {
    List<Field> allFields = new ArrayList<>();
    String models = row.getString("models");
    try (JsonReader jsonReader = Unchecked
        .get(() -> Json.createReader(new ByteArrayInputStream(models.getBytes(StandardCharsets.UTF_8.name()))))) {
      JsonObject object = jsonReader.readObject();
      object.forEach((key, value) -> {
        JsonObject individualModel = object.getJsonObject(key);
        JsonArray templates = individualModel.getJsonArray("flds");
        templates.forEach(template -> {
          JsonObject templateAsJson = template.asJsonObject();
          allFields.add(Field.FieldBuilder.aField()
              .withId(Long.valueOf(key + templateAsJson.getInt("ord")))
              .withModel_id(Long.valueOf(key))
              .withName(templateAsJson.getString("name"))
              .withFont_face(templateAsJson.getString("font"))
              .withFont_size((long) templateAsJson.getInt("size"))
              .withOrdinal((long) templateAsJson.getInt("ord"))
              .withSticky(templateAsJson.getBoolean("sticky"))
              .build());
        });
      });
    }
    return allFields;
  }

  @Override
  public String getImportTableName() {
    return "col";
  }
}
