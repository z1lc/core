package com.robertsanek.data.etl.remote.rescuetime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.robertsanek.data.etl.SlowEtl;
import com.robertsanek.util.DateTimeUtils;

@SlowEtl
public class CategoryEtl extends RescueTimeEtl<Category> {

  private static AtomicInteger ID_ISSUER = new AtomicInteger(1);

  @Override
  public List<Category> getObjects() throws Exception {
    return genericGet("category", record ->
        Category.CategoryBuilder.aCategory()
            .withId(ID_ISSUER.getAndIncrement())
            .withDate(
                DateTimeUtils.toZonedDateTime(LocalDate.parse(StringUtils.left(record.get(0), 10), ISO_LOCAL_DATE)))
            .withTimeSpentSeconds(Long.valueOf(record.get(1)))
            .withCategory(record.get(3))
            .build());
  }
}
