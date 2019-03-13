package com.robertsanek.data.etl.remote.oauth.goodreads;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class BookEtlTest {

  @Test
  @Ignore("integration")
  public void integration() {
    List<Book> objects = new BookEtl().getObjects();
    System.out.println("objects = " + objects);
  }

  @Test
  public void cleanGoodreadsAuthor_inMap() {
    assertEquals("J. K. Rowling", BookEtl.cleanGoodreadsAuthor("J.K. Rowling"));
  }

  @Test
  public void cleanGoodreadsAuthor_middleNames() {
    assertEquals("David Kirk", BookEtl.cleanGoodreadsAuthor("David B. Kirk"));
    assertEquals("Gayle McDowell", BookEtl.cleanGoodreadsAuthor("Gayle Laakmann McDowell"));
  }

  @Test
  public void cleanGoodreadsAuthor_jrAndMiddleName() {
    assertEquals("William Dentzer", BookEtl.cleanGoodreadsAuthor("William T. Dentzer, Jr."));
  }

  @Test
  public void cleanGoodreadsAuthor_oneName() {
    assertEquals("Plato", BookEtl.cleanGoodreadsAuthor("Plato"));
  }

}