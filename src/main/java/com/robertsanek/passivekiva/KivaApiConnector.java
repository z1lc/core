package com.robertsanek.passivekiva;

import static com.robertsanek.process.Command.KIVA;
import static com.robertsanek.process.SuccessType.ALERT;
import static com.robertsanek.util.SecretType.KIVA_SPREADSHEET_ID;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.robertsanek.Main;
import com.robertsanek.data.etl.remote.google.sheets.SheetsConnector;
import com.robertsanek.passivekiva.entities.Loan;
import com.robertsanek.passivekiva.entities.LoanListResponse;
import com.robertsanek.process.QuartzJob;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.LastAlertedProvider;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.NotificationSender;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

import j2html.tags.ContainerTag;

/* You can turn on/off searching for new loans by changing the flag in the PassiveKiva Summary spreadsheet. */
public class KivaApiConnector implements QuartzJob {

  private static final String RANGE = "Kiva: Summary!B1";
  private static final int TIMEOUT_SECONDS = 15;
  private static double MINIMUM_AVAILABLE_VALUE_TO_PAGE = 1000.0;
  private static int MAXIMUM_DURATION_IN_DAYS_TO_PAGE = 90;
  private static String MINIMUM_RISK_RATING = "4.5";
  private static Duration MINIMUM_TIME_BETWEEN_PAGES = Duration.ofHours(2);
  private static boolean CHECK_CURRENT_TIME_BEFORE_PAGING = false;
  private static int BEGIN_HOUR = 8;
  private static int END_HOUR = 22;
  private static int SEARCH_SIZE_LIMIT = 500; //max: 500; had to move down to 100 since executorservice was messing up
  private static int DETAILED_LOAN_LIMIT = 100; //max: 100
  private static int CONCURRENCY_LEVEL = 5;
  private static String APP_ID = "com.robertsanek.com.robertsanek.passivekiva";
  private static ObjectMapper mapper = CommonProvider.getObjectMapper();
  private static Log log = Logs.getLog(KivaApiConnector.class);
  private static String PUSH_TITLE = "PassiveKiva Alert";
  ZonedDateTime now = ZonedDateTime.now();
  @Inject LastAlertedProvider lastAlertedProvider;
  @Inject NotificationSender notificationSender;

  @Override
  public void exec(JobExecutionContext context) {
    if (!shouldRun()) {
      return;
    }
    Unchecked.get(this::doStuff);
  }

  public boolean doStuff() throws IOException, InterruptedException {
    final URI listAllAcceptablyRatedLoansURI = Unchecked.get(() -> new URIBuilder()
        .setScheme("http")
        .setHost("api.kivaws.org")
        .setPath("/v1/loans/search.json")
        .setParameter("status", "fundraising")
        .setParameter("page", "1")
        .setParameter("partner_risk_rating_min", MINIMUM_RISK_RATING)
        .setParameter("sort_by", "repayment_term")
        .setParameter("per_page", String.valueOf(SEARCH_SIZE_LIMIT))
        .setParameter("app_id", APP_ID)
        .build());

    final String allLoanBody = Request.Get(listAllAcceptablyRatedLoansURI)
        .execute()
        .returnContent()
        .asString();
    final LoanListResponse allLoans = mapper.readValue(allLoanBody, LoanListResponse.class);

    final ExecutorService executorService = Executors.newFixedThreadPool(CONCURRENCY_LEVEL);

    final URIBuilder individualLoanBaseURIBuilder = new URIBuilder()
        .setScheme("http")
        .setHost("api.kivaws.org")
        .setParameter("app_id", APP_ID);

    final List<Callable<List<Loan>>> callablesByBatchesOf100List = IntStream
        .rangeClosed(0, allLoans.getLoans().size() / DETAILED_LOAN_LIMIT)
        .mapToObj(batchNumber -> {
          final int firstNumberInBatch = batchNumber * DETAILED_LOAN_LIMIT;
          final int lastNumberInBatch = Math.min(allLoans.getLoans().size(), firstNumberInBatch + DETAILED_LOAN_LIMIT);
          if (firstNumberInBatch == lastNumberInBatch) {
            return (Callable<List<Loan>>) Lists::newArrayList;
          }
          List<Loan> these100Loans = allLoans.getLoans().subList(firstNumberInBatch, lastNumberInBatch);
          String these100LoansAsCSVString = these100Loans.stream()
              .map(loan -> loan.getId().toString())
              .collect(Collectors.joining(","));
          return Unchecked.get(() -> {
            URI these100LoansURL = individualLoanBaseURIBuilder
                .setPath("/v1/loans/" + these100LoansAsCSVString + ".json")
                .build();
            return (Callable<List<Loan>>) () -> {
              String these100LoansHTMLBody = Request.Get(these100LoansURL).execute().returnContent().asString();
              return mapper.readValue(these100LoansHTMLBody, LoanListResponse.class).getLoans();
            };
          });
        })
        .collect(Collectors.toList());

    List<Loan> allDetailedLoans =
        executorService.invokeAll(callablesByBatchesOf100List, TIMEOUT_SECONDS, TimeUnit.SECONDS).stream()
            .flatMap(individual100Loans -> {
              try {
                return individual100Loans.get().stream();
              } catch (CancellationException | InterruptedException | ExecutionException e) {
                log.info("Exception when trying to get future.", e);
                return Stream.empty();
              }
            })
            .collect(Collectors.toList());

    executorService.shutdown();

    allDetailedLoans.forEach(loan -> loan.setDuration(LoanDurationCalculator.getDuration(now, loan)));
    allDetailedLoans = allDetailedLoans.stream().sorted().collect(Collectors.toList());
    final ContainerTag containerTag = new HTMLOutputBuilder().buildHTML(allDetailedLoans);

    File loansTarget =
        new File(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/loans.html");
    log.info("Writing to %s", loansTarget.getAbsolutePath());
    try (PrintWriter writer = new PrintWriter(loansTarget, StandardCharsets.UTF_8)) {
      writer.print(containerTag.render());
    }

    final List<Loan> toPageLoans = allDetailedLoans.stream()
        .filter(loan -> loan.getDuration().orElse(100.0) <= MAXIMUM_DURATION_IN_DAYS_TO_PAGE)
        .collect(Collectors.toList());

    final double outstandingValueOfLoansToPage = toPageLoans.stream()
        .mapToDouble(loan -> loan.getUnfundedAmount().getAmount().doubleValue())
        .sum();

    if (outstandingValueOfLoansToPage >= MINIMUM_AVAILABLE_VALUE_TO_PAGE) {
      ZonedDateTime lastPageTime = lastAlertedProvider.getLast(KIVA, ALERT);
      if (lastPageTime.isBefore(now.minus(MINIMUM_TIME_BETWEEN_PAGES))) {
        Main.writeFile(KIVA, ALERT);
        return Unchecked.get(() -> {
          String message = String.format("<%sd duration loans with aggregate value of $%s available. Loan IDs: %s",
              MAXIMUM_DURATION_IN_DAYS_TO_PAGE, outstandingValueOfLoansToPage,
              toPageLoans.stream().map(loan -> loan.getId().toString()).collect(Collectors.toList()));
          return notificationSender.sendNotificationDefault(PUSH_TITLE, message);
        });
      } else {
        log.info("Will not page because time since last page (%s minutes) does not exceed minimum time " +
                "between pages (%s minutes)", ChronoUnit.MINUTES.between(lastPageTime, now),
            MINIMUM_TIME_BETWEEN_PAGES.getSeconds());
      }
    } else {
      log.info("Will not page because outstanding value of loans available ($%s) does not exceed configured " +
          "minimum necessary to page ($%s)", outstandingValueOfLoansToPage, MINIMUM_AVAILABLE_VALUE_TO_PAGE);
    }
    return false;
  }

  private boolean shouldRun() {
    ZonedDateTime startTime =
        ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), BEGIN_HOUR, 0, 0, 0, ZoneOffset.UTC);
    ZonedDateTime endTime =
        ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), END_HOUR, 0, 0, 0, ZoneOffset.UTC);
    return currentlyLooking() &&
        (!CHECK_CURRENT_TIME_BEFORE_PAGING || (now.isAfter(startTime) && now.isBefore(endTime)));
  }

  private boolean currentlyLooking() {
    try {
      List<List<Object>> spreadsheetCells =
          SheetsConnector.getSpreadsheetCells(CommonProvider.getSecret(KIVA_SPREADSHEET_ID), RANGE);
      return Boolean.valueOf(Iterables.getOnlyElement(Iterables.getOnlyElement(spreadsheetCells)).toString());
    } catch (Exception e) {
      log.error("Encountered exception when trying to determine if should be searching for Kiva loans. " +
          "Assuming we should not search.");
      log.error(e);
      return false;
    }
  }
}
