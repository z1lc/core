package com.robertsanek.lifx;

public class SceneInfo {

  private String name;
  private String uuid;
  private Boolean powered;

  public SceneInfo(String name, String uuid) {
    this.name = name;
    this.uuid = uuid;
  }

  public String getUuid() {
    return uuid;
  }

  public Boolean isPowered() {
    return powered;
  }
}
