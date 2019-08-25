package com.robertsanek.data.etl.local.habitica;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.robertsanek.data.etl.DoNotRun;
import com.robertsanek.util.DateTimeUtils;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

@DoNotRun(explanation = "messes with local files; don't care about habitica anymore")
public class OldHistoryEtl extends HabiticaEtl<History> {

  private static Log log = Logs.getLog(OldHistoryEtl.class);

  @Override
  public List<History> getObjects() {
    List<HistoryCsv> historyCSVs = Unchecked.get(() -> new HabiticaHistorySaver().getHistoryAndSaveAsCsv());
    Map<String, String> nameToId = historyCSVs.stream()
        .map(history -> Pair.of(
            history.getName()
                .replaceAll("[^\\x00-\\x7F]", "")
                .replaceAll("ZZZ", "")
                .trim(),
            history.getId()))
        .distinct()
        .collect(Collectors
            .groupingBy(Pair::getLeft,
                Collectors.flatMapping(p -> Stream.of(p.getRight()),
                    Collectors.collectingAndThen(Collectors.toList(), list -> {
                          if (list.size() != 1) {
                            log.warn("Habitica returned multiple IDs for the same task name: %s", list);
                          }
                          return list.get(0);
                        }
                    ))));
    Pattern captureDate = Pattern.compile(".*habitica-(\\d+)\\.csv$");
    File folder = new File(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/habitica/");
    File[] listOfFiles = folder.listFiles();

    List<LocalDate> fileDates = Arrays.stream(listOfFiles)
        .map(file -> Unchecked.get(() ->
            LocalDate.parse(
                Iterables.getOnlyElement(
                    captureDate.matcher(file.getName()).results().collect(Collectors.toList())).group(1),
                HabiticaHistorySaver.ourFormatter)))
        .collect(Collectors.toList());

    log.info("Found files with dates %s", fileDates);

    CsvMapper mapper = new CsvMapper();
    CsvSchema schema = mapper.schemaFor(HistoryCsv.class).withHeader();
    Set<String> missingMappingNames = Sets.newHashSet();
    List<History> collect = IntStream.range(0, fileDates.size())
        .mapToObj(j -> Unchecked.get(() -> {
          File file = new File(String.format(
              CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/habitica/habitica-%s.csv",
              HabiticaHistorySaver.ourFormatter.format(fileDates.get(j))));
          ZonedDateTime etlDateFromFilename = DateTimeUtils.toZonedDateTime(LocalDate.parse(
              Iterables.getOnlyElement(
                  captureDate.matcher(file.getName()).results().collect(Collectors.toList())).group(1),
              HabiticaHistorySaver.ourFormatter));
          log.info("Reading file %s.", file.getName());
          MappingIterator<HistoryCsv> objectMappingIterator =
              mapper.readerFor(HistoryCsv.class).with(schema).readValues(file);
          List<HistoryCsv> histories = objectMappingIterator.readAll();

          Map<String, List<HistoryCsv>> historyByName =
              histories.stream()
                  //first real start date for file habitica-20180416.csv
                  .filter(historyCsv -> historyCsv.getDate().isAfter(LocalDateTime.of(2018, 1, 23, 0, 0)))
                  .filter(historyCsv -> {
                    if (j > 0) {
                      //don't include data from a later ETL that is covered by a previous ETL,
                      //except for the same date that the ETL actually happened
                      return historyCsv.getDate().toLocalDate().isAfter(fileDates.get(j - 1));
                    } else {
                      return true;
                    }
                  })
                  .collect(Collectors.groupingBy(HistoryCsv::getName));
          return historyByName.entrySet().stream()
              .flatMap(idHistoryPair -> {
                List<HistoryCsv> taskHistoryCsv = idHistoryPair.getValue();
                List<History> fulfillHistoryCsv = Lists.newArrayList();
                String id = idHistoryPair.getValue().get(0).getId();
                if (id.equals("")) {
                  String name = idHistoryPair.getValue().get(0).getName()
                      .replaceAll("<f0>", "")
                      .replaceAll("<U.*>", "")
                      .replaceAll("[^\\x00-\\x7F]", "")
                      .replaceAll("ZZZ", "")
                      .trim();
                  Optional<String> maybeId = Optional.ofNullable(nameToId.get(name));
                  if (maybeId.isPresent()) {
                    id = maybeId.orElseThrow();
                  } else {
                    missingMappingNames.add(name);
                    return Stream.empty();
                  }
                }
                fulfillHistoryCsv.add(
                    History.HistoryBuilder.aHistory()
                        .withId(UUID.randomUUID().toString())
                        .withDate(
                            DateTimeUtils.toZonedDateTime(idHistoryPair.getValue().get(0).getDate().toLocalDate()))
                        .withTaskId(id)
                        .withCompleted(Boolean.TRUE)
                        .build());
                for (int i = 1; i < taskHistoryCsv.size(); i++) {
                  Optional<Boolean> aBool =
                      NewHistoryEtl.increaseOrDecrease(taskHistoryCsv.get(i).getValue(), taskHistoryCsv.get(i - 1).getValue());
                  if (aBool.isPresent()) {
                    fulfillHistoryCsv.add(
                        History.HistoryBuilder.aHistory()
                            .withId(UUID.randomUUID().toString())
                            .withDate(
                                DateTimeUtils.toZonedDateTime(idHistoryPair.getValue().get(i).getDate().toLocalDate()))
                            .withTaskId(id)
                            .withCompleted(aBool.orElseThrow())
                            .build());
                  }
                }
                return fulfillHistoryCsv.stream();
              });
        }))
        .flatMap(Function.identity())
        //          .collect(Collectors.groupingBy(history -> Pair.of(history.getTask_id(), history.getDate())))
        //          .entrySet().stream()
        //          .map(pair -> pair.getValue().stream().max(Comparator.comparing(History::getEtl_date)).orElseThrow())
        .collect(Collectors.toList());
    log.warn("Missing mappings for %s names:\n%s",
        missingMappingNames.size(), String.join("\n", missingMappingNames));
    return collect;
  }

}
