package com.robertsanek.util.inject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.sheigutn.pushbullet.Pushbullet;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.SecretType;

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

  @Provides
  @Singleton
  XmlMapper xmlMapper() {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    return xmlMapper;
  }

  @Provides
  @Singleton
  Pushbullet pushbullet(Injector injector) {
    return new Pushbullet(injector.getInstance(SecretProvider.class).getSecret(SecretType.PUSHBULLET_ACCESS_TOKEN));
  }

}
