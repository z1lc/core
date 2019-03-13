package com.robertsanek.data.etl.local.sqllite.anki;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AnkiSyncResult {

  private ZonedDateTime lastSuccessfulSync;
  private ZonedDateTime lastSyncAttempt;

  public AnkiSyncResult() {
  }

  public AnkiSyncResult(ZonedDateTime lastSuccessfulSync, ZonedDateTime lastSyncAttempt) {
    this.lastSuccessfulSync = lastSuccessfulSync;
    this.lastSyncAttempt = lastSyncAttempt;
  }

  public ZonedDateTime getLastSyncAttempt() {
    return lastSyncAttempt;
  }

  public ZonedDateTime getLastSuccessfulSync() {
    return lastSuccessfulSync;
  }

  @JsonIgnore
  public Boolean getSucceeded() {
    return lastSyncAttempt.equals(lastSuccessfulSync);
  }
}
