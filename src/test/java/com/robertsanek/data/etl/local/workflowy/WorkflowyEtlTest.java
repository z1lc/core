package com.robertsanek.data.etl.local.workflowy;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class WorkflowyEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<Entry> objects = new WorkflowyEtl().getObjects();
    System.out.println("objects = " + objects);
  }

}