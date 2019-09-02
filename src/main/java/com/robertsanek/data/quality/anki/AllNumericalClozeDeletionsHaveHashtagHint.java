package com.robertsanek.data.quality.anki;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllNumericalClozeDeletionsHaveHashtagHint extends DataQualityBase {

  static final Log log = Logs.getLog(AllNumericalClozeDeletionsHaveHashtagHint.class);

  @Override
  void runDQ() {
    final Set<Long> NOTE_ID_EXCLUSIONS = Sets.newHashSet(
        1413928215505L,  //IEEE floating point
        1422640917711L,  //IEEE floating point
        1414291348391L,  //RS flip-flop
        1414291902632L,  //D-type latch
        1414293098355L,  //hex 8-F
        1414300247537L,  //intel 4004
        1421332676181L,  //log base
        1423892116563L,  //java interface
        1424058032996L,  //checksum
        1471447583333L,  //23/36 rule
        1543988681948L,  //type 1/2 hypervisor
        1506893843355L,  //SAE 0W-20
        1546907641919L,  //Form ADV part 1/2
        1547699725186L,  //Das Keyboard 4
        1547937943437L,  //python L[::] subsets
        //shoe names
        1552805817123L,
        1552805757575L,
        1552804997297L,
        1553373071049L, //bezos type 1 type 2
        1553903551657L,
        1552684177180L,
        1555695726952L,
        0L
    );
    Pattern clozeDigitsNoHint = Pattern.compile("\\{\\{c\\d+::\\d+?}}");
    Set<String> noteIdViolationsList = new HashSet<>();
    allNotes.forEach(note -> {
      Matcher matcher = clozeDigitsNoHint.matcher(note.getFields());
      if (matcher.find() && !NOTE_ID_EXCLUSIONS.contains(note.getId())) {
        noteIdViolationsList.add("nid:" + note.getId());
      }
    });
    if (noteIdViolationsList.size() > 0) {
      dqInformation.warn(this.getClass(), noteIdViolationsList);
      log.warn(String.join(" or ", noteIdViolationsList));
    }
  }
}
