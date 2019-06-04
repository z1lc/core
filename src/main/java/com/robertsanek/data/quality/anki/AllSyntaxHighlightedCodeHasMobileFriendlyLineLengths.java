package com.robertsanek.data.quality.anki;

import java.util.Arrays;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableSet;

public class AllSyntaxHighlightedCodeHasMobileFriendlyLineLengths extends DataQualityBase {

  private static final int MAX_LINE_LENGTH = 65;
  private static final ImmutableSet<Long> NOTE_ID_EXCLUSIONS = ImmutableSet.of(
      1422760703581L,
      1422761169018L,
      1463275593211L,
      1464727944478L,
      1489002714666L,
      1550182062881L, //just barely 61 char
      1553985540001L, //62

      //xml
      1559350133563L,
      1559345588345L,
      1559345796149L,
      1559346168368L,
      0L
  );

  @Override
  void runDQ() {
    getAllNotesInRelevantDecks().stream()
        .filter(note -> !NOTE_ID_EXCLUSIONS.contains(note.getId()))
        .flatMap(note -> splitCsvIntoCommaSeparatedList(note.getFields()).stream()
            .map(field -> Pair.of(note.getId(), field)))
        .filter(idFieldPair -> idFieldPair.getRight().contains("<div class=\"highlight\""))
        .forEach(idFieldPairHighlightedCode -> {
          int start = idFieldPairHighlightedCode.getRight().indexOf("<pre");
          int end = idFieldPairHighlightedCode.getRight().indexOf("</pre>");
          if (start > 0 && end > 0) {
            String highlightPre = idFieldPairHighlightedCode.getRight().substring(start, end);
            String codeOnly = highlightPre
                .replaceAll("<pre style=\"line-height: 125%\">", "")
                .replaceAll("(<span.*?>|</span>|<b>|</b>|<u>|</u>)", "")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&")
                .replaceAll("\\{\\{c\\d+::(.*?)}}", "$1");
            if (Arrays.stream(codeOnly.split("\n")).anyMatch(line -> line.length() > MAX_LINE_LENGTH)) {
              violations.put(this.getClass(), "nid:" + idFieldPairHighlightedCode.getLeft());
            }
          }
        });
  }
}
