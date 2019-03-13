package com.robertsanek.data.etl.remote.humanapi;

import java.util.List;

import com.robertsanek.data.etl.remote.humanapi.entities.ActivitySummary;

public class ActivitySummaryEtl extends HumanApiEtl<ActivitySummary> {

  @Override
  public List<ActivitySummary> getObjects() {
    return genericGet("/v1/human/activities/summaries", ActivitySummary[].class);
  }

}
