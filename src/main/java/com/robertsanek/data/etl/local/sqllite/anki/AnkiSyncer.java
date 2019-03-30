package com.robertsanek.data.etl.local.sqllite.anki;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.NotificationSender;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class AnkiSyncer {

  private static final Duration DO_NOT_SYNC_IF_WITHIN = Duration.ofMinutes(15);
  private static final Duration WAIT_TIME_BETWEEN_STEPS = Duration.ofSeconds(5);
  private static final int ANKI_CONNECT_VERSION = 6;
  private static final ObjectMapper objectMapper = CommonProvider.getObjectMapper();
  private static Log log = Logs.getLog(AnkiSyncer.class);
  private static String ANKI_CONNECT_HOST = "localhost";
  private static int ANKI_CONNECT_PORT = 8765;
  private static String ANK_CONNECT_PRETTY_URL = String.format("%s:%d", ANKI_CONNECT_HOST, ANKI_CONNECT_PORT);
  private static final URIBuilder ANKI_CONNECT_BASE_URI = new URIBuilder()
      .setScheme("http")
      .setHost(ANK_CONNECT_PRETTY_URL);
  private static final Map<String, ZonedDateTime> lastLoggedMap = Maps.newHashMap();

  public static synchronized boolean syncLocalCollectionIfOutOfDate(String profileToSync) {
    File lastSyncFile = new File(String.format("%sout/anki/last_sync_%s.zoneddatetime",
        CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow(), profileToSync));
    ZonedDateTime lastSuccessfulSync = ZonedDateTime.now();
    ZonedDateTime thisSyncTime = ZonedDateTime.now();
    try {
      if (lastSyncFile.exists()) {
        AnkiSyncResult ankiSyncResult = objectMapper.readValue(lastSyncFile, AnkiSyncResult.class);
        if (!ankiSyncResult.getSucceeded()) {
          log.warn("Last sync of profile '%s' did not succeed.", profileToSync);
        }
        lastSuccessfulSync = ankiSyncResult.getLastSuccessfulSync();
        if (lastSuccessfulSync.isBefore(ZonedDateTime.now().minus(DO_NOT_SYNC_IF_WITHIN))) {
          log.info("Will need to sync profile '%s'; last sync was %s minutes ago.",
              profileToSync, ChronoUnit.MINUTES.between(lastSuccessfulSync, ZonedDateTime.now()));
        } else {
          if (ChronoUnit.SECONDS.between(
              lastLoggedMap.getOrDefault(profileToSync, ZonedDateTime.now().minusYears(10)),
              ZonedDateTime.now()) > 30) {
            log.info("No need to sync profile '%s'; last sync is within %s minutes.",
                profileToSync, DO_NOT_SYNC_IF_WITHIN.toMinutes());
            lastLoggedMap.put(profileToSync, ZonedDateTime.now());
          }
          return true;
        }
      } else {
        log.info("Didn't find file at '%s'. Will create after Anki sync.", lastSyncFile.getAbsolutePath());
      }
      if (getAnkiExecutablePath().isPresent()) {
        File ankiExecutable = new File(getAnkiExecutablePath().orElseThrow());
        log.info("Opening Anki...");
        Desktop.getDesktop().open(ankiExecutable);
        Thread.sleep(WAIT_TIME_BETWEEN_STEPS.toMillis());
      } else {
        log.info("Anki application path was not provided so application could not be started. " +
            "Assuming Anki is open and attempting to connect anyway....");
      }
      URI ankiConnectUri = ANKI_CONNECT_BASE_URI.build();
      HttpPost changeProfilePost = new HttpPost(ankiConnectUri);
      changeProfilePost.setEntity(new ByteArrayEntity(
          String.format("{\"action\": \"loadProfile\", \"params\": {\"name\": \"%s\"}, \"version\": %s}",
              profileToSync, ANKI_CONNECT_VERSION)
              .getBytes(StandardCharsets.UTF_8)));
      log.info("Loading profile '%s'...", profileToSync);
      String profileResponse = EntityUtils.toString(
          CommonProvider.getHttpClient().execute(changeProfilePost).getEntity());
      Thread.sleep(WAIT_TIME_BETWEEN_STEPS.toMillis());
      if (profileResponse.contains("true")) {
        HttpPost syncPost = new HttpPost(ankiConnectUri);
        syncPost.setEntity(new ByteArrayEntity(
            String.format("{\"action\": \"sync\", \"version\": %s}", ANKI_CONNECT_VERSION)
                .getBytes(StandardCharsets.UTF_8)));
        log.info("Syncing profile '%s'...", profileToSync);
        String syncResponse = EntityUtils.toString(CommonProvider.getHttpClient().execute(syncPost).getEntity());
        Thread.sleep(WAIT_TIME_BETWEEN_STEPS.toMillis());
        if (syncResponse.equals("{\"result\": null, \"error\": null}")) {
          log.info("Successfully synced Anki for profile '%s'.", profileToSync);
          writeFile(lastSyncFile, thisSyncTime, thisSyncTime);
          return true;
        } else {
          log.info("Response from AnkiConnect did not match expected. Actual response: '%s'", syncResponse);
        }
      } else {
        log.info("Response from AnkiConnect did not match expected. Actual response: '%s'", profileResponse);
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
      NotificationSender.sendNotificationDefault("Anki Sync broken for more than 24 hours!",
          String.format("Anki has been unable to sync since %s. Please investigate.", lastSuccessfulSync));
    }

    return false;
  }

  private static void writeFile(File lastSyncFile, ZonedDateTime lastSuccessfulSync,
                                ZonedDateTime lastSyncAttempt) throws IOException {
    log.info("Writing ZonedDateTime to file '%s'.", lastSyncFile.getAbsolutePath());
    objectMapper.writeValue(lastSyncFile, new AnkiSyncResult(lastSuccessfulSync, lastSyncAttempt));
  }

  @VisibleForTesting
  static Optional<String> getAnkiExecutablePath() {
    return CrossPlatformUtils.getPlatform().getAnkiExecutable().map(Path::toString);
  }

}
