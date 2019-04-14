package com.robertsanek.data.etl.local.sqllite.anki.connect;

import com.robertsanek.data.etl.local.sqllite.anki.connect.jsonschema.CardsInfoResult;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnkiConnectUtilsTest {

  @Test
  @Ignore("integration")
  public void getCardInfo() {
    CardsInfoResult cardInfo = AnkiConnectUtils.getCardInfo(1555217979670L);
    System.out.println("cardInfo = " + cardInfo);
  }

  @Test
  @Ignore("integration")
  public void getPersonNoteImage() {
    String personNoteImage = AnkiConnectUtils.getPersonNoteImage(1555217944784L);
    System.out.println("personNoteImage = " + personNoteImage);
  }

  @Test
  public void getAnkiExecutablePath() {
    assertEquals("C:\\Program Files\\Anki\\anki.exe", AnkiConnectUtils.getAnkiExecutablePath().orElseThrow());
  }

}