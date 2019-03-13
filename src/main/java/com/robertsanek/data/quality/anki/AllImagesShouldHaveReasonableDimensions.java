package com.robertsanek.data.quality.anki;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.SystemUtils;

import com.google.common.collect.ImmutableSet;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllImagesShouldHaveReasonableDimensions extends DataQualityBase {

  static final Log log = Logs.getLog(AllImagesShouldHaveReasonableDimensions.class);
  private static final ImmutableSet<String> IMAGE_EXTENSIONS = ImmutableSet.of(
      "gif",
      "jpe",
      "jpeg",
      "jpg",
      "png"
  );
  private static final int MAXIMUM_HEIGHT_OR_WIDTH = 3840;

  @Override
  void runDQ() {
    if (SystemUtils.IS_OS_MAC) {
      File[] filesInMediaFolder = Optional.ofNullable(MEDIA_FOLDER.listFiles())
          .orElseThrow(() -> new RuntimeException("Didn't find any files in media folder."));
      Stream.of(filesInMediaFolder).parallel()
          .filter(file -> IMAGE_EXTENSIONS.stream().anyMatch(ext -> file.getName().endsWith(ext)))
          .forEach(file -> {
            try {
              BufferedImage image = ImageIO.read(file);
              int width = image.getWidth();
              int height = image.getHeight();
              if (width > MAXIMUM_HEIGHT_OR_WIDTH || height > MAXIMUM_HEIGHT_OR_WIDTH) {
                //TODO: automatically resize
                dqInformation.error("Image '%s' of size %sx%s has a dimension that exceeds the %spx limit.",
                    file.getName(), width, height, MAXIMUM_HEIGHT_OR_WIDTH);
              }
            } catch (IOException ignored) {
            } //couldn't parse image; that's fine
          });
    }
  }
}
