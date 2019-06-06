package com.robertsanek.data.etl.remote.google.sheets;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.common.collect.ImmutableList;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class SheetsConnector {

  private static final String APPLICATION_NAME = "R ETL";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final HttpTransport HTTP_TRANSPORT;
  private static final ImmutableList<String> SCOPES = ImmutableList.of(SheetsScopes.SPREADSHEETS_READONLY);
  private static final FileDataStoreFactory DATA_STORE_FACTORY;
  private static final java.io.File DATA_STORE_DIR = new java.io.File(
      System.getProperty("user.home"), ".credentials/com.robertsanek.sheets.googleapis.com-java-quickstart");
  private static Log log = Logs.getLog(SheetsConnector.class);

  private static Credential authorize() throws IOException {
    try (InputStream in = new FileInputStream(
        CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "in/etl/client_secret.json")) {
      final GoogleClientSecrets clientSecrets =
          GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in, UTF_8));
      log.info("Will look for existing credentials in %s.", DATA_STORE_DIR.getAbsolutePath());
      final GoogleAuthorizationCodeFlow flow =
          new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
              .setDataStoreFactory(DATA_STORE_FACTORY)
              .setAccessType("offline")
              .build();
      return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
  }

  private static Sheets getSheetsService() {
    final Credential credential = Unchecked.get(SheetsConnector::authorize);
    return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  public static List<List<Object>> getSpreadsheetCells(String spreadsheetId, String range) {
    return Unchecked.get(() -> getSheetsService().spreadsheets().values()
        .get(spreadsheetId, range)
        .setDateTimeRenderOption("FORMATTED_STRING")
        .setValueRenderOption("UNFORMATTED_VALUE")
        .setMajorDimension("ROWS")
        .execute()
        .getValues());
  }

  public static String getOrNull(List<Object> row, int i) {
    if (row.size() > i) {
      if (row.get(i).toString().isEmpty()) {
        return null;
      }
      return row.get(i).toString();
    }
    return null;
  }

  public static BigDecimal getOrNullBigDecimal(List<Object> row, int i) {
    if (row.size() > i) {
      if (row.get(i).toString().isEmpty()) {
        return null;
      }
      return new BigDecimal(row.get(i).toString()).setScale(2, RoundingMode.CEILING);
    }
    return null;
  }

  static {
    HTTP_TRANSPORT = Unchecked.get(GoogleNetHttpTransport::newTrustedTransport);
    DATA_STORE_FACTORY = Unchecked.get(() -> new FileDataStoreFactory(DATA_STORE_DIR));
  }

}
