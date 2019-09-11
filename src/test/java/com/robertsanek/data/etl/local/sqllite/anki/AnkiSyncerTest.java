package com.robertsanek.data.etl.local.sqllite.anki;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AnkiSyncerTest {

  @Test
  @Disabled("integration")
  public void testSync() {
    AnkiSyncer.syncLocalCollectionIfOutOfDate("z1lc");
    AnkiSyncer.syncLocalCollectionIfOutOfDate("will");
  }
}
