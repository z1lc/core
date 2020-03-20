package com.robertsanek.data.derived.anki;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Maps;

public class BacklogByDayDeriverTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<BacklogForDay> objects = new BacklogByDayDeriver().getObjects();
    objects.stream()
        .sorted(Comparator.comparing(o -> o.date))
        .forEach(d -> System.out.println(d.date + "\t" + d.cardsInBacklog));
  }

  @Test
  public void incrementBacklogsInMap() {
    Map<LocalDate, Long> map = Maps.newHashMap();
    BacklogByDayDeriver.incrementBacklogsInMap(map, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 5));
    BacklogByDayDeriver.incrementBacklogsInMap(map, LocalDate.of(2020, 1, 3), LocalDate.of(2020, 1, 5));
    assertEquals(Map.of(
        LocalDate.of(2020, 1, 1), 1L,
        LocalDate.of(2020, 1, 2), 1L,
        LocalDate.of(2020, 1, 3), 2L,
        LocalDate.of(2020, 1, 4), 2L
    ), map);
  }
}