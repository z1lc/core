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

import org.apache.commons.lang3.StringUtils;

import com.robertsanek.util.Unchecked;

public class TemplateEtl extends AnkiEtl<Template> {

  @Override
  public List<Template> transformRow(ResultSet row) throws Exception {
    List<Template> allTemplates = new ArrayList<>();
    String models = row.getString("models");
    try (JsonReader jsonReader = Unchecked
        .get(() -> Json.createReader(new ByteArrayInputStream(models.getBytes(StandardCharsets.UTF_8.name()))))) {
      JsonObject object = jsonReader.readObject();
      object.forEach((key, value) -> {
        JsonObject individualModel = object.getJsonObject(key);
        JsonArray templates = individualModel.getJsonArray("tmpls");
        templates.forEach(template -> {
          JsonObject templateAsJson = template.asJsonObject();
          allTemplates.add(Template.TemplateBuilder.aTemplate()
              .withId(Long.valueOf(key + templateAsJson.getInt("ord")))
              .withDeck_id(
                  templateAsJson.get("did").toString().equals("null") ? null : (long) templateAsJson.getInt("did"))
              .withModel_id(Long.valueOf(key))
              .withName(templateAsJson.getString("name"))
              .withFront_html(StringUtils.left(templateAsJson.getString("qfmt"), FIELDS_LIMIT))
              .withBack_html(StringUtils.left(templateAsJson.getString("afmt"), FIELDS_LIMIT))
              .withOrdinal((long) templateAsJson.getInt("ord"))
              .build());
        });
      });
    }
    return allTemplates;
  }

  @Override
  public String getImportTableName() {
    return "col";
  }
}
