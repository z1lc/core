package com.robertsanek.data.etl.remote.lastfm;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import org.junit.Ignore;
import org.junit.Test;

import com.robertsanek.util.FakeSecretProvider;

public class ArtistEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Artist> objects = new ArtistEtl().getObjects();
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