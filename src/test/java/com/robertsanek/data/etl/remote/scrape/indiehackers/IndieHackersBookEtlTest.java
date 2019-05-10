package com.robertsanek.data.etl.remote.scrape.indiehackers;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class IndieHackersBookEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<IndieHackersBook> objects = new IndieHackersBookEtl().getObjects();
    System.out.println(objects);
  }
}