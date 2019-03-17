package com.robertsanek.data.etl.local.sqllite.anki;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.RandomStringUtils;

import com.robertsanek.data.etl.local.sqllite.SQLiteEtl;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

public abstract class AnkiEtl<T> extends SQLiteEtl<T> {

  private static final Log log = Logs.getLog(AnkiEtl.class);
  private static final String PROFILE_NAME = "z1lc";
  static final int FIELDS_LIMIT = 10_000;
  private File tempCopiedAnkiDb;

  @Override
  public void preEtlStep() {
    AnkiSyncer.syncLocalCollectionIfOutOfDate(getProfileName());
  }

  @Override
  public void postEtlStep() {
    if (!tempCopiedAnkiDb.delete()) {
      log.error("Couldn't delete temporary copy of Anki database located at '%s'.");
    }
  }

  @Override
  public File getSQLiteDbFileLocation() {
    final Path fromPath = Path.of(String.format("%s/%s/collection.anki2",
        CrossPlatformUtils.getPlatform().getAnkiBaseDirectory().orElseThrow().toString(), getProfileName()));
    final String newFileName = RandomStringUtils.randomAlphanumeric(32).toLowerCase();
    final String target = String.format(System.getProperty("java.io.tmpdir") + "/%s.ankidb", newFileName);
    Unchecked.run(() -> Files.copy(fromPath,
        Paths.get(target),
        StandardCopyOption.REPLACE_EXISTING));
    tempCopiedAnkiDb = new File(target);
    return tempCopiedAnkiDb;
  }

  public String getProfileName() {
    return PROFILE_NAME;
  }

}
