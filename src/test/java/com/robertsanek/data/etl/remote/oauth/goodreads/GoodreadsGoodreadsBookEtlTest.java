package com.robertsanek.data.etl.remote.oauth.goodreads;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GoodreadsGoodreadsBookEtlTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<GoodreadsBook> objects = new GoodreadsBookEtl().getObjects();
    System.out.println("objects = " + objects);
  }

  @Test
  public void cleanGoodreadsAuthor_inMap() {
    assertEquals("J. K. Rowling", GoodreadsBookEtl.cleanGoodreadsAuthor("J.K. Rowling"));
  }

  @Test
  public void cleanGoodreadsAuthor_middleNames() {
    assertEquals("David Kirk", GoodreadsBookEtl.cleanGoodreadsAuthor("David B. Kirk"));
    assertEquals("Gayle McDowell", GoodreadsBookEtl.cleanGoodreadsAuthor("Gayle Laakmann McDowell"));
  }

  @Test
  public void cleanGoodreadsAuthor_jrAndMiddleName() {
    assertEquals("William Dentzer", GoodreadsBookEtl.cleanGoodreadsAuthor("William T. Dentzer, Jr."));
  }

  @Test
  public void cleanGoodreadsAuthor_oneName() {
    assertEquals("Plato", GoodreadsBookEtl.cleanGoodreadsAuthor("Plato"));
  }

}