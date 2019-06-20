package com.robertsanek.data.etl.remote.fitbit;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.FitbitApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.fitbit.json.Sleep;
import com.robertsanek.data.etl.remote.fitbit.json.SleepSummary;
import com.robertsanek.data.etl.remote.oauth.OAuth20Utils;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class SleepEtl extends Etl<Sleep> {

  private static final String FITBIT_ROOT =
      CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/fitbit/";
  private static final LocalDate FITBIT_START_DATE = LocalDate.of(2016, 8, 7);
  private static final ObjectMapper mapper = CommonProvider.getObjectMapper();

  @Override
  public List<Sleep> getObjects() {
    final OAuth20Service service = new ServiceBuilder(CommonProvider.getSecret(SecretType.FITBIT_CLIENT_ID))
        .apiSecret(CommonProvider.getSecret(SecretType.FITBIT_CLIENT_SECRET))
        .defaultScope("activity nutrition heartrate location nutrition profile settings sleep social weight")
        .build(FitbitApi20.instance());
    OAuth20Utils oAuth20Utils = new OAuth20Utils(service, FITBIT_ROOT,
        "https://api.fitbit.com/1.2/user/-/sleep/date/2019-01-01.json");

    final Response response = oAuth20Utils.getSignedResponse(String.format(
        "https://api.fitbit.com/1.2/user/-/sleep/date/%s/%s.json",
        FITBIT_START_DATE.toString(),
        LocalDate.now().toString()),
        oAuth20Utils.maybeGetAccessToken().orElseThrow());
    SleepSummary sleepSummary = Unchecked.get(() -> mapper.readValue(response.getBody(), SleepSummary.class));
    return Optional.ofNullable(sleepSummary.getSleep())
        .orElse(Collections.emptyList());
  }

}
