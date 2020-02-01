package com.robertsanek.util.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectUtils {

  public static <T> T inject(Class<T> type) {
    Injector injector = getInjector();
    return injector.getInstance(type);
  }

  public static Injector getInjector() {
    return Guice.createInjector(new ParentModule());
  }

}
