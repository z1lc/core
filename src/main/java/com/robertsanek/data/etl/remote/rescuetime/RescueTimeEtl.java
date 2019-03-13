package com.robertsanek.data.etl.remote.rescuetime;

import static com.robertsanek.util.SecretType.RESCUETIME_API_KEY;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

abstract class RescueTimeEtl<T> extends Etl<T> {

  private static final int FROM_YEAR = 2009; //first data in RescueTime is from 2009
  private static final int TO_YEAR = LocalDate.now().getYear();
  private static Log log = Logs.getLog(RescueTimeEtl.class);

  public <O> List<O> genericGet(String taxonomy, Function<CSVRecord, O> csvToObjectFunction) {
    Set<O> allRecords = Collections.synchronizedSet(Sets.newHashSet());
    IntStream.range(FROM_YEAR, TO_YEAR + 1).parallel()
        .forEach(currentYear -> {
          final URI efficiencyUri;
          try {
            efficiencyUri = new URIBuilder()
                .setScheme("https")
                .setHost("rescuetime.com")
                .setPath("/anapi/data")
                .setParameter("key", CommonProvider.getSecret(RESCUETIME_API_KEY))
                .setParameter("rs", "day")
                .setParameter("by", "interval")
                .setParameter("format", "csv")
                .setParameter("ty", taxonomy)
                .setParameter("rb", String.format("%s-01-01", currentYear))
                .setParameter("re", String.format("%s-01-01", currentYear + 1))
                .build();
            String csv = Request.Get(efficiencyUri).execute().returnContent().asString();
            CSVParser csvRecords = CSVParser.parse(csv, CSVFormat.DEFAULT);
            synchronized (allRecords) {
              allRecords.addAll(csvRecords.getRecords().stream()
                  .skip(1)
                  .map(csvToObjectFunction)
                  .collect(Collectors.toList()));
            }
          } catch (URISyntaxException | IOException e) {
            log.error("Exception thrown when performing RescueTime ETL:");
            log.error(e);
          }
        });
    log.info("Received %s objects for '%s' taxonomy.", allRecords.size(), taxonomy);
    return Lists.newArrayList(allRecords);
  }
}
