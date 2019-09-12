package com.robertsanek.data.etl.remote.google.analytics;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PageViewEtlTest {

  @Test
  @Disabled("integration")
  public void PageViewEtl() {
    List<PageView> objects = new PageViewEtl().getObjects();
    System.out.println(objects);
  }
}