package com.robertsanek.data.etl.local.sqllite.anki.connect;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.etl.local.sqllite.anki.connect.jsonschema.CardsInfoResult;
import com.robertsanek.util.platform.CrossPlatformUtils;
import com.robertsanek.util.platform.Platform;

public class AnkiConnectUtilsTest {

  @Test
  @Disabled("integration")
  public void getCardInfo() {
    CardsInfoResult cardInfo = new AnkiConnectUtils().getCardInfo(1555217979670L);
    System.out.println("cardInfo = " + cardInfo);
  }

  @Test
  @Disabled("integration")
  public void getPersonNoteImage() {
    String personNoteImage = new AnkiConnectUtils().getPersonNoteImage(1555217944784L);
    System.out.println("personNoteImage = " + personNoteImage);
  }

  @Test
  @Disabled("integration")
  public void search() {
    List<Long> search = new AnkiConnectUtils().getNoteIdsForSearch("\"<br></th>\" or \"<br></td>\"", "z1lc");
    System.out.println(search);
  }

  @Test
  @Disabled("integration")
  public void getFieldsForNote() {
    Map<String, String> fieldsForNote = new AnkiConnectUtils().getFieldsForNote(1568489381781L);
    System.out.println(fieldsForNote);
  }

  @Test
  @Disabled("integration")
  public void updateNoteFields() {
    new AnkiConnectUtils().updateNoteFields(1568489381806L, Map.of(
        "Born \uD83D\uDC76", "2001",
        "Died ☠️", "2002"
    ));
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

      @Override
      public Boolean caseMac(Platform.Mac ubuntu) {
        return true;
      }
    }));
  }

}