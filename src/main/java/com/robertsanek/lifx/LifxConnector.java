package com.robertsanek.lifx;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.robertsanek.lifx.jsonentities.Scene;
import com.robertsanek.lifx.jsonentities.SceneSelectionResult;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;

public class LifxConnector {

  private static final String LIFX_ACCESS_TOKEN = CommonProvider.getSecret(SecretType.LIFX_ACCESS_TOKEN);
  @Inject ObjectMapper mapper;
  private static final Duration SWITCH_BETWEEN_SCENE_DURATION = Duration.ofMinutes(5);

  boolean triggerCoreDay() {
    return triggerScene(Optional.ofNullable(getSceneNameMap().get("Core Day")).orElseThrow().getUuid());
  }

  boolean triggerEarlyNight() {
    return allLightsAreOnRightNow() &&
        triggerScene(Optional.ofNullable(getSceneNameMap().get("Early Night")).orElseThrow().getUuid());
  }

  boolean triggerCoreNight() {
    return allLightsAreOnRightNow() &&
        triggerScene(Optional.ofNullable(getSceneNameMap().get("Core Night")).orElseThrow().getUuid());
  }

  boolean triggerBreathe() {
    String response = CommonProvider.retrying().get(() -> Request
        .Post("https://api.lifx.com/v1/lights/all/effects/breathe")
        .setHeader("Authorization", String.format("Bearer %s", LIFX_ACCESS_TOKEN))
        .body(EntityBuilder.create()
            .setParameters(
                new BasicNameValuePair("color", "#800080"),
                new BasicNameValuePair("period", "0.5"),
                new BasicNameValuePair("cycles", "10"),
                new BasicNameValuePair("persist", "false"),
                new BasicNameValuePair("power_on", "false"),
                new BasicNameValuePair("peak", "0.8"))
            .build())
        .execute()
        .returnContent()
        .asString());
    return responseWasSuccess(response);
  }

  private boolean allLightsAreOnRightNow() {
    return StreamSupport.stream(
        new JsonParser().parse(CommonProvider.retrying().get(() -> Request
            .Get("https://api.lifx.com/v1/lights/all")
            .setHeader("Authorization", String.format("Bearer %s", LIFX_ACCESS_TOKEN))
            .execute()
            .returnContent()
            .asString()))
            .getAsJsonArray()
            .spliterator(), false)
        .allMatch(jsonElement -> jsonElement.getAsJsonObject().get("power").getAsString().equals("on"));
  }

  private Map<String, SceneInfo> getSceneNameMap() {
    String scenes = CommonProvider.retrying()
        .get(() -> Request
            .Get("https://api.lifx.com/v1/scenes")
            .setHeader("Authorization", String.format("Bearer %s", LIFX_ACCESS_TOKEN))
            .execute()
            .returnContent()
            .asString());
    return Arrays.stream(Unchecked.get(() -> mapper.readValue(scenes, Scene[].class)))
        .collect(Collectors.toMap(Scene::getName, scene -> new SceneInfo(scene.getName(), scene.getUuid())));
  }

  private boolean triggerScene(String uuid) {
    String response = CommonProvider.retrying()
        .get(() -> Request
            .Put(String.format("https://api.lifx.com/v1/scenes/scene_id:%s/activate", uuid))
            .setHeader("Authorization", String.format("Bearer %s", LIFX_ACCESS_TOKEN))
            .body(EntityBuilder.create()
                .setParameters(
                    new BasicNameValuePair("duration", String.valueOf(SWITCH_BETWEEN_SCENE_DURATION.getSeconds())))
                .build())
            .execute()
            .returnContent()
            .asString());
    return responseWasSuccess(response);
  }

  private boolean responseWasSuccess(String response) {
    String dataArrAsString = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("results").toString();
    return Arrays.stream(Unchecked.get(() -> mapper.readValue(dataArrAsString, SceneSelectionResult[].class)))
        .allMatch(result -> result.getStatus().equals("ok"));
  }

}
