package com.robertsanek.data.etl.remote.google.sheets.clothing;

import static com.robertsanek.util.SecretType.CLOTHING_SPREADSHEET_ID;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.google.sheets.SheetsConnector;
import com.robertsanek.util.CommonProvider;

public class ClothingEtl extends Etl<ClothingRating> {

  private static final String RANGE = "Data!A2:G10000";
  private static final AtomicLong ID_ISSUER = new AtomicLong(1);

  private static boolean convertYesNoStringToBoolean(String yesNoOrNull) {
    return yesNoOrNull != null && (yesNoOrNull.equals("Yes") || yesNoOrNull.startsWith("Yes "));
  }

  @Override
  public List<ClothingRating> getObjects() {
    List<List<Object>> spreadsheetCells =
        SheetsConnector.getSpreadsheetCells(CommonProvider.getSecret(CLOTHING_SPREADSHEET_ID), RANGE);
    return spreadsheetCells.stream()
        .map(row -> ClothingRating.ClothingRatingBuilder.aClothingRating()
            .withId(ID_ISSUER.getAndIncrement())
            .withGeneralClass(SheetsConnector.getOrNull(row, 0))
            .withSubClass(SheetsConnector.getOrNull(row, 1))
            .withDescriptionOfStock(SheetsConnector.getOrNull(row, 2))
            .withAllTagsRemoved(convertYesNoStringToBoolean(SheetsConnector.getOrNull(row, 3)))
            .withTagsComment(SheetsConnector.getOrNull(row, 3))
            .withAllItemsFitCorrectly(convertYesNoStringToBoolean(SheetsConnector.getOrNull(row, 4)))
            .withFitComment(SheetsConnector.getOrNull(row, 4))
            .withAllItemsGreat(convertYesNoStringToBoolean(SheetsConnector.getOrNull(row, 5)))
            .withGreatComment(SheetsConnector.getOrNull(row, 5))
            .withEnoughTotalItems(convertYesNoStringToBoolean(SheetsConnector.getOrNull(row, 6)))
            .withTotalComment(SheetsConnector.getOrNull(row, 6))
            .build())
        .collect(Collectors.toList());
  }

}
