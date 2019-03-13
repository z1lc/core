package com.robertsanek.data.quality.anki;

import static org.junit.Assert.assertFalse;

import org.junit.Ignore;
import org.junit.Test;

public class AllClozeTablesHaveDeletionForRowOrColumnTitleTest {

  private String clozeTablePythonExample = "<code>L = list(range(10))</code>\n" +
      "<table>\n" +
      "<tbody><tr>\n" +
      "<th></th>\n" +
      "<th><code>L__</code></th>\n" +
      "<th><code>del L__</code></th>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td><br></td>\n" +
      "<td><code>{{c9::[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]}}</code></td>\n" +
      "<td></td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td><code>{{c1::[:<span></span>:2]}}</code></td>\n" +
      "<td><code>{{c6::[0, 2, 4, 6, 8]}}</code></td>\n" +
      "<td></td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td><code>{{c3::[1:2]}}</code></td>\n" +
      "<td><code>{{c10::[1]}}</code></td>\n" +
      "<td></td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td><code>{{c4::[3:6:2]}}<br></code></td>\n" +
      "<td><code>{{c7::[3, 5]}}</code></td>\n" +
      "<td></td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td><code>{{c2::[:<span></span>:-1]}}</code></td>\n" +
      "<td><code>{{c11::[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]}}</code></td>\n" +
      "<td></td>\n" +
      "</tr>\n" +
      "<tr>\n" +
      "<td><code>{{c5::[:<span></span>:3]}}</code></td>\n" +
      "<td><br></td>\n" +
      "<td><code>{{c8::[1, 2, 4, 5, 7, 8]}}</code></td>\n" +
      "</tr>\n" +
      "</tbody></table>";

  @Test
  @Ignore("integration")
  public void integration() {
    new AllClozeTablesHaveDeletionForRowOrColumnTitle().runDQ();
  }

  @Test
  @Ignore("integration")
  public void hasViolation_empty() {
    assertFalse(AllClozeTablesHaveDeletionForRowOrColumnTitle.hasViolation(""));
  }

  @Test
  @Ignore("integration")
  public void hasViolation_simpleTable() {
    assertFalse(
        AllClozeTablesHaveDeletionForRowOrColumnTitle.hasViolation("<table><th><tr><td></td></tr></th></table>"));
  }

  @Test
  @Ignore("integration")
  public void hasViolation_notInFirstRow() {
    assertFalse(AllClozeTablesHaveDeletionForRowOrColumnTitle.hasViolation(clozeTablePythonExample));
  }

}