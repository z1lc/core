package com.robertsanek.data.etl.remote.google.fit;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.DateTimeUtils;

public class BloodPressureEtl extends Etl<BloodPressureReading> {

  private static AtomicLong ID_ISSUER = new AtomicLong(1);

  @Inject FitConnector fitConnector;

  @Override
  public List<BloodPressureReading> getObjects() {
    return fitConnector.getBloodPressureReadings().getPoint().stream()
        .map(point -> BloodPressureReading.BloodPressureReadingBuilder.aBloodPressureReading()
            .withId(ID_ISSUER.getAndIncrement())
            .withDate(DateTimeUtils.toZonedDateTime(
                Instant.ofEpochMilli(
                    Long.parseLong(point.getStartTimeNanos()) / 1_000_000)))
            .withSystolic(point.getValue().get(0).getFpVal().longValue())
            .withDiastolic(point.getValue().get(1).getFpVal().longValue())
            .build())
        .collect(Collectors.toList());
  }
}
