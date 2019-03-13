package com.robertsanek.data.etl.remote.rescuetime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.robertsanek.data.etl.SlowEtl;

@SlowEtl
public class EfficiencyEtl extends RescueTimeEtl<Efficiency> {

  @Override
  public List<Efficiency> getObjects() throws Exception {
    return genericGet("efficiency", record ->
        Efficiency.EfficiencyBuilder.anEfficiency()
            .withDate(ZonedDateTime.of(LocalDate.parse(StringUtils.left(record.get(0), 10), ISO_LOCAL_DATE),
                LocalTime.of(0, 0), ZoneId.of("America/Los_Angeles")))
            .withTimeSpentSeconds(Long.valueOf(record.get(1)))
            .withEfficiency(Double.valueOf(record.get(3)))
            .withEfficiencyPercent(Double.valueOf(record.get(4)))
            .build());
  }

}
