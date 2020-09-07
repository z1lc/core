package com.robertsanek.data.etl.remote.fitbit;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.apache.commons.lang3.tuple.Triple;

import com.github.scribejava.apis.FitbitApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.oauth.OAuth20Utils;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class ActivityEtl extends Etl<Activity> {

  private static final String FITBIT_ROOT =
      CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/fitbit/";
  private static final LocalDate FITBIT_START_DATE = LocalDate.of(2016, 8, 7);
  private static final long MAXIMUM_DAYS_PER_REQUEST = 100;

  @Inject SecretProvider secretProvider;

  @Override
  public List<Activity> getObjects() {
    final OAuth20Service service = new ServiceBuilder(secretProvider.getSecret(SecretType.FITBIT_CLIENT_ID))
        .apiSecret(secretProvider.getSecret(SecretType.FITBIT_CLIENT_SECRET))
        .defaultScope("activity nutrition heartrate location nutrition profile settings sleep social weight")
        .build(FitbitApi20.instance());
    OAuth20Utils oAuth20Utils = new OAuth20Utils(service, FITBIT_ROOT,
        "https://api.fitbit.com/1/user/-/activities/date/2019-01-01.json");

    long daysBetweenDates = ChronoUnit.DAYS.between(FITBIT_START_DATE, LocalDate.now());
    return LongStream.range(0, (daysBetweenDates / MAXIMUM_DAYS_PER_REQUEST) + 1)
        .mapToObj(l -> {
          final LocalDate thisDate = FITBIT_START_DATE.plusDays(l * 100);
          final JsonArray minutesLightlyActive = getResponse(oAuth20Utils, thisDate, "minutesLightlyActive");
          final JsonArray minutesFairlyActive = getResponse(oAuth20Utils, thisDate, "minutesFairlyActive");
          final JsonArray minutesVeryActive = getResponse(oAuth20Utils, thisDate, "minutesVeryActive");
          return Triple.of(minutesLightlyActive, minutesFairlyActive, minutesVeryActive);
        })
        .flatMap(activeArraysTriple -> {
          if (activeArraysTriple.getLeft().size() != activeArraysTriple.getMiddle().size() ||
              activeArraysTriple.getMiddle().size() != activeArraysTriple.getRight().size()) {
            throw new RuntimeException("arrays not of same length");
          }
          return IntStream.range(0, activeArraysTriple.getLeft().size())
              .mapToObj(i -> {
                LocalDate thisDate = LocalDate
                    .parse(activeArraysTriple.getLeft().get(i).getAsJsonObject().get("dateTime").getAsString());
                return Activity.ActivityBuilder.anActivity()
                    .withDate(thisDate)
                    .withLightlyActiveMinutes(getMinutesFromObj(activeArraysTriple.getLeft().get(i)))
                    .withFairlyActiveMinutes(getMinutesFromObj(activeArraysTriple.getMiddle().get(i)))
                    .withVeryActiveMinutes(getMinutesFromObj(activeArraysTriple.getRight().get(i)))
                    .build();
              });
        })
        .filter(activity -> activity.getDate().isBefore(LocalDate.now()))
        .distinct()
        .collect(Collectors.toList());
  }

  private int getMinutesFromObj(JsonElement e) {
    return Integer.parseInt(e.getAsJsonObject().get("value").getAsString());
  }

  private JsonArray getResponse(OAuth20Utils oAuth20Utils, LocalDate date, String measure) {
    return new JsonParser().parse(Unchecked.get(() -> oAuth20Utils.getSignedResponse(String.format(
        "https://api.fitbit.com/1/user/-/activities/tracker/%s/date/%s/%s.json",
        measure,
        date.toString(),
        date.plusDays(100).toString()),
        oAuth20Utils.maybeGetAccessToken().orElseThrow()).getBody()))
        .getAsJsonObject()
        .getAsJsonArray(String.format("activities-tracker-%s", measure));

  }

}
