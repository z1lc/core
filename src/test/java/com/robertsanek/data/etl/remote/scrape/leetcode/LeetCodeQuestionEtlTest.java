package com.robertsanek.data.etl.remote.scrape.leetcode;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class LeetCodeQuestionEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<Question> objects = new LeetCodeQuestionEtl().getObjects();
    System.out.println("objects = " + objects);
  }
}