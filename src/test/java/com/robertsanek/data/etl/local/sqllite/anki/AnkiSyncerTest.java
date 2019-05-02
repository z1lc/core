package com.robertsanek.data.etl.local.sqllite.anki;

import org.junit.Ignore;
import org.junit.Test;

public class AnkiSyncerTest {

  @Test
  @Ignore("integration")
  public void testSync() {
    AnkiSyncer.syncLocalCollectionIfOutOfDate("z1lc");
    AnkiSyncer.syncLocalCollectionIfOutOfDate("will");
  }
}
