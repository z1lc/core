package com.robertsanek.data.etl.local.sqllite.anki;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TemplateEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<Template> objects = new TemplateEtl().getObjects().stream().filter(temp -> temp.getDeck_id() != null).collect(
        Collectors.toList());
    System.out.println("objects = " + objects);
  }
}