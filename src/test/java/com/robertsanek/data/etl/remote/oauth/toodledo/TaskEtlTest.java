package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class TaskEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<ToodledoTask> objects = new TaskEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}