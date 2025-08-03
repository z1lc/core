package com.robertsanek.util;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.htmlunit.WebClient;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.FailsafeExecutor;
import net.jodah.failsafe.RetryPolicy;

public class CommonProvider {

  private static final String EMAIL_ADDRESS = "rsanek@gmail.com";
  private static final RequestConfig.Builder globalConfig = RequestConfig.custom()
      .setCookieSpec(CookieSpecs.STANDARD);
  private static final Duration DEFAULT_HTTP_REQUEST_TIMEOUT = Duration.ofSeconds(30);
  private static final CookieStore DEFAULT_COOKIE_STORE = new BasicCookieStore();

  public static String getEmailAddress() {
    return EMAIL_ADDRESS;
  }

  //https://stackoverflow.com/a/25591540
  public static WebClient getHtmlUnitWebClient() {
    WebClient webClient = new WebClient();
    webClient.getOptions().setThrowExceptionOnScriptError(false);
    return webClient;
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
        "com/robertsanek/data/quality/anki/files/common_first_names.txt"), StandardCharsets.UTF_8));
    return ImmutableSet.copyOf(Arrays.stream(rawNames.split("\r\n"))
        .map(name -> StringUtils.capitalize(name.toLowerCase()))
        .collect(Collectors.toSet()));
  }

}
