package com.robertsanek.data.etl.remote.scrape.toodledo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.robertsanek.util.DateTimeUtils;

public class SimpleDateDeserializer extends JsonDeserializer<ZonedDateTime> {

  public static DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.of("en"));

  @Override
  public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return DateTimeUtils.toZonedDateTime(LocalDate.parse(p.getText(), d));
  }
}
