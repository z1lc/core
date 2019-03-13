package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.etl.remote.humanapi.entities.ActivitySummary;

public class ActivitySummaryEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<ActivitySummary> objects = new ActivitySummaryEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}