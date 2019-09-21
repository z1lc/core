package com.robertsanek.data.etl.remote.google.sheets.health;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class HealthEtlTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<Health> objects = InjectUtils.inject(HealthEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }

  @Test
  public void getTotal() {
    assertEquals(new BigDecimal(0), new HealthEtl().getTotal(Optional.empty(), Optional.empty()));
    assertEquals(new BigDecimal(1), new HealthEtl().getTotal(Optional.of(new BigDecimal(1)), Optional.empty()));
    assertEquals(new BigDecimal(1), new HealthEtl().getTotal(Optional.empty(), Optional.of(new BigDecimal(1))));
    assertEquals(new BigDecimal(2),
        new HealthEtl().getTotal(Optional.of(new BigDecimal(1)), Optional.of(new BigDecimal(1))));
  }
}