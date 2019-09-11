package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.remote.humanapi.entities.SleepSummary;

public class SleepSummaryEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<SleepSummary> objects = new SleepSummaryEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}