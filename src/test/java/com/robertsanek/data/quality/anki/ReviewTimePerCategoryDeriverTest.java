package com.robertsanek.data.quality.anki;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.derived.anki.ReviewTimePerCategory;
import com.robertsanek.data.derived.anki.ReviewTimePerCategoryDeriver;
import com.robertsanek.data.etl.local.sqllite.anki.Review;

public class ReviewTimePerCategoryDeriverTest {

  @Test
  @Disabled("integration")
  public void integration() {
    List<ReviewTimePerCategory> objects = new ReviewTimePerCategoryDeriver().getObjects();
    System.out.println("objects = " + objects);
  }

  @Test
  @Disabled("needs local Anki db")
  void reviewToCategory_other_as_default() {
    String actual = new ReviewTimePerCategoryDeriver().reviewToCategory(Review.ReviewBuilder.aReview()
        .withCard_id(1590470946681L)
        .build());
    assertEquals("Other", actual);
  }

  @Test
  @Disabled("needs local Anki db")
  void reviewToCategory_computing_by_model() {
    String actual = new ReviewTimePerCategoryDeriver().reviewToCategory(Review.ReviewBuilder.aReview()
        .withCard_id(1590430668579L)
        .build());
    assertEquals("Computing", actual);
  }

  @Test
  @Disabled("needs local Anki db")
  void reviewToCategory_work_by_tag() {
    String actual = new ReviewTimePerCategoryDeriver().reviewToCategory(Review.ReviewBuilder.aReview()
        .withCard_id(1590511642292L)
        .build());
    assertEquals("Work", actual);
  }

  @Test
  @Disabled("needs local Anki db")
  void reviewToCategory_culture_by_context() {
    String actual = new ReviewTimePerCategoryDeriver().reviewToCategory(Review.ReviewBuilder.aReview()
        .withCard_id(1542917262530L)
        .build());
    assertEquals("Culture", actual);
  }
}