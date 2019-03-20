package com.robertsanek.data.etl.local.sqllite.anki;

import static junit.framework.TestCase.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class AnkiSyncerTest {

  @Test
  @Ignore("integration")
  public void testSync() {
    AnkiSyncer.syncLocalCollectionIfOutOfDate("z1lc");
    AnkiSyncer.syncLocalCollectionIfOutOfDate("will");
  }

  @Test
  public void getAnkiExecutablePath() {
    assertEquals("C:\\Program Files\\Anki\\anki.exe", AnkiSyncer.getAnkiExecutablePath().orElseThrow());
  }
}