package com.barnacle.lifting;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum HeavyOrEndurance {
  ENDURANCE(6),
  HEAVY(5);

  private final int numSets;
  private static Map<Integer, HeavyOrEndurance> map;

  static {
    map = Arrays.stream(HeavyOrEndurance.values())
        .collect(Collectors.toMap(HeavyOrEndurance::getNumSets, Function.identity()));
  }

  HeavyOrEndurance(int numSets) {
    this.numSets = numSets;
  }

  public int getNumSets() {
    return numSets;
  }

  public static HeavyOrEndurance fromSetCount(int numSets) {
    return Optional.ofNullable(map.get(numSets)).orElseThrow(
        () -> new RuntimeException(String.format("Can't detect heavy or endurance day based on %d sets.", numSets)));
  }

}
