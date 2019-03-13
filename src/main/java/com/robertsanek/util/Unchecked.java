package com.robertsanek.util;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class Unchecked {

  public static <A, B> Function<A, B> function(ThrowingFunction<A, B, Exception> function) {
    return string -> {
      try {
        return function.apply(string);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    };
  }

  public static <V> V get(Callable<V> callable) {
    try {
      return callable.call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <V> void run(Callable<V> callable) {
    try {
      callable.call();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @FunctionalInterface
  public interface ThrowingFunction<T, R, E extends Exception> {

    R apply(T t) throws E;
  }

}
