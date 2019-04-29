package com.robertsanek.data.etl.local.sqllite.calibre;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class BookEtlTest {

  @Test
  @Ignore("integration")
  public void name() throws Exception {
    List<Book> objects = new BookEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}