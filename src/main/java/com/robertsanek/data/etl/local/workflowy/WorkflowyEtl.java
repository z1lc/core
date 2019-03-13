package com.robertsanek.data.etl.local.workflowy;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileReader;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.robertsanek.data.etl.DoNotRun;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.UsesLocalFiles;
import com.robertsanek.util.DateTimeUtils;
import com.robertsanek.util.platform.CrossPlatformUtils;

import be.ceau.opml.OpmlParser;
import be.ceau.opml.entity.Opml;
import be.ceau.opml.entity.Outline;

@DoNotRun(explanation = "uses file that I have to manually download, so basically will always be out-of-date")
@UsesLocalFiles
public class WorkflowyEtl extends Etl<Entry> {

  static final int TEXT_LIMIT = 10_000;
  private AtomicLong logicalClock = new AtomicLong(1);
  private ZonedDateTime dateExported;

  @Override
  public List<Entry> getObjects() throws Exception {
    File workflowyExport = new File(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() +
        "manual_file_download/workflowy-export.opml");
    dateExported = DateTimeUtils.toZonedDateTime(Instant.ofEpochMilli(workflowyExport.lastModified()));
    Opml parsedData = new OpmlParser().parse(new FileReader(workflowyExport, UTF_8));
    List<Outline> outlines = parsedData.getBody().getOutlines();
    long firstId = logicalClock.getAndIncrement();
    return outlines.stream().flatMap(outline -> processOutline(outline, firstId).stream()).collect(Collectors.toList());
  }

  private List<Entry> processOutline(Outline outline, long parentId) {
    long thisId = logicalClock.getAndIncrement();
    List<Outline> subElements = outline.getSubElements();
    Entry entry = Entry.EntryBuilder.anEntry()
        .withId(thisId)
        .withParent_id(parentId)
        .withText(outline.getText())
        .withNote(outline.getAttribute("_note"))
        .withNum_children((long) subElements.size())
        .withDate_exported(dateExported)
        .build();
    List<Entry> subEntries = subElements.stream()
        .flatMap(subOutline -> processOutline(subOutline, thisId).stream())
        .collect(Collectors.toList());
    subEntries.add(0, entry);
    return subEntries;
  }

}
