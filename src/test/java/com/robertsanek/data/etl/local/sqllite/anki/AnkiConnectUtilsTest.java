package com.robertsanek.data.etl.local.sqllite.anki;

import org.junit.Test;

import static org.junit.Assert.*;

public class AnkiConnectUtilsTest {

  @Test
  public void getAnkiExecutablePath() {
    assertEquals("C:\\Program Files\\Anki\\anki.exe", AnkiConnectUtils.getAnkiExecutablePath().orElseThrow());
  }

}