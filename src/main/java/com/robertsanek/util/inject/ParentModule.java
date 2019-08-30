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
import com.google.inject.name.Named;
import com.robertsanek.data.etl.remote.scrape.toodledo.HabitEtl;
import com.robertsanek.data.etl.remote.scrape.toodledo.HabitRepEtl;
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

  @Provides
  @Named("will")
  HabitEtl habitEtlWill(Injector injector) {
    SecretProvider secretProvider = injector.getInstance(SecretProvider.class);
    HabitEtl habitEtl = new HabitEtl() {
      @Override
      public String getUsername() {
        return secretProvider.getSecret(SecretType.TOODLEDO_WILL_USERNAME);
      }

      @Override
      public String getPassword() {
        return secretProvider.getSecret(SecretType.TOODLEDO_WILL_PASSWORD);
      }
    };
    injector.injectMembers(habitEtl);
    return habitEtl;
  }

  @Provides
  @Named("will")
  HabitRepEtl habitRepEtlWill(Injector injector) {
    SecretProvider secretProvider = injector.getInstance(SecretProvider.class);
    HabitRepEtl habitRepEtl = new HabitRepEtl() {
      @Override
      public String getUsername() {
        return secretProvider.getSecret(SecretType.TOODLEDO_WILL_USERNAME);
      }

      @Override
      public String getPassword() {
        return secretProvider.getSecret(SecretType.TOODLEDO_WILL_PASSWORD);
      }
    };
    injector.injectMembers(habitRepEtl);
    return habitRepEtl;
  }

}
