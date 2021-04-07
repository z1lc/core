package com.robertsanek.data.quality.anki;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unit;
import com.robertsanek.util.platform.CrossPlatformUtils;
import com.robertsanek.util.platform.Platform;

public class AllCountiesHavePronunciations extends DataQualityBase {

  static final Log log = Logs.getLog(AllCountiesHavePronunciations.class);
  private static final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1527659218804L,  //Casablanca-Settat
      1527659341502L,  //Marrakesh-Safi
      0L
  );

  @Override
  void runDQ() {
    CrossPlatformUtils.getPlatform().visit(new Platform.Visitor<Unit>() {
      @Override
      public Unit caseWindows10(Platform.Windows10 windows10) {
        File mediaFolder = new File("C:\\Users\\z1lc\\AppData\\Roaming\\Anki2\\z1lc\\collection.media\\");
        File[] filesInMediaFolder = Optional.ofNullable(mediaFolder.listFiles())
            .orElseThrow(() -> new RuntimeException("Didn't find any files in media folder."));
        Set<String> fileNames = Arrays.stream(filesInMediaFolder)
            .map(File::getName)
            .collect(Collectors.toSet());
        getAllNotesInRelevantDecks(1437392508740L, NOTE_ID_EXCLUSIONS).stream()
            .map(note -> {
              List<String> fields = splitCsvIntoCommaSeparatedList(note.getFields());
              return fields.get(2);
            })
            .distinct()
            .forEach(county -> {
              if (!county.isEmpty() && !fileNames.contains("_" + county + ".mp3")) {
                String toOutput = String.format("No pronunciation exists for county '%s'. Expected filename is '_%s.mp3'", county, county);
                violations.put(AllCountiesHavePronunciations.class, toOutput);
                dqInformation.error(toOutput);
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

      @Override
      public Unit caseMac(Platform.Mac ubuntu) {
        log.info("Won't run on Mac.");
        return Unit.unit();
      }
    });
  }
}
