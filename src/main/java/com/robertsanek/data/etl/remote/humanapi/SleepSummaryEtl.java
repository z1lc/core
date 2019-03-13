package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import com.robertsanek.data.etl.remote.humanapi.entities.SleepSummary;

public class SleepSummaryEtl extends HumanApiEtl<SleepSummary> {

  @Override
  public List<SleepSummary> getObjects() {
    return genericGet("/v1/human/sleeps/summaries", SleepSummary[].class);
  }

}
