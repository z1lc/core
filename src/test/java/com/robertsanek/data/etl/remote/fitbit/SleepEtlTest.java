package com.robertsanek.data.etl.remote.fitbit;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.etl.remote.fitbit.json.Sleep;

public class SleepEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Sleep> objects = new SleepEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}