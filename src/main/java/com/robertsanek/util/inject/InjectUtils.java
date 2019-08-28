package com.robertsanek.util.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectUtils {

  public static <T> T inject(Class<T> type) {
    Injector injector = Guice.createInjector(new ParentModule());
    return injector.getInstance(type);
  }

}
