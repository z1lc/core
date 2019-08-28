package com.robertsanek.data.etl.local.habitica;

import java.net.URI;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.local.habitica.jsonentities.JsonTask;
import com.robertsanek.data.etl.local.habitica.jsonentities.Response;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.SecretType;
import com.robertsanek.util.Unchecked;

public abstract class HabiticaEtl<T> extends Etl<T> {

  @Inject ObjectMapper mapper;
  @Inject SecretProvider secretProvider;

  public List<JsonTask> getJsonObjects() {
    String responseJson = genericGetJson("api/v3/tasks/user", Lists
        .newArrayList(new BasicNameValuePair("type", "dailys")));
    Response response = Unchecked.get(() -> mapper.readValue(responseJson, Response.class));
    return response.getData();
  }

  String genericGetJson(String path, List<NameValuePair> parameter) {
    final URI uri = Unchecked.get(() -> new URIBuilder()
        .setScheme("https")
        .setHost("habitica.com")
        .setPath(path)
        .setParameters(parameter)
        .build());
    return Unchecked.get(() -> Request.Get(uri)
        .addHeader("x-api-key", secretProvider.getSecret(SecretType.HABITICA_API_KEY))
        .addHeader("x-api-user", secretProvider.getSecret(SecretType.HABITICA_USER))
        .execute()
        .returnContent()
        .asString());
  }

}
