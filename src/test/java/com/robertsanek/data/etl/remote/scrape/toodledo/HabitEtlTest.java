package com.robertsanek.data.etl.remote.scrape.toodledo;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class HabitEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Habit> objects = new HabitEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}