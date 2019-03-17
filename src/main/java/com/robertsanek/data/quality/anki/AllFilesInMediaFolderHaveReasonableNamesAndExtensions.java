package com.robertsanek.data.quality.anki;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unit;
import com.robertsanek.util.platform.CrossPlatformUtils;
import com.robertsanek.util.platform.Platform;

public class AllFilesInMediaFolderHaveReasonableNamesAndExtensions extends DataQualityBase {

  static final Log log = Logs.getLog(AllFilesInMediaFolderHaveReasonableNamesAndExtensions.class);
  private static final ImmutableSet<String> REASONABLE_EXTENSIONS = ImmutableSet.of(
      "3gp",  //phone audio
      "css",
      "dat",  //some add-on storage
      "db",   //AnKindle add-on
      "gif",
      "jpg",  //.jpg should be preferred to .jpe and .jpeg so that we can more easily automatically
              // find an image based on a name
      "js",
      "json",  //some add-on storage
      "mp3",
      "oga",   //Ogg / Vorbis
      "ogg",   //Ogg / Vorbis
      "ogx",   //Ogg / Vorbis
      "png",
      "svg",
      "ttf",   //font files
      "wav"
  );

  @Override
  void runDQ() {
    CrossPlatformUtils.getPlatform().visit(new Platform.Visitor<Unit>() {
      @Override
      public Unit caseWindows10(Platform.Windows10 windows10) {
        File[] filesInMediaFolder = Optional.ofNullable(MEDIA_FOLDER.listFiles())
            .orElseThrow(() -> new RuntimeException("Didn't find any files in media folder."));

        //via https://stackoverflow.com/a/31976060
        Pattern forbiddenPatterns = Pattern.compile("[\\x00-\\x1F%<>:&\"/\\\\|?*]+");
        Stream.of(filesInMediaFolder)
            .flatMap(file -> {
              String fileName = file.getName();
              if (forbiddenPatterns.matcher(fileName).find()) {
                dqInformation.error("File with name '%s' uses invalid characters.", fileName);
              }
              List<String> periodSeparatedList = Arrays.asList(fileName.split("\\."));
              if (periodSeparatedList.size() == 0) {
                dqInformation.error("File with name '%s' has no extension.", fileName);
                return Stream.empty();
              }
              return Stream.of(Pair.of(Iterables.getLast(periodSeparatedList), fileName));
            })
            .collect(Collectors.groupingBy(Pair::getLeft, Collectors.mapping(Pair::getRight, Collectors.toSet())))
            .forEach((extension, fileNames) -> {
              if (!REASONABLE_EXTENSIONS.contains(extension)) {
                dqInformation.warn("%s files in media folder with disallowed extension '%s': \n%s",
                    fileNames.size(), extension, String.join("\n", fileNames));
              } else {
                //log.info("%s files with extension %s", fileNames.size(), extension);
              }
            });
        return Unit.unit();
      }

      @Override
      public Unit caseRaspberryPi(Platform.RaspberryPi raspberryPi) {
        log.info("Won't run on Raspberry Pi.");
        return Unit.unit();
      }

      @Override
      public Unit caseUbuntu(Platform.Ubuntu ubuntu) {
        log.info("Won't run on Ubuntu.");
        return Unit.unit();
      }
    });
  }
}
