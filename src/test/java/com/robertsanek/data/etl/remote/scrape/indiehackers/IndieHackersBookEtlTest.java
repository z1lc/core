package com.robertsanek.data.etl.remote.scrape.indiehackers;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class IndieHackersBookEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<IndieHackersBook> objects = InjectUtils.inject(IndieHackersBookEtl.class).getObjects();
    System.out.println(objects);
  }
}