package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.remote.humanapi.entities.SleepSummary;
import com.robertsanek.util.inject.InjectUtils;

public class SleepSummaryEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<SleepSummary> objects = InjectUtils.inject(SleepSummaryEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}