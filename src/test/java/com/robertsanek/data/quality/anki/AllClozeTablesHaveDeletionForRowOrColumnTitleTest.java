package com.robertsanek.data.quality.anki;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

public class AllClozeTablesHaveDeletionForRowOrColumnTitleTest {

  @Test
  @Ignore("integration")
  public void integration() {
    new AllClozeTablesHaveDeletionForRowOrColumnTitle().runDQ();
  }

  @Test
  public void hasViolation_empty() {
    assertFalse(AllClozeTablesHaveDeletionForRowOrColumnTitle.hasViolation(""));
  }

  @Test
  public void hasViolation_noClozeDeletion() {
    assertFalse(
        AllClozeTablesHaveDeletionForRowOrColumnTitle.hasViolation("<table><th><tr><td></td></tr></th></table>"));
  }

  @Test
  public void hasViolation_notInFirstRow() {
    String clozeTablePythonExample = "<code>L = list(range(10))</code>\n" +
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
    assertFalse(AllClozeTablesHaveDeletionForRowOrColumnTitle.hasViolation(clozeTablePythonExample));
  }

  @Test
  public void hasViolation_notFullCell() {
    String clozeTableDynamoExample = "<table>\n" +
        "<tbody><tr>\n" +
        "<th>Dynamo node selection strategy</th>\n" +
        "<th>routing through {{c3::generic load balancer}}</th>\n" +
        "<th>using {{c4::partition-aware client library}}</th>\n" +
        "</tr>\n" +
        "<tr>\n" +
        "<td>Advantage of __ to invoke get() and put() operations:</td>\n" +
        "<td>{{c1::client doesn't need any Dynamo-specific code}}</td>\n" +
        "<td>{{c2::lower latency since forwarding step is skipped}}</td>\n" +
        "</tr>\n" +
        "</tbody></table>";
    assertFalse(AllClozeTablesHaveDeletionForRowOrColumnTitle.hasViolation(clozeTableDynamoExample));
  }

  @Test
  public void hasViolation_violation() {
    assertTrue(AllClozeTablesHaveDeletionForRowOrColumnTitle.hasViolation(
        "<table><th><tr><td></td><td>Header</td></tr></th><tr><td>Hello there</td><td>{{c1::hi}}</td></tr></table>"));
  }

  @Test
  public void hasViolation_violation2() {
    String clozeTableDynamoExampleShouldViolate = "<table>\n" +
        "<tbody><tr>\n" +
        "<th></th>\n" +
        "<th>Dynamo's technique to address __:</th>\n" +
        "</tr>\n" +
        "<tr>\n" +
        "<td>partitioning</td>\n" +
        "<td>{{c1::consistent hashing}}</td>\n" +
        "</tr>\n" +
        "<tr>\n" +
        "<td>highly available writes</td>\n" +
        "<td>{{c2::vector clocks}} with {{c4::reconciliation::action}} {{c3::during reads::when?}}</td>\n" +
        "</tr>\n" +
        "</tbody></table>";
    assertTrue(AllClozeTablesHaveDeletionForRowOrColumnTitle.hasViolation(clozeTableDynamoExampleShouldViolate));
  }

  @Test
  public void hasViolation_noViolation() {
    String muscleStrengthEnduranceShouldNotViolate = "<table>\n" +
        "<tbody><tr>\n" +
        "<th></th>\n" +
        "<th>What level of resistance to emphasize __?</th>\n" +
        "<th>How many repetitions to emphasize __?</th>\n" +
        "<th>At what velocity should you weight train to focus on __?</th>\n" +
        "</tr>\n" +
        "<tr>\n" +
        "<td>muscle {{c2::strength::endurance / power / strength}}</td>\n" +
        "<td>{{c10::high::low / moderate / high}}</td>\n" +
        "<td>{{c8::fewer::fewer / more}}</td>\n" +
        "<td>{{c5::normal::normal / high}}</td>\n" +
        "</tr>\n" +
        "<tr>\n" +
        "<td>muscle {{c3::power::endurance / power / strength}}</td>\n" +
        "<td>{{c4::moderate::low / moderate / high}}</td>\n" +
        "<td></td>\n" +
        "<td>{{c7::high::normal / high}}</td>\n" +
        "</tr>\n" +
        "<tr>\n" +
        "<td>muscle {{c1::endurance::endurance / power / strength}}</td>\n" +
        "<td>{{c11::low::low / moderate / high}}</td>\n" +
        "<td>{{c9::more::fewer / more}}</td>\n" +
        "<td>{{c6::normal::normal / high}}</td>\n" +
        "</tr>\n" +
        "</tbody></table>";
    assertFalse(AllClozeTablesHaveDeletionForRowOrColumnTitle.hasViolation(muscleStrengthEnduranceShouldNotViolate));
  }
}