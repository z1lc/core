package com.robertsanek.data.etl.local.sqllite.anki.connect;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.data.etl.local.sqllite.anki.connect.jsonschema.CardsInfoResult;
import com.robertsanek.util.platform.CrossPlatformUtils;
import com.robertsanek.util.platform.Platform;

public class AnkiConnectUtilsTest {

  @Test
  @Ignore("integration")
  public void getCardInfo() {
    CardsInfoResult cardInfo = new AnkiConnectUtils().getCardInfo(1555217979670L);
    System.out.println("cardInfo = " + cardInfo);
  }

  @Test
  @Ignore("integration")
  public void getPersonNoteImage() {
    String personNoteImage = new AnkiConnectUtils().getPersonNoteImage(1555217944784L);
    System.out.println("personNoteImage = " + personNoteImage);
  }

  @Test
  public void getAnkiExecutablePath() {
    assertTrue(CrossPlatformUtils.getPlatform().visit(new Platform.Visitor<>() {
      @Override
      public Boolean caseWindows10(Platform.Windows10 windows10) {
        return "C:\\Program Files\\Anki\\anki.exe".equals(new AnkiConnectUtils().getAnkiExecutablePath().orElseThrow());
      }

      @Override
      public Boolean caseRaspberryPi(Platform.RaspberryPi raspberryPi) {
        return true;
      }

      @Override
      public Boolean caseUbuntu(Platform.Ubuntu ubuntu) {
        return true;
      }
    }));
  }

}