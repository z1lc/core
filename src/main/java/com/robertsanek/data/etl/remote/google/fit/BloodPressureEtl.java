package com.robertsanek.data.etl.remote.google.fit;

import static com.robertsanek.util.SecretType.HEALTH_SPREADSHEET_ID;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.google.sheets.SheetsConnector;
import com.robertsanek.data.etl.remote.google.sheets.health.HealthEtl;
import com.robertsanek.util.DateTimeUtils;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.Unchecked;

public class BloodPressureEtl extends Etl<BloodPressureReading> {

  @VisibleForTesting static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", new Locale("en"));
  private static final AtomicLong ID_ISSUER = new AtomicLong(1);
  private static final String RANGE = "Weight & HR!A2:G10000";

  @Inject SecretProvider secretProvider;
  @Inject FitConnector fitConnector;

  @Override
  public List<BloodPressureReading> getObjects() {
    List<List<Object>> spreadsheetCells =
        SheetsConnector.getSpreadsheetCells(secretProvider.getSecret(HEALTH_SPREADSHEET_ID), RANGE);
    Stream<BloodPressureReading> manual = spreadsheetCells.stream()
        .map(row -> {
          String rowDate = row.get(0).toString();
          // really weird &nbsp; -type character getting added by Google Sheets between the date and time, we just by-hand replace it here with a regular space.
          final LocalDate date = Unchecked.get(() -> LocalDateTime.parse(rowDate.replace(String.valueOf(rowDate.charAt(10)), " "), DATE_FORMAT)).toLocalDate();
          return BloodPressureReading.BloodPressureReadingBuilder.aBloodPressureReading()
              .withId(ID_ISSUER.getAndIncrement())
              .withDate(DateTimeUtils.toZonedDateTime(date))
              .withSystolic(HealthEtl.maybeGet(row, 3).map(Long::parseLong).orElse(0L))
              .withDiastolic(HealthEtl.maybeGet(row, 4).map(Long::parseLong).orElse(0L))
              .build();
        })
        .filter(bpr -> bpr.getSystolic() > 0 || bpr.getDiastolic() > 0);
    Stream<BloodPressureReading> automated =
        fitConnector.getBloodPressureReadings().getPoint().stream()
            .map(point -> BloodPressureReading.BloodPressureReadingBuilder.aBloodPressureReading()
                .withId(ID_ISSUER.getAndIncrement())
                .withDate(DateTimeUtils.toZonedDateTime(
                    Instant.ofEpochMilli(
                        Long.parseLong(point.getStartTimeNanos()) / 1_000_000)))
                .withSystolic(point.getValue().get(0).getFpVal().longValue())
                .withDiastolic(point.getValue().get(1).getFpVal().longValue())
                .build());
    return Stream.concat(manual, automated).toList();
  }
}
