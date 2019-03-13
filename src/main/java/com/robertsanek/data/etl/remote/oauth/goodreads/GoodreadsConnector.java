package com.robertsanek.data.etl.remote.oauth.goodreads;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.google.common.collect.Iterables;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

public class GoodreadsConnector {

  private static final Log log = Logs.getLog(GoodreadsConnector.class);
  private static final String GOODREADS_ROOT =
      CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/goodreads/";
  private static ObjectMapper mapper = CommonProvider.getObjectMapper();
  private final String secretState = RandomStringUtils.randomAlphanumeric(13).toLowerCase();
  private final OAuth10aService service = new ServiceBuilder(CommonProvider.getSecret(SecretType.GOODREADS_API_KEY))
      .apiSecret(CommonProvider.getSecret(SecretType.GOODREADS_API_SECRET))
      .build(new GoodreadsApi10a());

  private OAuth1RequestToken getRequestToken() throws InterruptedException, ExecutionException, IOException {
    return service.getRequestToken();
  }

  public List<Object> getTasks() throws Exception {
    if (maybeGetAccessToken().isEmpty()) {
      OAuth1RequestToken requestToken = getRequestToken();
      final String authorizationUrl = service.getAuthorizationUrl(requestToken);
      log.info("Go to " + authorizationUrl);
      Thread.sleep(10000);
      OAuth1AccessToken accessToken = service.getAccessToken(requestToken, "1");
      saveAccessToken(accessToken.getToken());
      saveAccessTokenSecret(accessToken.getTokenSecret());
    }
    OAuth1AccessToken accessToken =
        new OAuth1AccessToken(maybeGetAccessToken().orElseThrow(), maybeGetAccessTokenSecret().orElseThrow());
    OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.goodreads.com/api/auth_user");
    service.signRequest(accessToken, request);
    Response execute = service.execute(request);
    return null;
    //    JsonTask[] jsonTasks = Unchecked.get(() -> mapper.readValue(response.getBody(), JsonTask[].class));
    //    return Lists.newArrayList(jsonTasks).subList(1, jsonTasks.length);
  }

  public Optional<String> maybeGetAccessToken() {
    return maybeGet("access.token");
  }

  public Optional<String> maybeGetAccessTokenSecret() {
    return maybeGet("access.token.secret");
  }

  private Optional<String> maybeGet(String fileName) {
    if (new File(GOODREADS_ROOT + fileName).isFile()) {
      return Optional.of(
          Iterables.getOnlyElement(
              Unchecked.get(() ->
                  Files.readAllLines(Paths.get(GOODREADS_ROOT + fileName)))));
    }
    return Optional.empty();
  }

  public String saveAccessTokenSecret(String accessTokenSecret) {
    log.info("Saving access token secret %s.", accessTokenSecret);
    return saveToken(accessTokenSecret, "access.token.secret");
  }

  public String saveAccessToken(String accessToken) {
    log.info("Saving access token %s.", accessToken);
    return saveToken(accessToken, "access.token");
  }

  private String saveToken(String token, String fileName) {
    String outName = GOODREADS_ROOT + fileName;
    try (final PrintWriter writer = Unchecked.get(() -> new PrintWriter(outName, StandardCharsets.UTF_8))) {
      writer.print(token);
    }
    return token;
  }
}
