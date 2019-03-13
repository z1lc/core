package com.robertsanek.data.etl.remote.scrape.leetcode;

import static com.robertsanek.util.SecretType.LEETCODE_PASSWORD;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.remote.scrape.leetcode.jsonentities.LCQuestion;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.Unchecked;

public class LeetCodeQuestionEtl extends Etl<Question> {

  private static final ObjectMapper mapper = CommonProvider.getObjectMapper();
  private static final String LOGIN = CommonProvider.getEmailAddress();
  private static final RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
  private static final CookieStore cookieStore = new BasicCookieStore();

  @Override
  public List<Question> getObjects() {
    try {
      HttpClientContext context = HttpClientContext.create();
      context.setCookieStore(cookieStore);
      HttpGet httpGet = new HttpGet("https://leetcode.com/accounts/login/");
      httpGet.setHeader("Referer", "https://leetcode.com/");
      getHttpClient().execute(httpGet, context);
      String csrftoken = Iterables.getOnlyElement(context.getCookieStore().getCookies().stream()
          .filter(cookie -> cookie.getName().equals("csrftoken"))
          .collect(Collectors.toList()))
          .getValue();

      URI loginUri = Unchecked.get(() -> new URIBuilder()
          .setScheme("https")
          .setHost("leetcode.com")
          .setPath("accounts/login/")
          .build());
      HttpPost post = new HttpPost(loginUri);
      post.setHeader("Referer", "https://leetcode.com/accounts/login/");
      post.setEntity(MultipartEntityBuilder.create()
          .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
          .addTextBody("csrfmiddlewaretoken", csrftoken)
          .addTextBody("login", LOGIN)
          .addTextBody("password", CommonProvider.getSecret(LEETCODE_PASSWORD))
          .build());
      String loginResponse = EntityUtils.toString(getHttpClient().execute(post).getEntity());
      if (loginResponse.toLowerCase().contains("csrf")) {
        throw new RuntimeException("CSRF token problem.");
      }

      HttpGet httpGet2 = new HttpGet("https://leetcode.com/api/problems/algorithms/");
      JsonObject obj =
          new JsonParser().parse(EntityUtils.toString(getHttpClient().execute(httpGet2).getEntity())).getAsJsonObject();
      if (!obj.toString().contains("rsanek")) {
        throw new RuntimeException("Could not guarantee user was logged into LeetCode. " +
            "Likely need to debug names & values of header fields, csrf token, or cookies.");
      }
      String questionInfo = obj.getAsJsonArray("stat_status_pairs").toString();
      return Lists.newArrayList(Unchecked.get(() -> mapper.readValue(questionInfo, LCQuestion[].class))).stream()
          .map(lcQuestion -> Question.QuestionBuilder.aQuestion()
              .withId((long) lcQuestion.getStat().getQuestionId())
              .withTitle(lcQuestion.getStat().getQuestionTitle())
              .withStatus(Question.Status.fromValue(lcQuestion.getStatus()))
              .withDifficulty((long) lcQuestion.getDifficulty().getLevel())
              .build())
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private CloseableHttpClient getHttpClient() {
    return HttpClients.custom()
        .setDefaultRequestConfig(globalConfig)
        .setDefaultCookieStore(cookieStore)
        .build();
  }
}
