package com.robertsanek.data.etl.remote.trello;

import com.google.inject.Inject;
import com.julienvey.trello.Trello;
import com.julienvey.trello.impl.TrelloImpl;
import com.julienvey.trello.impl.http.ApacheHttpClient;
import com.robertsanek.util.SecretProvider;
import com.robertsanek.util.SecretType;

public class TrelloConnector {

  @Inject SecretProvider secretProvider;

  public Trello getApi() {
    return new TrelloImpl(
        secretProvider.getSecret(SecretType.TRELLO_API_KEY),
        secretProvider.getSecret(SecretType.TRELLO_API_ACCESS_TOKEN),
        new ApacheHttpClient());
  }

}
