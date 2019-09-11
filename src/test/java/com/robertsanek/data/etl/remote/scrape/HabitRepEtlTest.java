package com.robertsanek.data.etl.remote.scrape;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.remote.scrape.toodledo.Habit;
import com.robertsanek.data.etl.remote.scrape.toodledo.HabitEtl;

public class HabitRepEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<Habit> objects = new HabitEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}