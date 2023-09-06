package com.robertsanek.data.quality.anki;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllClozeTablesHaveUnderscoresAndUniqueDeletionIndexesAndNoThElementsInRowsOtherThanTheHeader
    extends DataQualityBase {

  private static final Log log =
      Logs.getLog(AllClozeTablesHaveUnderscoresAndUniqueDeletionIndexesAndNoThElementsInRowsOtherThanTheHeader.class);
  private static final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1512433850788L,
      1511970123106L,
      1511197551925L,
      1510852295909L,
      1423513347990L,
      1423067675251L,
      1551911896871L,
      1534845749748L,
      1562008511052L,
      1564894601797L,
      1564885781959L,
      1595094496843L,
      1553664584201L,
      0L
  );

  @Override
  void runDQ() {
    getAllNotesInRelevantDecks(CLOZE_MODEL_ID, NOTE_ID_EXCLUSIONS).stream()
        .filter(note -> {
          List<String> fields = splitCsvIntoCommaSeparatedList(note.getFields());

          if (fields.get(0).contains("<table>") && !CODE_TABLE.matcher(fields.get(0)).find()) {
            Boolean thAfterTheadComplete = Pattern.compile("</thead>[\\s\\S]*<th>")
                .matcher(fields.get(0))
                .matches();

            Pattern cxPattern = Pattern.compile("\\{\\{c(\\d+)::");
            List<String> cxAsList = cxPattern.matcher(fields.get(0)).results()
                .map(MatchResult::group)
                .toList();
            boolean clozeIndexMismatch = Sets.newHashSet(cxAsList).size() != cxAsList.size();

            boolean noUnderscoreOrDescription = !(fields.get(0).contains("__")
                || fields.get(0).contains("Definition")
                || fields.get(0).contains("Description"));

            return thAfterTheadComplete || clozeIndexMismatch || noUnderscoreOrDescription;
          }
          return false;
        })
        .forEach(note -> {
          violations.put(this.getClass(), "nid:" + note.getId());
        });
  }
}
