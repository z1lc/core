package com.robertsanek.util.platform;

import java.nio.file.Path;
import java.util.Optional;

import org.apache.commons.lang3.SystemUtils;

public class CrossPlatformUtils {

  public static Optional<String> getRootPathIncludingTrailingSlash() {
    return getPlatform().getFileStorageDirectory().map(s -> s.toString().replace("\\", "/") + "/");
  }

  public static Optional<String> getDesktopPathIncludingTrailingSlash() {
    return getPlatform().getDesktop().map(s -> s.toString().replace("\\", "/") + "/");
  }

  public static Optional<String> getAnkiMediaFolderForUserz1lcIncludingTrailingSlash() {
    return getPlatform().getAnkiBaseDirectory().map(s -> s.toString().replace("\\", "/") + "/z1lc/collection.media/");
  }

  public static Platform getPlatform() {
    if (SystemUtils.IS_OS_WINDOWS) {
      return new Platform.Windows10(
          Path.of("Z:/core/"),
          Path.of("C:/Users/z1lc/AppData/Roaming/Anki2/"),
          Path.of("C:/Program Files/Anki/anki.exe"),
          Path.of("C:/Users/z1lc/Desktop/"),
          Path.of("C:/Users/z1lc/Google Drive/2 S-I/0 All Books/Calibre Library"));
    } else if (SystemUtils.IS_OS_LINUX) {
      if (System.getProperty("user.home").contains("/pi")) {
        return new Platform.RaspberryPi(
            Path.of("/home/pi/core/"),
            Path.of("/home/pi/Documents/Anki/"),
            null,
            null,
            null);
      } else {
        return new Platform.Ubuntu(
            Path.of("/home/robert/core/"),
            Path.of("/home/robert/.local/share/Anki2/"),
            null,
            null,
            null);
      }
    } else {
      throw new RuntimeException(String.format("Couldn't detect platform for operating system %s.",
          System.getProperty("os.name")));
    }
  }

}
