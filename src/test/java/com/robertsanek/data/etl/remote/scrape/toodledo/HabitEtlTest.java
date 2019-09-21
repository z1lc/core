package com.robertsanek.data.etl.remote.scrape.toodledo;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class HabitEtlTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<Habit> objects = InjectUtils.inject(HabitEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}
