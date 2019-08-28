package com.robertsanek.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.robertsanek.ParentModule;

public class InjectUtils {

  public static <T> T inject(Class<T> type) {
    Injector injector = Guice.createInjector(new ParentModule());
    return injector.getInstance(type);
  }

}
