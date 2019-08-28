package com.robertsanek.here;

import java.time.LocalTime;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.robertsanek.here.jsonentities.ForecastResponse;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;

public class HereConnector {

  @Inject ObjectMapper mapper;

  //https://developer.here.com/api-explorer/rest/auto_weather/weather-forecast-7days-astronomy
  public LocalTime getTodaysSundownTimeForSanFrancisco() {
    String apiResponse = Unchecked.get(() -> Request.Get(new URIBuilder()
        .setScheme("https")
        .setHost("weather.cit.api.here.com")
        .setPath("/weather/1.0/report.json")
        .setParameter("product", "forecast_astronomy")
        .setParameter("name", "San Francisco")
        .setParameter("app_id", CommonProvider.getSecret(SecretType.HERE_APP_ID))
        .setParameter("app_code", CommonProvider.getSecret(SecretType.HERE_APP_CODE))
        .build())
        .execute()
        .returnContent()
        .asString());
    ForecastResponse forecastResponse = Unchecked.get(() -> mapper.readValue(apiResponse, ForecastResponse.class));
    return forecastResponse.getAstronomy().getAstronomyInner().get(0).getSunset();
  }

}
