package com.robertsanek.data;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

class EnsureAllTablesHaveRecentDataTest {

  @Test
  @Disabled("integration")
  void integration() {
    InjectUtils.inject(EnsureAllTablesHaveRecentData.class).ensure();
  }

}