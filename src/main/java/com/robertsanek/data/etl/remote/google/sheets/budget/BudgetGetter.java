package com.robertsanek.data.etl.remote.google.sheets.budget;

import static com.robertsanek.util.SecretType.MICRO_FINANCE_SPREADSHEET_ID;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.quartz.JobExecutionContext;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.robertsanek.data.etl.remote.google.sheets.SheetsConnector;
import com.robertsanek.process.QuartzJob;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.NotificationSender;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

/**
 * Connects to Google Sheets and returns budget information in type-safe entities.
 * Based on Google Sheets Quickstart, found at https://developers.google.com/sheets/api/quickstart/java
 */
public class BudgetGetter implements QuartzJob {

  private static final DateTimeFormatter SIMPLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("en"));
  private static final ImmutableList<LineItemType> EXPENSES_LINE_ITEM_ORDER = ImmutableList.of(
      LineItemType.FOOD,
      LineItemType.FOOD,
      LineItemType.FOOD,
      LineItemType.HOUSE,
      LineItemType.HOUSE,
      LineItemType.CLOTHING,
      LineItemType.TRANSPORTATION,
      LineItemType.PERSONAL,
      LineItemType.PERSONAL,
      LineItemType.CHARITY,
      LineItemType.HEALTH,
      LineItemType.TRAVEL,
      LineItemType.OTHER,
      LineItemType.OTHER
  );
  private static final ImmutableList<LineItemType> INCOME_LINE_ITEM_ORDER = ImmutableList.of(LineItemType.INCOME);
  private static final String EXPENSES_RANGE = "\uD83E\uDDFE Budget!A2:P10000";
  private static final String INCOME_RANGE = "\uD83E\uDDFE Budget!Q2:S10000";
  private static final String CSV_DELIMITER = "`";
  private static final Log log = Logs.getLog(BudgetGetter.class);

  @Inject NotificationSender notificationSender;
  @Inject SecretProvider secretProvider;

  private final AtomicLong counter = new AtomicLong(0);
  private final List<String> errors = new ArrayList<>();

  public List<AnnotatedItem> getData() {
    List<AnnotatedItem> allLineItems = getValues(INCOME_RANGE);
    allLineItems.addAll(getValues(EXPENSES_RANGE));
    allLineItems.sort(Comparator.comparing(AnnotatedItem::getDate));
    if (errors.size() > 0) {
      notificationSender.sendEmailDefault(
          String.format("%s errors in Budget ETL", errors.size()),
          String.join("\n", errors));
    }
    return allLineItems;
  }

  //I used to write this out within the getData method so that I'd have the information for the R shiny dashboard.
  //I no longer need this but leaving it in case I want to go back.
  private void writeToCsvForShinyDash(List<AnnotatedItem> allLineItems) throws IOException {
    String outName = CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/extracted.csv";
    try (PrintWriter writer = new PrintWriter(outName, StandardCharsets.UTF_8)) {
      writer.println(String.join(CSV_DELIMITER, "date", "value", "lineItemType", "comment", "isExpense"));
      allLineItems
          .forEach(ai -> {
            final List<String> row = ImmutableList.of(
                SIMPLE_DATE_FORMAT.format(ai.getDate()),
                ai.getValue().toString(),
                ai.getLineItemType().getFullName(),
                ai.getComment(),
                Boolean.toString(ai.getLineItemType().isExpense())
            );
            writer.println(String.join(CSV_DELIMITER, row));
          });
    }
  }

  private List<AnnotatedItem> getValues(String range) {
    String columnLettersOnly = range.substring(7).replaceAll("[^A-Z]", "");
    Preconditions.checkArgument(columnLettersOnly.length() == 2, "Expected column letters to be only 1 letter long.");
    final int expectedCells = columnLettersOnly.charAt(1) - columnLettersOnly.charAt(0) + 1;
    final List<List<Object>> spreadsheet =
        SheetsConnector.getSpreadsheetCells(secretProvider.getSecret(MICRO_FINANCE_SPREADSHEET_ID), range);
    if (spreadsheet == null || spreadsheet.size() == 0) {
      log.error("No data found.");
      return new ArrayList<>();
    }

    String requestType = range.equals(EXPENSES_RANGE) ? "expenses" : "income";
    return spreadsheet.stream()
        .filter(row -> row.size() > 0 && !row.get(0).toString().isEmpty())
        .flatMap(row -> {
          final List<AnnotatedItem> currentRowAnnotatedItems = new ArrayList<>();
          final LocalDate date = Unchecked.get(() -> LocalDate.parse(row.get(0).toString(), SIMPLE_DATE_FORMAT));
          if (row.size() == expectedCells) {
            final String comment = row.get(expectedCells - 1).toString();
            final List<String> split = Splitter.on(';').splitToList(comment);
            int currentSplit = 0;
            for (int i = 1; i < expectedCells - 1; i++) {
              final String cell = row.get(i).toString();
              if (!cell.isEmpty()) {
                final BigDecimal value = new BigDecimal(cell.replace("annotatedItem", ""))
                    .setScale(2, RoundingMode.CEILING);
                final LineItemType correspondingLineItemType = requestType.equals("expenses") ?
                    EXPENSES_LINE_ITEM_ORDER.get(i - 1) :
                    INCOME_LINE_ITEM_ORDER.get(i - 1);
                String itemComment = "";
                try {
                  itemComment = split.get(currentSplit++).trim();
                } catch (IndexOutOfBoundsException e) {
                  //will be caught by error handling below
                }
                final AnnotatedItem annotatedItem = new AnnotatedItem(
                    counter.getAndIncrement(),
                    ZonedDateTime.of(date, LocalTime.of(0, 0), ZoneId.of("America/Los_Angeles")),
                    value,
                    correspondingLineItemType,
                    itemComment);
                currentRowAnnotatedItems.add(annotatedItem);
              }
            }
            if (currentSplit != split.size()) {
              String descriptionExpenseMismatch = String.format(
                  "Number of descriptions does not match individual %s for date %s; descriptions: %d, expenses: %d",
                  requestType, SIMPLE_DATE_FORMAT.format(date), split.size(), currentSplit);
              log.error(descriptionExpenseMismatch);
              errors.add(descriptionExpenseMismatch);
            }
          } else {
            if (row.size() != 1) { //list size should always be 15 or 1: comment causes it to be 15
              String noDescription = String.format(
                  "No description for some %s on date %s.", requestType, SIMPLE_DATE_FORMAT.format(date));
              log.error(noDescription);
              errors.add(noDescription);
            }
          }
          return currentRowAnnotatedItems.stream();
        })
        .collect(Collectors.toList());
  }

  @Override
  public void exec(JobExecutionContext context) {
    Unchecked.run(() -> new BudgetGetter().getData());
  }

}