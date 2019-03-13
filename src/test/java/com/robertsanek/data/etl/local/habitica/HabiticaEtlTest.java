package com.robertsanek.data.etl.local.habitica;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.etl.local.habitica.jsonentities.JsonTask;

public class HabiticaEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<JsonTask> objects = new TaskEtl().getJsonObjects();
    System.out.println("objects = " + objects);
  }
}