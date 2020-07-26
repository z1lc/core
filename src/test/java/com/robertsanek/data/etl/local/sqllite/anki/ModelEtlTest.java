package com.robertsanek.data.etl.local.sqllite.anki;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class ModelEtlTest {

  @Test
  @Disabled("integration")
  void name() throws Exception {
    List<Model> objects = InjectUtils.inject(ModelEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}