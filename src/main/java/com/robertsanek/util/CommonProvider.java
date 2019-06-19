package com.robertsanek.util;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.robertsanek.util.platform.CrossPlatformUtils;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.FailsafeExecutor;
import net.jodah.failsafe.RetryPolicy;

public class CommonProvider {

  private static final Log log = Logs.getLog(CommonProvider.class);
  private static final String EMAIL_ADDRESS = "rsanek@gmail.com";
  private static final Map<SecretType, Secret> secrets;
  private static final RequestConfig.Builder globalConfig = RequestConfig.custom()
      .setCookieSpec(CookieSpecs.STANDARD);
  private static final Duration DEFAULT_HTTP_REQUEST_TIMEOUT = Duration.ofSeconds(30);
  private static final CookieStore DEFAULT_COOKIE_STORE = new BasicCookieStore();

  static {
    String secretsLocation = Optional.ofNullable(System.getenv("Z1LC_CORE_SECRETS_FILE_LOCATION"))
        .orElse(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "secrets.json");
    File secretsFile = new File(secretsLocation);
    if (secretsFile.exists()) {
      secrets = Arrays.stream(Unchecked.get(() -> getObjectMapper()
          .readValue(secretsFile, Secret[].class)))
          .collect(Collectors.toMap(Secret::getType, Function.identity()));
    } else {
      secrets = Arrays.stream(SecretType.values())
          .map(secretType -> new Secret(secretType, "", Maps.newHashMap()))
          .collect(Collectors.toMap(Secret::getType, Function.identity()));
      log.error("No file was found at '%s'.", secretsFile.toString());
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

  public static <R> RetryPolicy<R> defaultRetryPolicy() {
    return new RetryPolicy<R>()
        .handle(Throwable.class)
        .withBackoff(10, 60, ChronoUnit.SECONDS)
        .withMaxRetries(2);
  }

  public static <R> FailsafeExecutor<R> retrying() {
    return Failsafe.with(defaultRetryPolicy());
  }

  public static ImmutableSet<String> getCommonNames() {
    String rawNames = Unchecked.get(() -> Resources.toString(Resources.getResource(
        "com/robertsanek/data/quality/anki/files/common_first_names.txt"), Charsets.UTF_8));
    return ImmutableSet.copyOf(Arrays.stream(rawNames.split("\r\n"))
        .map(name -> StringUtils.capitalize(name.toLowerCase()))
        .collect(Collectors.toSet()));
  }

}
