package com.robertsanek.data.etl.remote.humanapi;

import static com.robertsanek.util.SecretType.HUMAN_API_ACCESS_TOKEN;

import java.net.URI;
import java.util.List;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.Unchecked;

abstract class HumanApiEtl<T> extends Etl<T> {

  @Inject ObjectMapper mapper;
  @Inject SecretProvider secretProvider;

  <O> List<O> genericGet(String path, Class<O[]> clazz) {
    final URI weightUri = Unchecked.get(() -> new URIBuilder()
        .setScheme("https")
        .setHost("api.humanapi.co")
        .setPath(path)
        .setParameter("access_token", secretProvider.getSecret(HUMAN_API_ACCESS_TOKEN))
        .setParameter("limit", "500")
        .build());
    String weightsJson = Unchecked.get(() -> Request.Get(weightUri)
        .execute()
        .returnContent()
        .asString());
    return Lists.newArrayList(Unchecked.get(() -> mapper.readValue(weightsJson, clazz)));
  }

}
