package com.robertsanek.data.etl.local.sqllite.anki.connect;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.robertsanek.data.etl.local.sqllite.anki.connect.jsonschema.CardsInfoResult;
import com.robertsanek.data.quality.anki.DataQualityBase;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class AnkiConnectUtils {

  private static String ANKI_CONNECT_HOST = "localhost";
  private static int ANKI_CONNECT_PORT = 8765;
  private static String ANK_CONNECT_PRETTY_URL = String.format("%s:%d", ANKI_CONNECT_HOST, ANKI_CONNECT_PORT);
  private static final URIBuilder ANKI_CONNECT_BASE_URI = new URIBuilder()
      .setScheme("http")
      .setHost(ANK_CONNECT_PRETTY_URL);
  private static final int ANKI_CONNECT_VERSION = 6;
  private static Log log = Logs.getLog(AnkiConnectUtils.class);
  private static ZonedDateTime lastAnkiOpenTime = ZonedDateTime.now().minusYears(10);

  @Inject ObjectMapper mapper;

  public boolean loadProfile(String profileToLoad) {
    openAnki();
    HttpPost changeProfilePost = new HttpPost(getUri());
    changeProfilePost.setEntity(new ByteArrayEntity(
        String.format("{\"action\": \"loadProfile\", \"params\": {\"name\": \"%s\"}, \"version\": %s}",
            profileToLoad, ANKI_CONNECT_VERSION).getBytes(StandardCharsets.UTF_8)));
    log.info("Loading profile '%s'...", profileToLoad);
    String profileResponse = Unchecked.get(() -> EntityUtils.toString(
        CommonProvider.getHttpClient().execute(changeProfilePost).getEntity()));
    if (!profileResponse.contains("true")) {
      log.info("Response from AnkiConnect did not match expected. Actual response: '%s'", profileResponse);
      return false;
    } else {
      log.info("Successfully loaded profile '%s'.", profileToLoad);
      return true;
    }
  }

  public boolean triggerSync() {
    openAnki();
    HttpPost syncPost = new HttpPost(getUri());
    syncPost.setEntity(new ByteArrayEntity(
        String.format("{\"action\": \"sync\", \"version\": %s}", ANKI_CONNECT_VERSION)
            .getBytes(StandardCharsets.UTF_8)));
    log.info("Syncing current profile...");
    String syncResponse =
        Unchecked.get(() -> EntityUtils.toString(CommonProvider.getHttpClient().execute(syncPost).getEntity()));
    try {
      Thread.sleep(Duration.ofSeconds(5).toMillis());
    } catch (InterruptedException ignored) {

    }
    if (!syncResponse.equals("{\"result\": null, \"error\": null}")) {
      log.info("Response from AnkiConnect did not match expected. Actual response: '%s'", syncResponse);
      return false;
    } else {
      log.info("Successfully synced current profile.");
      return true;
    }
  }

  //methods that use the browser will not return the response if the browser isn't already open. not ideal.
  private boolean openBrowser() {
    HttpPost openBrowser = new HttpPost(getUri());
    openBrowser.setEntity(new ByteArrayEntity(("{\n" +
        "    \"action\": \"guiBrowse\",\n" +
        "    \"version\": 6\n" +
        "}").getBytes(StandardCharsets.UTF_8)));
    String updateResponse =
        Unchecked.get(() -> EntityUtils.toString(CommonProvider.getHttpClient().execute(openBrowser).getEntity()));
    if (validateEmptyListResponse(updateResponse)) {
      Unchecked.run(() -> {
        Thread.sleep(Duration.ofSeconds(5).toMillis());
        return null;
      });
      return true;
    }
    return false;
  }

  public Map<String, String> getFieldsForNote(Long noteId) {
    HttpPost notesInfo = new HttpPost(getUri());
    notesInfo.setEntity(new ByteArrayEntity(
        String.format("{\n" +
            "    \"action\": \"notesInfo\",\n" +
            "    \"version\": 6,\n" +
            "    \"params\": {\n" +
            "        \"notes\": [%s]\n" +
            "    }\n" +
            "}", noteId)
            .getBytes(StandardCharsets.UTF_8)));
    String updateResponse =
        Unchecked.get(() -> EntityUtils.toString(CommonProvider.getHttpClient().execute(notesInfo).getEntity()));
    JsonObject fields =
        new JsonParser().parse(updateResponse).getAsJsonObject().getAsJsonArray("result").get(0).getAsJsonObject()
            .getAsJsonObject("fields");
    return fields.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue().getAsJsonObject().get("value").getAsString()));
  }

  public List<Long> getNoteIdsForSearch(String search, String profile) {
    loadProfile(profile);
    HttpPost searchPost = new HttpPost(getUri());
    searchPost.setEntity(new ByteArrayEntity(
        String.format("{\n" +
            "    \"action\": \"findNotes\",\n" +
            "    \"version\": 6,\n" +
            "    \"params\": {\n" +
            "        \"query\": \"%s\"\n" +
            "    }\n" +
            "}", StringEscapeUtils.escapeJson(search))
            .getBytes(StandardCharsets.UTF_8)));
    String updateResponse =
        Unchecked.get(() -> EntityUtils.toString(CommonProvider.getHttpClient().execute(searchPost).getEntity()));
    JsonArray result = new JsonParser().parse(updateResponse).getAsJsonObject().getAsJsonArray("result");
    return StreamSupport.stream(result.spliterator(), false)
        .map(JsonElement::getAsLong)
        .collect(Collectors.toList());
  }

  public boolean updateNoteFields(Long noteId, Map<String, String> fieldsToUpdate) {
    String updateString = fieldsToUpdate.entrySet().stream()
        .map(entry -> "\"" + entry.getKey() + "\": \"" + StringEscapeUtils.escapeJson(entry.getValue()) + "\"")
        .collect(Collectors.joining(","));
    HttpPost updateNote = new HttpPost(getUri());
    updateNote.setEntity(new ByteArrayEntity(
        String.format("{\n" +
            "    \"action\": \"updateNoteFields\",\n" +
            "    \"version\": 6,\n" +
            "    \"params\": {\n" +
            "        \"note\": {\n" +
            "            \"id\": %s,\n" +
            "            \"fields\": {\n" +
            "                %s" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}", noteId, updateString)
            .getBytes(StandardCharsets.UTF_8)));
    String updateResponse =
        Unchecked.get(() -> EntityUtils.toString(CommonProvider.getHttpClient().execute(updateNote).getEntity()));
    return validateNullResponse(updateResponse);
  }

  public boolean updatePersonNoteImage(Long noteId, String oldName, String newName) {
    String currentImageField = getPersonNoteImage(noteId);
    HttpPost updateNote = new HttpPost(getUri());
    updateNote.setEntity(new ByteArrayEntity(
        String.format("{\n" +
            "    \"action\": \"updateNoteFields\",\n" +
            "    \"version\": 6,\n" +
            "    \"params\": {\n" +
            "        \"note\": {\n" +
            "            \"id\": %s,\n" +
            "            \"fields\": {\n" +
            "                \"⭐Image \uD83D\uDDBC️\": \"%s\"\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}", noteId, StringEscapeUtils.escapeJson(currentImageField.replace(oldName, newName)))
            .getBytes(StandardCharsets.UTF_8)));
    String updateResponse =
        Unchecked.get(() -> EntityUtils.toString(CommonProvider.getHttpClient().execute(updateNote).getEntity()));
    if (!validateNullResponse(updateResponse)) {
      log.info("Response from AnkiConnect did not match expected. Actual response: '%s'", updateResponse);
      return false;
    } else {
      log.info("Successfully updated image source from '%s' to '%s'.", oldName, newName);
      return true;
    }
  }

  private boolean validateNullResponse(String response) {
    return response.equals("{\"result\": null, \"error\": null}");
  }

  private boolean validateEmptyListResponse(String response) {
    return response.equals("{\"result\": [], \"error\": null}");
  }

  @SuppressWarnings("unchecked")
  public String getPersonNoteImage(Long noteId) {
    Long firstCardId = DataQualityBase.getCardsByNoteId().get(noteId).get(0).getId();
    CardsInfoResult cardInfo = getCardInfo(firstCardId);
    return ((HashMap<String, String>) cardInfo.getFields().getAdditionalProperties().get("⭐Image 🖼️")).get("value");
  }

  public CardsInfoResult getCardInfo(Long cardId) {
    HttpPost syncPost = new HttpPost(getUri());
    syncPost.setEntity(new ByteArrayEntity(
        String.format("{\n" +
            "    \"action\": \"cardsInfo\",\n" +
            "    \"version\": 6,\n" +
            "    \"params\": {\n" +
            "        \"cards\": [%s]\n" +
            "    }\n" +
            "}", cardId)
            .getBytes(StandardCharsets.UTF_8)));
    JsonObject obj =
        new JsonParser()
            .parse(
                Unchecked.get(() -> EntityUtils.toString(CommonProvider.getHttpClient().execute(syncPost).getEntity())))
            .getAsJsonObject();
    String dataSources = obj.getAsJsonArray("result").toString();
    CardsInfoResult[] cardsInfoResults = Unchecked.get(() -> mapper.readValue(dataSources, CardsInfoResult[].class));
    return cardsInfoResults[0];
  }

  private void openAnki() {
    if (ChronoUnit.MINUTES.between(lastAnkiOpenTime, ZonedDateTime.now()) >= 15) {
      if (getAnkiExecutablePath().isPresent()) {
        lastAnkiOpenTime = ZonedDateTime.now();
        File ankiExecutable = new File(getAnkiExecutablePath().orElseThrow());
        log.info("Opening Anki...");
        try {
          Desktop.getDesktop().open(ankiExecutable);
          Thread.sleep(Duration.ofSeconds(5).toMillis());
        } catch (IOException | InterruptedException ignored) {
        }
      } else {
        log.info("Anki application path was not provided so application could not be started. " +
            "Assuming Anki is open and attempting to connect anyway....");
      }
    }
  }

  private URI getUri() {
    return Unchecked.get(ANKI_CONNECT_BASE_URI::build);
  }

  @VisibleForTesting
  Optional<String> getAnkiExecutablePath() {
    return CrossPlatformUtils.getPlatform().getAnkiExecutable().map(Path::toString);
  }

}
