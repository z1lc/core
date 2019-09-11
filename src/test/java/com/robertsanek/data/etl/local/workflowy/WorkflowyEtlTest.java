package com.robertsanek.data.etl.local.workflowy;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class WorkflowyEtlTest {

  @Test
  @Disabled("integration")
  public void name() throws Exception {
    List<Entry> objects = new WorkflowyEtl().getObjects();
    System.out.println("objects = " + objects);
  }

}