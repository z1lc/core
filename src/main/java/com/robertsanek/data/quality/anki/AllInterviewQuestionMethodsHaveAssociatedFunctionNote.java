package com.robertsanek.data.quality.anki;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Jsoup;

import com.google.common.collect.ImmutableSet;

public class AllInterviewQuestionMethodsHaveAssociatedFunctionNote extends DataQualityBase {

  private static final Pattern methodNameRegex = Pattern.compile("\\.([a-zA-Z][a-zA-Z0-9]+)\\(");
  private static final ImmutableSet<String> METHOD_NAME_EXCLUSIONS = ImmutableSet.of(
      "dfs",
      "findUnassigned",
      "getKey",
      "getRange",
      "getValue",
      "isSafe",
      "recurGen",
      "recurSum",
      "remove",
      "solve"
  );

  @Override
  void runDQ() {
    Set<String> methodNames =
        getAllNotesInRelevantDecks(PROGRAMMING_LANGUAGE_FUNCTION_MODEL_ID).stream()
            .map(note -> splitCsvIntoCommaSeparatedList(note.getFields()).get(0))
            .collect(Collectors.toSet());
    getAllNotesInRelevantDecks(INTERVIEW_QUESTION_MODEL_ID).stream()
        .flatMap(note -> {
          List<String> strings = splitCsvIntoCommaSeparatedList(note.getFields());
          if (strings.size() > 10) {
            return Stream.of(Jsoup.parse(strings.get(10)).text());
          }
          return Stream.empty();
        })
        .flatMap(solutionText ->
            methodNameRegex.matcher(solutionText).results()
                .map(result -> result.group(1)))
        .distinct()
        .filter(methodName -> !METHOD_NAME_EXCLUSIONS.contains(methodName))
        .filter(methodName -> !methodNames.contains(methodName))
        .forEach(method -> violations.put(this.getClass(), method));
  }
}
