package com.robertsanek.data.quality.anki;

import com.google.common.collect.Lists;
import com.robertsanek.data.etl.local.sqllite.anki.Note;
import com.robertsanek.data.etl.local.sqllite.anki.connect.AnkiConnectUtils;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;

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

public class RenameAllPersonImagesToUniformNaming extends DataQualityBase {

  static final Log log = Logs.getLog(RenameAllPersonImagesToUniformNaming.class);
  static final boolean SHOULD_RUN = true;
  static final boolean SHOULD_RENAME = true;

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
                        throw new RuntimeException(String.format("File with name '%s' already exists. This likely " +
                                "means you have unused media files -- delete them by using Tools > Check Media...",
                            targetSrc));
                      }
                      return Stream.of(new FileNameChange(note.getId(), fields.get(0), currentSrc, targetSrc));
                    }
                  });
            } else {
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

        AnkiConnectUtils.loadProfile("z1lc");
        for (FileNameChange fileNameChange : filesToChange) {
          if (SHOULD_RENAME) {
            String ankiMediaRoot =
                CrossPlatformUtils.getAnkiMediaFolderForUserz1lcIncludingTrailingSlash().orElseThrow();
            Unchecked.get(() -> Files.move(
                Paths.get((ankiMediaRoot + fileNameChange.getOriginalFileName())),
                Paths.get((ankiMediaRoot + fileNameChange.getTargetFileName()))));
            if (!AnkiConnectUtils.updatePersonNoteImage(fileNameChange.getNoteId(),
                fileNameChange.getOriginalFileName(),
                fileNameChange.getTargetFileName())) {
              throw new RuntimeException(String.format(
                  "Failed to use anki-connect to update Person %s.", fileNameChange.getFirstField()));
            }
          }
        }
        AnkiConnectUtils.triggerSync();
      } else {
        log.info("No files to change.");
      }

    }
  }

  static class FileNameChange {

    private Long noteId;
    private String firstField;
    private String originalFileName;
    private String targetFileName;

    FileNameChange(Long noteId, String firstField, String originalFileName, String targetFileName) {
      this.noteId = noteId;
      this.firstField = firstField;
      this.originalFileName = originalFileName;
      this.targetFileName = targetFileName;
    }

    public Long getNoteId() {
      return noteId;
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
