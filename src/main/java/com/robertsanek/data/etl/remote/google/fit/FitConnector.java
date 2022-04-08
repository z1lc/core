package com.robertsanek.data.etl.remote.google.fit;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.fitness.Fitness;
import com.google.api.services.fitness.FitnessScopes;
import com.google.inject.Inject;
import com.robertsanek.data.etl.remote.google.fit.jsonentities.FitResponse;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class FitConnector {

  private static final String APPLICATION_NAME = "R ETL";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final HttpTransport HTTP_TRANSPORT = Unchecked.get(GoogleNetHttpTransport::newTrustedTransport);
  private static final java.io.File DATA_STORE_DIR = new java.io.File(
      CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "in/etl/google_fit_credential");
  private static final FileDataStoreFactory DATA_STORE_FACTORY =
      Unchecked.get(() -> new FileDataStoreFactory(DATA_STORE_DIR));
  private static final Log log = Logs.getLog(FitConnector.class);
  private static final String URL =
      "https://www.googleapis.com/fitness/v1/users/me/dataSources/derived:com.google.blood_pressure:com.google.android.gms:merged/datasets/000000-%s";
  @Inject ObjectMapper objectMapper;

  private static Credential authorize() throws IOException {
    try (InputStream in = new FileInputStream(
        CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "in/etl/fitness_secret.json")) {
      final GoogleClientSecrets clientSecrets =
          GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in, UTF_8));
      log.info("Will look for existing credentials in %s.", DATA_STORE_DIR.getAbsolutePath());
      final GoogleAuthorizationCodeFlow flow =
          new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, FitnessScopes.all())
              .setDataStoreFactory(DATA_STORE_FACTORY)
              .setAccessType("offline")
              .build();
      return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
  }

  private static Fitness getFitnessService() {
    final Credential credential = Unchecked.get(FitConnector::authorize);
    return new Fitness.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  public FitResponse getBloodPressureReadings() {
    return Unchecked.get(() -> {
      String s = getFitnessService()
          .getRequestFactory()
          .buildGetRequest(new GenericUrl(
              String.format(URL, Instant.now().plus(Duration.ofDays(100)).getEpochSecond() * 1_000_000_000)))
          .execute()
          .parseAsString();
      return objectMapper.readValue(s, FitResponse.class);
    });
  }
}
