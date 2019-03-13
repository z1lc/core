package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.etl.remote.humanapi.entities.SleepSummary;

public class SleepSummaryEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<SleepSummary> objects = new SleepSummaryEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}