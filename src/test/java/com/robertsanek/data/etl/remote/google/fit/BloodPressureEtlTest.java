package com.robertsanek.data.etl.remote.google.fit;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.util.inject.InjectUtils;

public class BloodPressureEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    BloodPressureEtl etl = InjectUtils.inject(BloodPressureEtl.class);
    List<BloodPressureReading> objects = etl.getObjects();
    System.out.println(objects);
  }
}