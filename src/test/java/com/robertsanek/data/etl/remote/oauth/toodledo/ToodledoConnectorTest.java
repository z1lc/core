package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.etl.remote.oauth.toodledo.jsonentities.JsonTask;

public class ToodledoConnectorTest {

  @Test
  @Ignore("integration")
  public void getTasks() {
    List<JsonTask> tasks = new ToodledoConnector().getTasks();
    System.out.println("tasks = " + tasks);
  }
}