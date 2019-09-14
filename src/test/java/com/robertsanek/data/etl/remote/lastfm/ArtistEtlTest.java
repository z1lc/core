package com.robertsanek.data.etl.remote.lastfm;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.FakeSecretProvider;
import com.robertsanek.util.inject.InjectUtils;

public class ArtistEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<Artist> objects = InjectUtils.inject(ArtistEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }

  @Test
  public void getApiResponse_doesntThrowOn500() {
    ArtistEtl etl = new ArtistEtl() {
      @Override
      String get(URI uri) throws IOException {
        throw new HttpResponseException(500, "Internal Server Error");
      }
    };
    etl.secretProvider = new FakeSecretProvider();
    etl.getApiResponse(0, 0);
  }
}