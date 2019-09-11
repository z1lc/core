package com.robertsanek.data.etl.local.habitica;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.local.habitica.jsonentities.JsonTask;

public class HabiticaEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<JsonTask> objects = new TaskEtl().getJsonObjects();
    System.out.println("objects = " + objects);
  }
}