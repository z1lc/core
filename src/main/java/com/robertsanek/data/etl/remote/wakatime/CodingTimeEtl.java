package com.robertsanek.data.etl.remote.wakatime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.fluent.Request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.wakatime.jsonentities.CodingInfoForDay;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.SecretType;

public class CodingTimeEtl extends Etl<CodingTime> {

  @Inject ObjectMapper mapper;
  private final AtomicLong counter = new AtomicLong(1);

  @Override
  public List<CodingTime> getObjects() throws Exception {
    LocalDate today = LocalDate.now();
    String URL = String.format("https://wakatime.com/api/v1/users/%s/summaries/?api_key=%s&start=%s&end=%s",
        CommonProvider.getSecret(SecretType.WAKATIME_USER_ID), CommonProvider.getSecret(SecretType.WAKATIME_API_KEY),
        today.minusDays(13), today);
    String jsonReturned = Request.Get(URL).execute().returnContent().asString();
    String dataArrAsString = new JsonParser().parse(jsonReturned).getAsJsonObject().getAsJsonArray("data").toString();
    return Arrays.stream(mapper.readValue(dataArrAsString, CodingInfoForDay[].class))
        .flatMap(codingInfoForDay -> codingInfoForDay.getLanguages().stream()
            .map(lang -> Pair.of(lang, codingInfoForDay.getRange())))
        .map(pair -> CodingTime.CodingTimeBuilder.aCodingTime()
            .withId(counter.getAndIncrement())
            .withDate(ZonedDateTime.of(
                pair.getRight().getDate(), LocalTime.of(0, 0, 0), ZoneId.of(pair.getRight().getTimezone())))
            .withLanguage(pair.getLeft().getName())
            .withSeconds(pair.getLeft().getTotalSeconds())
            .build())
        .filter(codingTime -> codingTime.getSeconds() > 0)
        .collect(Collectors.toList());
  }
}
