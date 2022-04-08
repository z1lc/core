package com.robertsanek.data.etl.remote.google.analytics;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.*;
import com.google.common.collect.Lists;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class HelloAnalytics {

  private static final String VIEW_ID = "59901365";
  private static final java.io.File DATA_STORE_DIR = new java.io.File(
      CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "in/etl/google_analytics_credential");

  private static final String APPLICATION_NAME = "Hello Analytics Reporting";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static AtomicLong ID_ISSUER = new AtomicLong(1);
  private static final Log log = Logs.getLog(HelloAnalytics.class);

  public static void main(String[] args) {
    try {
      AnalyticsReporting service = initializeAnalyticsReporting();

      GetReportsResponse response = getReport(service);
      getResponse(response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static List<PageView> getPageViews() {
    try {
      AnalyticsReporting service = initializeAnalyticsReporting();

      GetReportsResponse response = getReport(service);
      return getResponse(response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {
    NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
    InputStream in = new FileInputStream(
        CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "in/etl/analytics_secret.json");
    final GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in, UTF_8));
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
        .Builder(httpTransport, JSON_FACTORY, clientSecrets,
        AnalyticsReportingScopes.all()).setDataStoreFactory(dataStoreFactory)
        .build();
    Credential credential = new AuthorizationCodeInstalledApp(flow,
        new LocalServerReceiver()).authorize("user");
    return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME).build();
  }

  private static GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
    DateRange dateRange = new DateRange();
    dateRange.setStartDate("30DaysAgo");
    dateRange.setEndDate("today");
    Metric sessions = new Metric()
        .setExpression("ga:pageviews")
        .setAlias("pageviews");
    Dimension browser = new Dimension()
        .setName("ga:date");
    ReportRequest request = new ReportRequest()
        .setViewId(VIEW_ID)
        .setDateRanges(Collections.singletonList(dateRange))
        .setDimensions(Collections.singletonList(browser))
        .setMetrics(Collections.singletonList(sessions));

    ArrayList<ReportRequest> requests = new ArrayList<>();
    requests.add(request);
    GetReportsRequest getReport = new GetReportsRequest()
        .setReportRequests(requests);
    return service.reports().batchGet(getReport).execute();
  }

  private static List<PageView> getResponse(GetReportsResponse response) {

    List<PageView> toReturn = Lists.newArrayList();
    for (Report report : response.getReports()) {
      ColumnHeader header = report.getColumnHeader();
      List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
      List<ReportRow> rows = report.getData().getRows();

      if (rows == null) {
        log.error("No rows found for view %s!", VIEW_ID);
        return Lists.newArrayList();
      }

      for (ReportRow row : rows) {
        List<String> dimensions = row.getDimensions();
        List<DateRangeValues> metrics = row.getMetrics();

        for (DateRangeValues values : metrics) {
          for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
            toReturn.add(
                PageView.PageViewBuilder.aPageView()
                    .withId(ID_ISSUER.getAndIncrement())
                    .withDate(LocalDate.parse(dimensions.get(0), DateTimeFormatter.ofPattern("yyyyMMdd", new Locale("en"))))
                    .withWebsite("www.robertsanek.com")
                    .withPageViews(Long.parseLong(values.getValues().get(k)))
                    .build()
            );
          }
        }
      }
    }
    return toReturn;
  }
}
