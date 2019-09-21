package com.robertsanek.data.etl.local.habitica;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.local.habitica.jsonentities.JsonTask;
import com.robertsanek.util.inject.InjectUtils;

public class HabiticaEtlTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<JsonTask> objects = InjectUtils.inject(TaskEtl.class).getJsonObjects();
    System.out.println("objects = " + objects);
  }
}