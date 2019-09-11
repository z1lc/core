package com.robertsanek.data.etl.local.habitica;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TaskEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<Task> objects = new TaskEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}