package com.robertsanek.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import com.robertsanek.process.Command;
import com.robertsanek.process.SuccessType;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class LastAlertedProviderImpl implements LastAlertedProvider {

  private static String target = CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "_SUCCESS/";
  private static ZonedDateTime DEFAULT_DATETIME = ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

  public ZonedDateTime getLast(Command command, SuccessType successType) {
    try (Stream<String> lines =
             Files.lines(Paths.get(target + command.toString() + "." + successType.toString().toLowerCase()))) {
      List<ZonedDateTime> collect = lines
          .map(l -> ZonedDateTime.parse(l, DateTimeFormatter.ISO_ZONED_DATE_TIME))
          .toList();
      return collect.size() != 1 ? DEFAULT_DATETIME : collect.iterator().next();
    } catch (IOException e) {
      return DEFAULT_DATETIME;
    }
  }

}
