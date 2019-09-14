package com.robertsanek.data.etl.local.habitica;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class NewHistoryEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<History> objects = InjectUtils.inject(NewHistoryEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}