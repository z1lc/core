package com.robertsanek.data.etl.remote.oauth.toodledo;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.remote.oauth.toodledo.jsonentities.JsonTask;

public class ToodledoConnectorTest {

  @Test
  @Disabled("integration")
  public void getTasks() {
    List<JsonTask> tasks = new ToodledoConnector().getTasks();
    System.out.println("tasks = " + tasks);
  }
}