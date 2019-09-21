package com.robertsanek.data.etl.remote.fitbit;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.remote.fitbit.json.Sleep;
import com.robertsanek.util.inject.InjectUtils;

public class SleepEtlTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<Sleep> objects = InjectUtils.inject(SleepEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}