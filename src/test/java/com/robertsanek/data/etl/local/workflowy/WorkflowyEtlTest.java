package com.robertsanek.data.etl.local.workflowy;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class WorkflowyEtlTest {

  @Test
  @Disabled("integration")
  public void integration() throws Exception {
    List<Entry> objects = InjectUtils.inject(WorkflowyEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }

}