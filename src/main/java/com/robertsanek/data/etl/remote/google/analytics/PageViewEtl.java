package com.robertsanek.data.etl.remote.google.analytics;

import java.util.List;

import com.robertsanek.data.etl.DoNotRun;
import com.robertsanek.data.etl.Etl;

@DoNotRun(explanation = "stopped working sometime in late 2022 / 2023, don't really use this anyway so decided to just turn off")
public class PageViewEtl extends Etl<PageView> {

  @Override
  public List<PageView> getObjects() {
    return HelloAnalytics.getPageViews();
  }
}
