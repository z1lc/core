package com.robertsanek.data.quality.anki;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.robertsanek.data.etl.local.sqllite.anki.Note;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

/* HOW TO USE
 *
 * 1. Add tag 'temporary_person' to *all* person notes
 * 2. Use Edit > 'Export Cards as Text' (add-on)
 *        Use temporary_person in "Tag Filter"
 *        Check only "Include All Fields"
 * 3. Put exported data into folder core\src\main\resources\com\robertsanek\data\quality\anki\files\anki_export.csv
 * 4. Set SHOULD_RUN to true and run the class's associated integration test (this won't make any file/card changes)
 * 5. Evaluate results. If satisfied with the output, set SHOULD_RENAME to true and re-run (this will make file changes)
 * 5. Import file named core/out/anki/export.csv into Anki
 * 6. Make sure mapping of fields and total number of fields match (with default settings, this should be true).
 * 7. Use Tools>Check Media... and Tools>Empty Cards... to verify nothing went wrong.
 * 8. Remove 'temporary_person' tag from notes; use Notes > Clear Unused Tags
 * 9. Set SHOULD_RUN and SHOULD_RENAME back to false.
 */

public class RenameAllPersonImagesToUniformNaming extends DataQualityBase {

  static final Log log = Logs.getLog(RenameAllPersonImagesToUniformNaming.class);
  static final boolean SHOULD_RUN = false;
  static final boolean SHOULD_RENAME = false;

  @Override
  void runDQ() {
    if (SHOULD_RUN) {
      String mediaFolder = "C:\\Users\\z1lc\\AppData\\Roaming\\Anki2\\z1lc\\collection.media\\";
      List<FileNameChange> filesToChange = getAllNotesInRelevantDecks(1436872005312L).stream()
          .filter(note -> note.getId() != 1529614507428L) //HerO/herO StarCraft cards
          .sorted(Comparator.comparing(Note::getId))
          .flatMap(note -> {
            List<String> fields = splitCsvIntoCommaSeparatedList(note.getFields());
            final String cleanName = cleanName(fields.get(0)).replace(" ", "_");
            if (fields.size() >= 7) {
              String images = fields.get(6);
              final AtomicInteger count = new AtomicInteger(0);
              return Jsoup.parse(images).getElementsByTag("img").stream()
                  .flatMap(element -> {
                    String currentSrc = element.attr("src");
                    File file = new File(mediaFolder + currentSrc);
                    String targetSrc = String.format("Person_%s_%s.%s", cleanName,
                        String.valueOf(count.incrementAndGet()),
                        FilenameUtils.getExtension(file.getName()).toLowerCase());
                    if (currentSrc.equals(targetSrc)) {
                      return Stream.empty();
                    } else {
                      if (new File(mediaFolder + targetSrc).exists()) {
                        throw new RuntimeException(String.format("File with name %s already exists.", targetSrc));
                      }
                      return Stream.of(new FileNameChange(fields.get(0), currentSrc, targetSrc));
                    }
                  });
            } else {
              //log.info("Note with name '%s' didn't have enough fields", cleanName);
              return Stream.empty();
            }
          })
          .collect(Collectors.toList());

      if (filesToChange.size() > 0) {
        DateTimeFormatter ourFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm ssS");
        String dateTimeForFile = LocalDateTime.now().format(ourFormatter);
        File fileNameChangesTarget = new File(
            String.format(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/anki/%s.txt",
                dateTimeForFile));
        Unchecked.run(fileNameChangesTarget::createNewFile);
        log.info("%s files and %s notes to change. Full list written to %s",
            filesToChange.size(),
            filesToChange.stream().map(FileNameChange::getFirstField).distinct().count(),
            fileNameChangesTarget.getAbsolutePath());
        try (final PrintWriter writer = new PrintWriter(fileNameChangesTarget, StandardCharsets.UTF_8)) {
          writer.print(String.join("\n", Lists.transform(filesToChange, FileNameChange::toString)));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

        List<String> strings = Unchecked.get(() -> Resources.readLines(
            Resources.getResource("com/robertsanek/data/quality/anki/files/anki_export.csv"), Charsets.UTF_8)).stream()
            .skip(1) //skip the header line
            .distinct()
            .filter(line -> filesToChange.stream().anyMatch(file -> line.startsWith(file.getFirstField())))
            .collect(Collectors.toList());
        String allPersonCards = String.join("\n", strings);
        File ankiExport = new File(String
            .format(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/anki/%s export.csv",
                dateTimeForFile));
        for (FileNameChange fileNameChange : filesToChange) {
          allPersonCards = allPersonCards.replace(
              String.format("src=\"%s\"", fileNameChange.getOriginalFileName()),
              String.format("src=\"%s\"", fileNameChange.getTargetFileName()));
          if (SHOULD_RENAME) {
            String ankiMediaRoot =
                CrossPlatformUtils.getAnkiMediaFolderForUserz1lcIncludingTrailingSlash().orElseThrow();
            Unchecked.get(() -> Files.move(
                Paths.get((ankiMediaRoot + fileNameChange.getOriginalFileName())),
                Paths.get((ankiMediaRoot + fileNameChange.getTargetFileName()))));
          }
        }
        try (final PrintWriter writer = new PrintWriter(ankiExport, StandardCharsets.UTF_8)) {
          writer.print(allPersonCards);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } else {
        log.info("No files to change.");
      }

    }
  }

  static class FileNameChange {

    private String firstField;
    private String originalFileName;
    private String targetFileName;

    FileNameChange(String firstField, String originalFileName, String targetFileName) {
      this.firstField = firstField;
      this.originalFileName = originalFileName;
      this.targetFileName = targetFileName;
    }

    public String getFirstField() {
      return firstField;
    }

    public String getOriginalFileName() {
      return originalFileName;
    }

    public String getTargetFileName() {
      return targetFileName;
    }

    @Override
    public String toString() {
      return String.format("%s: %s => %s", firstField, originalFileName, targetFileName);
    }
  }
}
