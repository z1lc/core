package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class TaskEtlTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<ToodledoTask> objects = InjectUtils.inject(TaskEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}