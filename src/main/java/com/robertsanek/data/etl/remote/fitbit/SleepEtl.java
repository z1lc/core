package com.robertsanek.data.etl.remote.fitbit;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.FitbitApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.fitbit.json.Sleep;
import com.robertsanek.data.etl.remote.fitbit.json.SleepSummary;
import com.robertsanek.data.etl.remote.oauth.OAuth20Utils;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class SleepEtl extends Etl<Sleep> {

  private static final String FITBIT_ROOT =
      CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/fitbit/";
  private static final LocalDate FITBIT_START_DATE = LocalDate.of(2016, 8, 7);
  @Inject ObjectMapper mapper;
  @Inject SecretProvider secretProvider;
  private static final long MAXIMUM_DAYS_PER_REQUEST = 100;

  @Override
  public List<Sleep> getObjects() {
    final OAuth20Service service = new ServiceBuilder(secretProvider.getSecret(SecretType.FITBIT_CLIENT_ID))
        .apiSecret(secretProvider.getSecret(SecretType.FITBIT_CLIENT_SECRET))
        .defaultScope("activity nutrition heartrate location nutrition profile settings sleep social weight")
        .build(FitbitApi20.instance());
    OAuth20Utils oAuth20Utils = new OAuth20Utils(service, FITBIT_ROOT,
        "https://api.fitbit.com/1.2/user/-/sleep/date/2019-01-01.json");

    long daysBetweenDates = ChronoUnit.DAYS.between(FITBIT_START_DATE, LocalDate.now());
    return LongStream.range(0, (daysBetweenDates / MAXIMUM_DAYS_PER_REQUEST) + 1)
        .mapToObj(l -> {
          final Response response = oAuth20Utils.getSignedResponse(String.format(
              "https://api.fitbit.com/1.2/user/-/sleep/date/%s/%s.json",
              FITBIT_START_DATE.plusDays(l * 100).toString(),
              FITBIT_START_DATE.plusDays((l + 1) * 100).toString()),
              oAuth20Utils.maybeGetAccessToken().orElseThrow());
          return Unchecked.get(() -> mapper.readValue(response.getBody(), SleepSummary.class));
        })
        .flatMap(sleepSummary -> sleepSummary.getSleep() != null ? sleepSummary.getSleep().stream() : Stream.empty())
        .distinct()
        .collect(Collectors.toList());
  }

}
