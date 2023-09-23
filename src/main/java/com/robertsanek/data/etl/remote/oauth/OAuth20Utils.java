package com.robertsanek.data.etl.remote.oauth;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;

public class OAuth20Utils {

  private static final Log log = Logs.getLog(OAuth20Utils.class);

  private final OAuth20Service service;
  private final String tokenSaveLocation;
  private final String exampleUrl;

  public OAuth20Utils(OAuth20Service service, String tokenSaveLocation, String exampleUrl) {
    this.service = service;
    this.tokenSaveLocation = tokenSaveLocation;
    this.exampleUrl = exampleUrl;
  }

  public synchronized void handleLogin() {
    Optional<String> maybeAccessToken = maybeGetAccessToken();
    Optional<String> maybeRefreshToken = maybeGetRefreshToken();

    if (!isAccessTokenValid(maybeAccessToken)) {
      if (maybeRefreshToken.isEmpty()) {
        log.info("Did not find existing refresh token. Need to authenticate.");
        final Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);
        final String authorizationUrl = service.getAuthorizationUrl();
        log.info("Go to " + authorizationUrl);
        log.info("Paste the authorization code:");
        log.info(">>");
        final String code = in.nextLine();
        maybeRefreshToken =
            Optional.of(saveRefreshToken(Unchecked.get(() -> service.getAccessToken(code)).getRefreshToken()));
      } else {
        log.info("Found existing refresh token %s.", maybeRefreshToken.orElseThrow());
      }
      getRefreshedAccessToken(maybeRefreshToken.orElseThrow()).getAccessToken();
    }
  }

  private boolean isAccessTokenValid(Optional<String> maybeAccessToken) {
    if (maybeAccessToken.isPresent()) {
      final Response response = getSignedResponse(exampleUrl, maybeAccessToken.orElseThrow());
      if (response.getCode() == 200) {
        log.info("Existing access token %s is still valid.", maybeAccessToken.orElseThrow());
        return true;
      } else {
        log.info("Existing access token %s is no longer valid.", maybeAccessToken.orElseThrow());
        return false;
      }
    }
    log.info("Did not find access token.");
    return false;
  }

  public Response getSignedResponse(String requestUrl, String accessToken) {
    final OAuthRequest request = new OAuthRequest(Verb.GET, requestUrl);
    service.signRequest(accessToken, request);
    return Unchecked.get(() -> service.execute(request));
  }

  public synchronized OAuth2AccessToken getRefreshedAccessToken(String refreshToken) {
    log.info("Getting refreshed access token (using refresh token %s).", refreshToken);
    OAuth2AccessToken accessToken = Unchecked.get(() -> service.refreshAccessToken(refreshToken));
    saveRefreshToken(accessToken.getRefreshToken());
    saveAccessToken(accessToken.getAccessToken());
    return accessToken;
  }

  public Optional<String> maybeGetRefreshToken() {
    return maybeGet("refresh.token");
  }

  public Optional<String> maybeGetAccessToken() {
    return maybeGet("access.token");
  }

  public String saveRefreshToken(String refreshToken) {
    log.info("Saving refresh token %s.", refreshToken);
    return saveToken(refreshToken, "refresh.token");
  }

  public String saveAccessToken(String accessToken) {
    log.info("Saving access token %s.", accessToken);
    return saveToken(accessToken, "access.token");
  }

  private Optional<String> maybeGet(String fileName) {
    return getFile(fileName)
        .map(file -> Unchecked.get(() -> Files.readString(file)));
  }

  private synchronized String saveToken(String token, String fileName) {
    Unchecked.run(() -> Files.writeString(Paths.get(tokenSaveLocation + fileName), token));
    return token;
  }

  private Optional<Path> getFile(String fileName) {
    File maybeFile = new File(tokenSaveLocation + fileName);
    if (maybeFile.exists() && maybeFile.isFile()) {
      return Optional.of(maybeFile.toPath());
    }
    return Optional.empty();
  }
}
