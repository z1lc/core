package com.robertsanek.util.platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

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
      final String username = System.getProperty("user.name");
      final String gDrive = String.format("C:/Users/%s/Google Drive/", username);
      return new Platform.Windows10(
          Path.of(String.format("%score/", gDrive)),
          Path.of(String.format("C:/Users/%s/AppData/Roaming/Anki2/", username)),
          Path.of("C:/Program Files/Anki/anki.exe"),
          Path.of(String.format("C:/Users/%s/Desktop/", username)),
          Path.of(String.format("%s2 S-I/0 All Books/Calibre Library", gDrive)));
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
    } else if (SystemUtils.IS_OS_MAC) {
        return new Platform.Ubuntu(
            Path.of("/Users/rsanek/Google Drive/core/"),
            Path.of("/Users/rsanek/Library/Application Support/Anki2/"),
            null,
            Path.of("/Users/rsanek/Desktop/"),
            Path.of("/Users/rsanek/Google Drive/2 S-I/0 All Books/Calibre Library"));
    } else {
      throw new RuntimeException(String.format("Couldn't detect platform for operating system %s.",
          System.getProperty("os.name")));
    }
  }

  //via https://stackoverflow.com/a/52581380
  public static Boolean isRunningInsideDocker() {
    return getPlatform().visit(new Platform.Visitor<>() {
      @Override
      public Boolean caseWindows10(Platform.Windows10 windows10) {
        return false;
      }

      @Override
      public Boolean caseRaspberryPi(Platform.RaspberryPi raspberryPi) {
        return false;
      }

      @Override
      public Boolean caseUbuntu(Platform.Ubuntu ubuntu) {
        try (Stream<String> stream = Files.lines(Paths.get("/proc/1/cgroup"))) {
          return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
          return false;
        }
      }

      @Override
      public Boolean caseMac(Platform.Mac ubuntu) {
        return false;
      }
    });
  }

}
