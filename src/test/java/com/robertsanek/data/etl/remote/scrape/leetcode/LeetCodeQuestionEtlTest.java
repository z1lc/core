package com.robertsanek.data.etl.remote.scrape.leetcode;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.util.inject.InjectUtils;

public class LeetCodeQuestionEtlTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<Question> objects = InjectUtils.inject(LeetCodeQuestionEtl.class).getObjects();
    System.out.println("objects = " + objects);
  }
}