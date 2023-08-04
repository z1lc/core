package com.robertsanek.data.etl.local.habitica;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.robertsanek.data.etl.DoNotRun;
import com.robertsanek.data.etl.local.habitica.jsonentities.JsonTask;
import com.robertsanek.util.DateTimeUtils;

@DoNotRun(explanation = "no longer use Habitica")
public class NewHistoryEtl extends HabiticaEtl<History> {

  @Override
  public List<History> getObjects() {
    List<JsonTask> jsonObjects = getJsonObjects();
    return jsonObjects.stream()
        .flatMap(task -> {
          List<com.robertsanek.data.etl.local.habitica.jsonentities.History> historyList = task.getHistory();
          List<History> thisList = new ArrayList<>();
          for (int i = 1; i < historyList.size(); i++) {
            com.robertsanek.data.etl.local.habitica.jsonentities.History thisHistory = historyList.get(i);
            increaseOrDecrease(
                thisHistory.getValue(),
                historyList.get(i - 1).getValue())
                .ifPresent(isIncrease -> {
                  thisList.add(History.HistoryBuilder.aHistory()
                      .withId(UUID.randomUUID().toString())
                      .withTaskId(task.getId())
                      .withCompleted(isIncrease)
                      .withDate(DateTimeUtils.toZonedDateTime(Instant.ofEpochMilli(thisHistory.getDate())))
                      .build());
                });
          }
          return thisList.stream();
        })
        .toList();
  }

  static Optional<Boolean> increaseOrDecrease(double yesterday, double today) {
    double diff = today - yesterday;
    if (diff <= 10e-4 && diff >= -10e-4) {
      return Optional.empty();
    } else if (diff < 0) {
      return Optional.of(Boolean.TRUE);
    } else {
      return Optional.of(Boolean.FALSE);
    }
  }
}
