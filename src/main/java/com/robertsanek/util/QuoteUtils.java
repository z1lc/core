package com.robertsanek.util;

public class QuoteUtils {

  public static String singleQuote(Object s) {
    return quote(s, "'");
  }

  public static String doubleQuote(Object s) {
    return quote(s, "\"");
  }

  private static String quote(Object s, String quoteChar) {
    return s == null ? "NULL" : String.format("%s%s%s", quoteChar, s.toString(), quoteChar);
  }

}
