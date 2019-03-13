package com.robertsanek.util.platform;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CrossPlatformUtilsTest {

  @Test
  public void getRootPathIncludingTrailingSlash() {
    assertEquals("Z:/core/", CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow());
  }

  @Test
  public void getAnkiMediaFolderForUserz1lcIncludingTrailingSlash() {
    assertEquals("C:/Users/z1lc/AppData/Roaming/Anki2/z1lc/collection.media/",
        CrossPlatformUtils.getAnkiMediaFolderForUserz1lcIncludingTrailingSlash().orElseThrow());
  }

}