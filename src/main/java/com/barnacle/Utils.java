package com.barnacle;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

public class Utils {

  public static <T> List<Pair<User, T>> addUser(List<T> list, User user) {
    return list.stream()
        .map(element -> Pair.of(user, element))
        .collect(Collectors.toList());
  }

}
