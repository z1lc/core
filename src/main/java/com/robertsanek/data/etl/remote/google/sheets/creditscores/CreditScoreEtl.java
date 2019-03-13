package com.robertsanek.data.etl.remote.google.sheets.creditscores;

import static com.robertsanek.util.SecretType.FINANCE_SPREADSHEET_ID;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.google.sheets.SheetsConnector;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Unchecked;

public class CreditScoreEtl extends Etl<CreditScore> {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy");
  private static final String RANGE = "Credit Score!A2:H10000";

  @Override
  public List<CreditScore> getObjects() throws Exception {
    List<List<Object>> spreadsheetCells =
        SheetsConnector.getSpreadsheetCells(CommonProvider.getSecret(FINANCE_SPREADSHEET_ID), RANGE);
    return spreadsheetCells.stream()
        .map(row -> {
          final ZonedDateTime date =
              ZonedDateTime.of(Unchecked.get(() -> LocalDate.parse(row.get(0).toString(), DATE_FORMAT)),
                  LocalTime.of(0, 0), ZoneId.of("America/Los_Angeles"));
          return CreditScore.CreditScoreBuilder.aCreditScore()
              .withDate(date)
              .withCreditKarma(SheetsConnector.getOrNullBigDecimal(row, 1))
              .withCreditSesame(SheetsConnector.getOrNullBigDecimal(row, 2))
              .withCreditViewDashboard(SheetsConnector.getOrNullBigDecimal(row, 3))
              .withMint(SheetsConnector.getOrNullBigDecimal(row, 4))
              .withQuizzle(SheetsConnector.getOrNullBigDecimal(row, 5))
              .withBankRate(SheetsConnector.getOrNullBigDecimal(row, 6))
              .withCiti(SheetsConnector.getOrNullBigDecimal(row, 7))
              .build();
        })
        .collect(Collectors.toList());
  }
}
