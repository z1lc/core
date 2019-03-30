package com.robertsanek.util;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gargoylesoftware.htmlunit.WebClient;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class CommonProvider {

  private static final String EMAIL_ADDRESS = "rsanek@gmail.com";
  private static final Map<SecretType, Secret> secrets;
  private static final RequestConfig.Builder globalConfig = RequestConfig.custom()
      .setCookieSpec(CookieSpecs.STANDARD);
  private static final Duration DEFAULT_HTTP_REQUEST_TIMEOUT = Duration.ofSeconds(30);
  private static final CookieStore DEFAULT_COOKIE_STORE = new BasicCookieStore();

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

  //https://stackoverflow.com/a/25591540
  public static WebClient getHtmlUnitWebClient() {
    WebClient webClient = new WebClient();
    webClient.getOptions().setThrowExceptionOnScriptError(false);
    return webClient;
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

  public static CloseableHttpClient getHttpClient() {
    return getHttpClient(Optional.empty(), Optional.empty(), Optional.empty());
  }

  public static CloseableHttpClient getHttpClient(CookieStore cookieStore) {
    return getHttpClient(Optional.empty(), Optional.ofNullable(cookieStore), Optional.empty());
  }

  private static CloseableHttpClient getHttpClient(Optional<Duration> maybeTimeout,
                                                   Optional<CookieStore> maybeCookieStore,
                                                   Optional<RequestConfig> maybeRequestConfig) {
    Duration timeout = maybeTimeout.orElse(DEFAULT_HTTP_REQUEST_TIMEOUT);
    RequestConfig config = maybeRequestConfig.orElse(globalConfig
        .setConnectionRequestTimeout((int) timeout.toMillis())
        .setConnectTimeout((int) timeout.toMillis())
        .setSocketTimeout((int) timeout.toMillis())
        .build());
    CookieStore cookieStore = maybeCookieStore.orElse(DEFAULT_COOKIE_STORE);

    return HttpClients.custom()
        .setDefaultRequestConfig(config)
        .setDefaultCookieStore(cookieStore)
        .build();
  }

}
