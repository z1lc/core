package com.robertsanek.data.etl.remote.wakatime;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class CodingTimeEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<CodingTime> objects = InjectUtils.inject(CodingTimeEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}