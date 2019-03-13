package com.robertsanek.data.etl.local.habitica;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class HabiticaHistorySaver {

  public static DateTimeFormatter ourFormatter = DateTimeFormatter.ofPattern("uuuuMMdd");

  public List<HistoryCsv> getHistoryAndSaveAsCsv() throws IOException {
    CsvMapper mapper = new CsvMapper();
    CsvSchema schema = mapper.schemaFor(HistoryCsv.class).withHeader();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String responseJson = HabiticaUtils.genericGetJson("export/history.csv", Collections.emptyList());
    MappingIterator<HistoryCsv> objectMappingIterator =
        mapper.readerFor(HistoryCsv.class).with(schema).readValues(responseJson);
    List<HistoryCsv> histories = objectMappingIterator.readAll();
    File habiticaTarget;
    String dateTimeForFile = LocalDateTime.now().format(ourFormatter);
    habiticaTarget = new File(String
        .format(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/habitica/habitica-%s.csv",
            dateTimeForFile));
    mapper.writerFor(HistoryCsv.class).with(schema).writeValues(habiticaTarget).writeAll(histories);
    return histories;
  }

}
