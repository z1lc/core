package com.robertsanek.data.etl.local.sqllite.anki;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robertsanek.data.etl.local.sqllite.anki.connect.AnkiConnectUtils;
import com.robertsanek.data.quality.anki.DataQualityBase;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.NotificationSender;
import com.robertsanek.util.inject.InjectUtils;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class AnkiSyncer {

  private static final Duration DO_NOT_SYNC_IF_WITHIN = Duration.ofMinutes(15);
  private static final ObjectMapper objectMapper = InjectUtils.inject(ObjectMapper.class);
  private static final AnkiConnectUtils ankiConnectUtils = InjectUtils.inject(AnkiConnectUtils.class);
  private static Log log = Logs.getLog(AnkiSyncer.class);
  private static final Map<String, ZonedDateTime> lastLoggedMap = new HashMap<>();

  //TODO: make these methods non-static. hard because of reliance in static{} in DataQualityBase.
  public static synchronized boolean syncLocalCollectionIfOutOfDate(String profileToSync) {
    if (CrossPlatformUtils.isRunningInsideDocker()) {
      return true;
    }
    File lastSyncFile = new File(String.format("%sout/anki/last_sync_%s_%s.zoneddatetime",
        CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow(),
        getDeviceName(),
        profileToSync));
    ZonedDateTime lastSuccessfulSync = ZonedDateTime.now();
    ZonedDateTime thisSyncTime = ZonedDateTime.now();
    try {
      if (lastSyncFile.exists()) {
        AnkiSyncResult ankiSyncResult = objectMapper.readValue(lastSyncFile, AnkiSyncResult.class);
        if (!ankiSyncResult.getSucceeded()) {
          log.warn("Last sync of profile '%s' on device '%s' did not succeed.", profileToSync, getDeviceName());
        }
        lastSuccessfulSync = ankiSyncResult.getLastSuccessfulSync();
        if (lastSuccessfulSync.isBefore(ZonedDateTime.now().minus(DO_NOT_SYNC_IF_WITHIN))) {
          log.info("Will need to sync profile '%s'; last sync on device '%s' was %s minutes ago.",
              profileToSync, getDeviceName(), ChronoUnit.MINUTES.between(lastSuccessfulSync, ZonedDateTime.now()));
        } else {
          if (ChronoUnit.SECONDS.between(
              lastLoggedMap.getOrDefault(profileToSync, ZonedDateTime.now().minusYears(10)),
              ZonedDateTime.now()) > 30) {
            log.info("No need to sync profile '%s'; last sync on device '%s' is within %s minutes.",
                profileToSync, getDeviceName(), DO_NOT_SYNC_IF_WITHIN.toMinutes());
            lastLoggedMap.put(profileToSync, ZonedDateTime.now());
          }
          return true;
        }
      } else {
        log.info("Didn't find file at '%s'. Will create after Anki sync.", lastSyncFile.getAbsolutePath());
      }
      if (ankiConnectUtils.loadProfile(profileToSync) && ankiConnectUtils.triggerSync()) {
        writeFile(lastSyncFile, thisSyncTime, thisSyncTime);
        DataQualityBase.recomputeCachedFields();
        return true;
      }

    } catch (Exception e) {
      log.error("Exception during sync attempt:");
      log.error(e);
      try {
        writeFile(lastSyncFile, lastSuccessfulSync, thisSyncTime);
      } catch (IOException ignored) {
      }
    }

    if (ChronoUnit.HOURS.between(lastSuccessfulSync, thisSyncTime) > 24) {
      InjectUtils.inject(NotificationSender.class).sendNotificationDefault("Anki Sync broken for more than 24 hours!",
          String.format("Anki has been unable to sync on device '%s' since %s. Please investigate.",
              getDeviceName(), lastSuccessfulSync));
    }

    return false;
  }

  private static String getDeviceName() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      log.warn("Failed to get local hostname. Will use 'unknown'.");
      return "unknown";
    }
  }

  private static void writeFile(File lastSyncFile,
                                ZonedDateTime lastSuccessfulSync,
                                ZonedDateTime lastSyncAttempt) throws IOException {
    log.info("Writing ZonedDateTime to file '%s'.", lastSyncFile.getAbsolutePath());
    objectMapper.writeValue(lastSyncFile, new AnkiSyncResult(lastSuccessfulSync, lastSyncAttempt));
  }

}
