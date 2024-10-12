package com.robertsanek.data.etl.local.sqllite.calibre;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.RandomStringUtils;

import com.robertsanek.data.etl.local.sqllite.SQLiteEtl;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

public abstract class CalibreEtl<T> extends SQLiteEtl<T> {

  private File tempCopiedCalibreDb;

  @Override
  public File getSQLiteDbFileLocation() {
    final Path fromPath = Path.of(String.format("%s/metadata.db",
        CrossPlatformUtils.getPlatform().getCalibreLibraryDirectory().orElseThrow().toString()));
    final String newFileName = RandomStringUtils.insecure().nextAlphanumeric(32).toLowerCase();
    final String target = String.format(System.getProperty("java.io.tmpdir") + "/%s.calibredb", newFileName);
    Unchecked.run(() -> Files.copy(fromPath,
        Paths.get(target),
        StandardCopyOption.REPLACE_EXISTING));
    tempCopiedCalibreDb = new File(target);
    return tempCopiedCalibreDb;
  }

}
