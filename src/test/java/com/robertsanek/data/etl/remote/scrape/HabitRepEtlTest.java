package com.robertsanek.data.etl.remote.scrape;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.etl.remote.scrape.toodledo.Habit;
import com.robertsanek.data.etl.remote.scrape.toodledo.HabitEtl;

public class HabitRepEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Habit> objects = new HabitEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}