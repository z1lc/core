package com.robertsanek.data.etl.remote.wikipedia;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class WikiPersonEtlTest {

  @Test
  @Ignore("integration")
  public void integration() {
    List<WikiPerson> objects = new WikiPersonEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}