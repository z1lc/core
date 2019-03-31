package com.robertsanek.data.etl.remote.rescuetime;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import org.junit.Ignore;
import org.junit.Test;

public class CategoryEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Category> objects = new CategoryEtl().getObjects();
    System.out.println("objects = " + objects);
  }

  @Test
  public void genericGet_doesntThrowOn500() {
    CategoryEtl etlInstance = new CategoryEtl() {
      @Override
      String get(URI efficiencyUri) throws IOException {
        throw new HttpResponseException(504, "GATEWAY_TIMEOUT");
      }
    };
    etlInstance.FROM_YEAR = 2019;
    etlInstance.TO_YEAR = 2019;
    etlInstance.getObjects();
  }
}