package com.robertsanek.data.etl.remote.trello;

import com.julienvey.trello.Trello;
import com.julienvey.trello.impl.TrelloImpl;
import com.julienvey.trello.impl.http.ApacheHttpClient;
import com.robertsanek.util.CommonProvider;
import com.robertsanek.util.SecretType;

public class TrelloConnector {

  public static Trello getApi() {
    return new TrelloImpl(
        CommonProvider.getSecret(SecretType.TRELLO_API_KEY),
        CommonProvider.getSecret(SecretType.TRELLO_API_ACCESS_TOKEN),
        new ApacheHttpClient());
  }

}
