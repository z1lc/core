package com.robertsanek.data.quality.anki;

import static com.robertsanek.data.quality.anki.DataQualityBase.cleanName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.robertsanek.data.quality.anki.DataQualityBase.DQInformation;

public class DataQualityBaseTest {

  @Test
  public void getGenericAsTable() {
    String expected = "<table><tr><td>DataQuality Check</td><td>front:hi</td></tr>" +
        "<tr><td>DataQuality Check 2</td><td>front:hello</td></tr></table>";
    String actual = DQInformation.getGenericAsTable(Lists.newArrayList(
        new DataQualityBase.IndividualError("DataQuality Check", "front:hi"),
        new DataQualityBase.IndividualError("DataQuality Check 2", "front:hello")))
        .render();
    assertEquals(expected, actual);
  }

  @Test
  @Ignore("integration")
  public void getExistingPeopleInAnkiDb() {
    Set<String> existingPeopleInAnkiDb = DataQualityBase.getExistingPeopleInAnkiDbLowerCased();
    assertTrue(existingPeopleInAnkiDb.contains("robert sanek"));
  }

  @Test
  public void cleanName_empty() {
    assertEquals("", cleanName(""));
  }

  @Test
  public void cleanName_simple() {
    assertEquals("Robert Sanek", cleanName("Robert Sanek"));
  }

  @Test
  public void cleanName_nee() {
    assertEquals("Pam Halpert", cleanName("Pam Halpert (née Beesly)"));
    assertEquals("Pam Halpert", cleanName("Pam Halpert (nee Beesly)"));
  }

  @Test
  public void cleanName_abbrev() {
    assertEquals("David Foster Wallace", cleanName("DFW / David Foster Wallace"));
  }

  @Test
  public void cleanName_or() {
    assertEquals("Cloudchord", cleanName("Cloudchord or D.V.S*"));
    assertEquals("James the Just", cleanName("James the Just or James, brother of Jesus"));
  }

  @Test
  public void cleanName_specialChar() {
    assertEquals("abcdefghi", cleanName("a\"b/c&d*e:f,g'h’i"));
  }

  @Test
  public void cleanName_HER() {
    assertEquals("H.E.R.", cleanName("H.E.R."));
  }

  @Test
  public void cleanName_LetsEatGrandma() {
    assertEquals("Lets Eat Grandma", cleanName("Let's Eat Grandma"));
  }
}
