package com.robertsanek.data.etl.remote.fitbit;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class ActivityEtlTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<Activity> objects = InjectUtils.inject(ActivityEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }

}