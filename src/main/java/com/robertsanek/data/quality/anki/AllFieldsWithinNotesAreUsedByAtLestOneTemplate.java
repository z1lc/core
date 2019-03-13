package com.robertsanek.data.quality.anki;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.robertsanek.data.etl.local.sqllite.anki.Model;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllFieldsWithinNotesAreUsedByAtLestOneTemplate extends DataQualityBase {

  static final Log log = Logs.getLog(AllFieldsWithinNotesAreUsedByAtLestOneTemplate.class);
  private static final ImmutableSet<String> ALLOWED_EMPTY_FIELD_REGEX = ImmutableSet.of(
      "^\uD83D\uDD39Add Reverse \uD83D\uDD00$",
      "^Source \uD83C\uDFAF$",
      "^Context \uD83D\uDCA1$",
      "^@Deprecated.*",
      "^@Unused.*"
  );
  private static final ImmutableSet<String> ALLOWED_EMPTY_NOTE_TYPE_REGEX = ImmutableSet.of(
      ".*Cloze \\(overlapping\\).*",
      "^AnKindle.*",
      "^Image Occlusion Enhanced$"
  );
  Pattern fieldInTemplatePattern = Pattern.compile("\\{\\{.+?}}");

  @Override
  void runDQ() {
    modelsInUse.stream()
        .filter(model -> ALLOWED_EMPTY_NOTE_TYPE_REGEX.stream()
            .noneMatch(excludedNoteRegex -> model.getName().matches(excludedNoteRegex)))
        .sorted(Comparator.comparing(Model::getId))
        .forEach(model -> {
          Set<String> allFields = Sets.newHashSet(splitCsvIntoCommaSeparatedList(model.getFields())).stream()
              .filter(field -> ALLOWED_EMPTY_FIELD_REGEX.stream()
                  .noneMatch(field::matches))
              .collect(Collectors.toSet());
          Set<String> usedFields = allTemplates.stream()
              .filter(template -> template.getModel_id().equals(model.getId()))
              .flatMap(template -> Stream.of(template.getBack_html(), template.getFront_html()))
              .flatMap(html -> {
                Matcher matcher = fieldInTemplatePattern.matcher(html);
                return matcher.results().map(MatchResult::group);
              })
              .map(matchedField -> matchedField
                  .replaceAll("^\\{\\{", "")
                  .replaceAll("}}$", "")
                  .replaceAll("^#", "")
                  .replaceAll("^/", "")
                  .replaceAll("cloze:", "")
              )
              .collect(Collectors.toSet());
          Sets.SetView<String> unusedFields = Sets.difference(allFields, usedFields);
          List<String> sortedFields = unusedFields.stream().sorted().collect(Collectors.toList());
          if (unusedFields.size() > 0) {
            log.warn("Note '%s': %s %s appear in any template: %s", model.getName(), sortedFields.size(),
                sortedFields.size() > 1 ? "fields do not" : "field does not", String.join(", ", sortedFields));
            violations.put(this.getClass(), String.format("\"note:%s\"", model.getName()));
          }
        });
  }

}
