package com.robertsanek.data.etl.remote.fitbit;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.remote.fitbit.json.Sleep;

public class SleepEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<Sleep> objects = new SleepEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}