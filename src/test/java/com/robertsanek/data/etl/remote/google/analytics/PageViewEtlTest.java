package com.robertsanek.data.etl.remote.google.analytics;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class PageViewEtlTest {

  @Test
  @Disabled("integration")
  public void PageViewEtl() {
    List<PageView> objects = InjectUtils.inject(PageViewEtl.class).getObjects();
    System.out.println(objects);
  }
}