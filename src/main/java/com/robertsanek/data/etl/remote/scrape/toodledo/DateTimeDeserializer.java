package com.robertsanek.data.etl.remote.scrape.toodledo;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.robertsanek.util.DateTimeUtils;

public class DateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

  @Override
  public ZonedDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
    return DateTimeUtils.toZonedDateTime(
        Instant.ofEpochSecond(Long.valueOf(p.getText()))
            .atZone(ZoneId.of("GMT"))
            .toLocalDateTime());
  }

}