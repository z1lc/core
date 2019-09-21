package com.robertsanek.data.etl.remote.google.fit;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class BloodPressureEtlTest {

  @Test
  @Disabled("integration")
  public void integration() {
    BloodPressureEtl etl = InjectUtils.inject(BloodPressureEtl.class);
    List<BloodPressureReading> objects = etl.getObjects();
    System.out.println(objects);
  }
}