package com.robertsanek.data.etl.remote.google.analytics;

import java.util.List;

import com.robertsanek.data.etl.Etl;

public class PageViewEtl extends Etl<PageView> {

  @Override
  public List<PageView> getObjects() {
    return HelloAnalytics.getPageViews();
  }
}
