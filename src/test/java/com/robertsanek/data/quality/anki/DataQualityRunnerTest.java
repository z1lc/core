package com.robertsanek.data.quality.anki;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.quartz.JobDataMap;

public class DataQualityRunnerTest {

  @Test
  @Ignore("integration")
  public void run() {
    new DataQualityRunner().exec((JobDataMap) null);
  }

  @Test
  public void getEmailSubject() {
    assertEquals("Anki DQ: 1 error, 1 warning found!", DataQualityRunner.getEmailSubject(1, 1));
    assertEquals("Anki DQ: All checks passed!", DataQualityRunner.getEmailSubject(0, 0));
    assertEquals("Anki DQ: All checks passed!", DataQualityRunner.getEmailSubject(-999, -2));
    assertEquals("Anki DQ: 1 warning found!", DataQualityRunner.getEmailSubject(0, 1));
    assertEquals("Anki DQ: 2 errors found!", DataQualityRunner.getEmailSubject(2, 0));
  }

  @Test
  public void getEmailContent_empty() {
    assertEquals("<div></div>", DataQualityRunner.getEmailContent(new DataQualityBase.DQInformation()).render());
  }

  @Test
  public void getEmailContent_oneWarning() {
    DataQualityBase.DQInformation ex = new DataQualityBase.DQInformation();
    ex.warn("warning");
    assertEquals("<div><b>Warnings:</b><br><table><tr><td>warning</td><td></td></tr></table></div>",
        DataQualityRunner.getEmailContent(ex).render());
  }
}