package com.robertsanek.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Lists {

  public static List<String> quoteListItems(List<String> list) {
    return list.stream()
        .map(item -> String.format("\"%s\"", item))
        .toList();
  }

  public static String quoteListItemsAndJoinWithComma(List<String> list) {
    return String.join(",", quoteListItems(list));
  }

  public static String quoteListItemsAndJoinWithComma(List<String> list, int limit) {
    return StringUtils.left(quoteListItemsAndJoinWithComma(list), limit);
  }

}
