package com.robertsanek.data.etl.remote.scrape.leetcode;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class LeetCodeQuestionEtlTest {

  @Test
  @Ignore("integration")
  public void name() {
    List<Question> objects = new LeetCodeQuestionEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}