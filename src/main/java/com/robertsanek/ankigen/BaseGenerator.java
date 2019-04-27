package com.robertsanek.ankigen;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.fluent.Request;

import com.robertsanek.data.quality.anki.AllNumericalClozeDeletionsHaveHashtagHint;
import com.robertsanek.data.quality.anki.DataQualityBase;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

public abstract class BaseGenerator {

  public abstract List<PersonNote> getPersons() throws Exception;
  static final Log log = Logs.getLog(BaseGenerator.class);

  public void writeFiles() {
    log.info("Creating new_people.csv file and pictures folder on the Desktop...");
    String outName = getRootDestination() + "new_people.csv";
    try (Writer writer = Files.newBufferedWriter(Paths.get(outName));
         CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
      String directory = getRootDestination() + "pictures/";
      if (!new File(directory).exists() && !new File(directory).mkdirs()) {
        throw new RuntimeException("Failed to create directory on desktop!");
      }
      log.info("Getting people & downloading images...");
      Unchecked.get(this::getPersons).parallelStream().forEach(person -> {
        threadSafePrintRecord(csvPrinter,
            person.getName(),
            person.getNamePronunciation(),
            person.getKnownFor(),
            person.getBorn(),
            person.getDied(),
            person.getContext(),
            IntStream.range(0, person.getImage().size())
                .mapToObj(i -> {
                  URI uri = person.getImage().get(i);
                  String fileName = getPersonFileName(uri, person.getName(), i + 1);
                  File destination = new File(directory + fileName);
                  try {
                    Request.Get(uri)
                        .execute()
                        .saveContent(destination);
                  } catch (IOException ignored) {
                  }
                  return String.format("<img src='%s'>", fileName);
                })
                .collect(Collectors.joining("")),
            person.getSource()
        );
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static synchronized void threadSafePrintRecord(CSVPrinter csvPrinter, Object... values) {
    try {
      csvPrinter.printRecord(values);
    } catch (IOException ignored) {
    }
  }

  private String getRootDestination() {
    return CrossPlatformUtils.getDesktopPathIncludingTrailingSlash().orElseThrow();
  }

  private String getPersonFileName(URI location, String name, int num) {
    return String.format("Person_%s_%s.%s",
        DataQualityBase.cleanName(name).replace(" ", "_"),
        num,
        FilenameUtils.getExtension(location.toString()));
  }

}
