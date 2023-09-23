package com.robertsanek.data.etl.remote.rescuetime;

import static com.robertsanek.util.SecretType.RESCUETIME_API_KEY;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.Unchecked;

abstract class RescueTimeEtl<T> extends Etl<T> {

  private static final Log log = Logs.getLog(RescueTimeEtl.class);
  @VisibleForTesting int FROM_YEAR = 2009; //first data in RescueTime is from 2009
  @VisibleForTesting int TO_YEAR = LocalDate.now().getYear();
  DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.of("en"));

  @Inject SecretProvider secretProvider;

  public <O> List<O> genericGet(String taxonomy, Function<CSVRecord, O> csvToObjectFunction) {
    Set<O> allRecords = Collections.synchronizedSet(new HashSet<>());
    IntStream.rangeClosed(FROM_YEAR, TO_YEAR).parallel()
        .forEach(currentYear -> {
          IntStream.rangeClosed(1, 12).parallel()
              .forEach(currentMonth -> {
                final URI efficiencyUri;
                LocalDate startOfMonth = LocalDate.of(currentYear, currentMonth, 1);
                try {
                  efficiencyUri = Unchecked.get(() -> new URIBuilder()
                      .setScheme("https")
                      .setHost("www.rescuetime.com")
                      .setPath("/anapi/data")
                      .setParameter("key", getAPIKey())
                      .setParameter("rs", "day")
                      .setParameter("by", "interval")
                      .setParameter("format", "csv")
                      .setParameter("ty", taxonomy)
                      .setParameter("rb", DATE_FORMAT.format(startOfMonth))
                      .setParameter("re", DATE_FORMAT.format(startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth())))
                      .build());
                  String csv = get(efficiencyUri);
                  CSVParser csvRecords = CSVParser.parse(csv, CSVFormat.DEFAULT);
                  synchronized (allRecords) {
                    allRecords.addAll(csvRecords.getRecords().stream()
                        .skip(1)
                        .map(csvToObjectFunction)
                        .toList());
                  }
                } catch (IOException e) {
                  if (Pattern.compile("status code: 5\\d\\d").matcher(e.getMessage()).find()) {
                    log.error("Got 500-level exception from RescueTime:");
                    log.error(e);
                  } else {
                    throw new RuntimeException(e);
                  }
                }
              });
        });
    log.info("Received %s objects for '%s' taxonomy.", allRecords.size(), taxonomy);
    return Lists.newArrayList(allRecords);
  }

  @VisibleForTesting
  String getAPIKey() {
    return secretProvider.getSecret(RESCUETIME_API_KEY);
  }

  @VisibleForTesting
  String get(URI efficiencyUri) throws IOException {
    return Request.Get(efficiencyUri)
        .execute()
        .returnContent()
        .asString();
  }

}
