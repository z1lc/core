package com.robertsanek.data.etl.local.habitica;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateDeserializer extends JsonDeserializer<LocalDateTime> {

  public static DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.of("en"));

  private static DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.of("en"));

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
    try {
      return LocalDateTime.parse(p.getText(), d);
    } catch (Exception e) {
      return LocalDate.parse(p.getText(), oldFormat).atTime(0, 0);
    }
  }

}
