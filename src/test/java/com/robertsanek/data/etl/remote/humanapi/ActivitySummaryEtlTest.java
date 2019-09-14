package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.remote.humanapi.entities.ActivitySummary;
import com.robertsanek.util.inject.InjectUtils;

public class ActivitySummaryEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<ActivitySummary> objects = InjectUtils.inject(ActivitySummaryEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}