package com.robertsanek.data.etl.remote.oauth.goodreads;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * Handles OAuth1 to GoodReads Api as per ScribeJava Custom Api guidelines.
 * <p>
 * Created by Jenni on 2.5.2017.
 */
public class GoodreadsApi10a extends DefaultApi10a {

  /**
   * Authorization url.
   */
  private static final String AUTHORIZE_URL = "https://www.goodreads.com/oauth/authorize?oauth_token=%s";

  /**
   * Default constructor.
   */
  protected GoodreadsApi10a() {
  }

  /**
   * Returns instance.
   *
   * @return GoodreadsApi
   */
  public static GoodreadsApi10a instance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * Returns access token endpoint.
   *
   * @return String access token endpoint url
   */
  @Override
  public String getAccessTokenEndpoint() {
    return "https://www.goodreads.com/oauth/access_token";
  }

  @Override
  protected String getAuthorizationBaseUrl() {
    return AUTHORIZE_URL;
  }

  /**
   * Returns request token endpoint.
   *
   * @return String request token endpoint url
   */
  @Override
  public String getRequestTokenEndpoint() {
    return "https://www.goodreads.com/oauth/request_token";
  }

  /**
   * Returns authorization url.
   *
   * @param requestToken request token
   * @return String authorization url
   */
  @Override
  public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
    return String.format(AUTHORIZE_URL, requestToken.getToken());
  }

  /**
   * Instance holder.
   */
  private static class InstanceHolder {

    private static final GoodreadsApi10a INSTANCE = new GoodreadsApi10a();
  }
}