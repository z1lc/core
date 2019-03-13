package com.robertsanek.data.etl.remote.oauth.toodledo;

import com.github.scribejava.core.builder.api.DefaultApi20;

public class ToodledoApi20 extends DefaultApi20 {

  private final String clientId;
  private final String clientSecret;

  public ToodledoApi20(String clientId, String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  @Override
  public String getAccessTokenEndpoint() {
    return String.format("https://%s:%s@api.toodledo.com/3/account/token.php", clientId, clientSecret);
  }

  @Override
  protected String getAuthorizationBaseUrl() {
    return "https://api.toodledo.com/3/account/authorize.php";
  }
}
