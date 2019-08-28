package com.robertsanek.util.inject;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ParentModule extends AbstractModule {

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  ObjectMapper objectMapper() {
    //https://github.com/FasterXML/jackson-modules-java8
    ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());
    objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    return objectMapper;
  }
}
