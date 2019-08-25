package com.robertsanek.data.etl.local.habitica;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class TaskEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Task> objects = new TaskEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}