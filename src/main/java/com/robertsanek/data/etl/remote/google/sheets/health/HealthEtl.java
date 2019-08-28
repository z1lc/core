package com.robertsanek.data.etl.remote.google.sheets.health;

import static com.robertsanek.util.SecretType.HEALTH_SPREADSHEET_ID;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.google.sheets.SheetsConnector;
import com.robertsanek.util.DateTimeUtils;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.Unchecked;

public class HealthEtl extends Etl<Health> {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy");
  private static final String RANGE = "Daily Log!A2:H10000";
  @Inject SecretProvider secretProvider;

  @Override
  public List<Health> getObjects() {
    List<List<Object>> spreadsheetCells =
        SheetsConnector.getSpreadsheetCells(secretProvider.getSecret(HEALTH_SPREADSHEET_ID), RANGE);
    return spreadsheetCells.stream()
        .map(row -> {
          final LocalDate date = Unchecked.get(() -> LocalDate.parse(row.get(0).toString(), DATE_FORMAT));
          Optional<BigDecimal> cardio = maybeGet(row, 1)
              .map(this::fromStringToBigDecimal);
          Optional<BigDecimal> lifting = maybeGet(row, 2)
              .map(this::fromStringToBigDecimal);
          BigDecimal total = maybeGet(row, 3)
              .map(this::fromStringToBigDecimal)
              .orElse(getTotal(cardio, lifting));
          String note = maybeGet(row, 4).orElse("");
          BigDecimal alcohol = maybeGet(row, 6)
              .map(this::fromStringToBigDecimal)
              .orElse(new BigDecimal(0));
          String drugs = maybeGet(row, 7).orElse("");
          return Health.HealthBuilder.aHealth()
              .withDate(DateTimeUtils.toZonedDateTime(date))
              .withCardio(cardio.orElse(null))
              .withLifting(lifting.orElse(null))
              .withTotal(total)
              .withAlcohol(alcohol)
              .withDrugs(drugs)
              .withComment(note)
              .build();
        })
        .filter(health -> health.getDate().isBefore(ZonedDateTime.now()))
        .collect(Collectors.toList());
  }

  private BigDecimal fromStringToBigDecimal(String str) {
    if (!str.isEmpty()) {
      return new BigDecimal(str).setScale(2, RoundingMode.CEILING);
    }
    return null;
  }

  @VisibleForTesting
  BigDecimal getTotal(Optional<BigDecimal> maybeBigDecimal1, Optional<BigDecimal> maybeBigDecimal2) {
    BigDecimal total = new BigDecimal(0);
    return total
        .add(maybeBigDecimal1.orElse(new BigDecimal(0)))
        .add(maybeBigDecimal2.orElse(new BigDecimal(0)));
  }

  private Optional<String> maybeGet(List<Object> row, int index) {
    return row.size() > index ? Optional.ofNullable(row.get(index).toString()) : Optional.empty();
  }

}
