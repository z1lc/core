package com.robertsanek.util;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class CommonProvider {

  private static final String EMAIL_ADDRESS = "rsanek@gmail.com";
  private static final Map<SecretType, Secret> secrets;

  static {
    File secretsFile = new File(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "secrets.json");
    if (secretsFile.exists()) {
      secrets = Arrays.stream(Unchecked.get(() -> getObjectMapper()
          .readValue(secretsFile, Secret[].class)))
          .collect(Collectors.toMap(Secret::getType, Function.identity()));
    } else {
      throw new RuntimeException(String.format("No file was found at '%s'.", secretsFile.toString()));
    }
  }

  public static String getEmailAddress() {
    return EMAIL_ADDRESS;
  }

  //https://github.com/FasterXML/jackson-modules-java8
  public static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());
    objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    return objectMapper;
  }

  public static XmlMapper getXmlMapper() {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    return xmlMapper;
  }

  public static String getSecret(SecretType secretType) {
    Optional<Secret> maybeSecret = Optional.ofNullable(secrets.get(secretType));
    if (maybeSecret.isPresent()) {
      return maybeSecret.orElseThrow().getSecret();
    } else {
      throw new RuntimeException(String.format("secrets.json file has no mapping for SecretType '%s'.", secretType));
    }
  }

  public static CloseableHttpClient getHttpClient(RequestConfig config) {
    return HttpClients.custom()
        .setDefaultRequestConfig(config)
        .build();
  }

}
