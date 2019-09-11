package com.robertsanek.data.etl.remote.scrape.toodledo;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class HabitEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<Habit> objects = new HabitEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}
