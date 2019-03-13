package com.robertsanek.data.etl.local.sqllite.anki;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.google.common.collect.Lists;
import com.robertsanek.util.Unchecked;

public class TemplateEtl extends AnkiEtl<Template> {

  @Override
  public List<Template> transformRow(ResultSet row) throws Exception {
    List<Template> allTemplates = Lists.newArrayList();
    String models = row.getString("models");
    try (JsonReader jsonReader = Unchecked
        .get(() -> Json.createReader(new ByteArrayInputStream(models.getBytes(StandardCharsets.UTF_8.name()))))) {
      JsonObject object = jsonReader.readObject();
      object.forEach((key, value) -> {
        JsonObject individualModel = object.getJsonObject(key);
        JsonArray templates = individualModel.getJsonArray("tmpls");
        templates.forEach(template -> {
          Template.TemplateBuilder builder = Template.TemplateBuilder.aTemplate();
          JsonObject templateAsJson = template.asJsonObject();
          builder.withId(Long.valueOf(key + templateAsJson.getInt("ord")));
          builder.withDeck_id(
              templateAsJson.get("did").toString().equals("null") ? null : (long) templateAsJson.getInt("did"));
          builder.withModel_id(Long.valueOf(key));
          builder.withName(templateAsJson.getString("name"));
          builder.withFront_html(templateAsJson.getString("qfmt"));
          builder.withBack_html(templateAsJson.getString("afmt"));
          builder.withOrdinal((long) templateAsJson.getInt("ord"));
          allTemplates.add(builder.build());
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
