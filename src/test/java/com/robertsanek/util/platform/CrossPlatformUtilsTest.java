package com.robertsanek.util.platform;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

public class CrossPlatformUtilsTest {

  @Test
  public void getRootPathIncludingTrailingSlash() {
    assertTrue(CrossPlatformUtils.getPlatform().visit(new Platform.Visitor<>() {
      @Override
      public Boolean caseWindows10(Platform.Windows10 windows10) {
        return "C:/Users/z1lc/Google Drive/core/".equals(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow());
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

  @Test
  public void getAnkiMediaFolderForUserz1lcIncludingTrailingSlash() {
    assertTrue(CrossPlatformUtils.getPlatform().visit(new Platform.Visitor<>() {
      @Override
      public Boolean caseWindows10(Platform.Windows10 windows10) {
        return "C:/Users/z1lc/AppData/Roaming/Anki2/z1lc/collection.media/".equals(
            CrossPlatformUtils.getAnkiMediaFolderForUserz1lcIncludingTrailingSlash().orElseThrow());
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