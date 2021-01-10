package com.robertsanek.data.etl.remote.wikipedia;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class WikiPersonEtlTest {

  @Test
//  @Disabled("integration")
  public void integration() {
    List<WikiPerson> objects = InjectUtils.inject(WikiPersonEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}