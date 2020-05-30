package com.robertsanek.data.quality.anki;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.robertsanek.data.etl.local.sqllite.anki.Model;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class FieldsThatControlNewCardGenerationAreLabeledWithUnicodeDiamond extends DataQualityBase {

  static final Log log = Logs.getLog(FieldsThatControlNewCardGenerationAreLabeledWithUnicodeDiamond.class);

  @Override
  void runDQ() {
    //TODO: does not handle double-conditional template generation well (will only detect 1)
    final List<String> IGNORE_MODEL_NAMES = Lists.newArrayList(
        "^AnKindle.*",
        "^Image Occlusion Enhanced$",
        "^The Basic \\(optional reversed card\\)$",
        "^Spotify Track$",
        "^Spotify Artist$"
    );
    Pattern endConditional = Pattern.compile("\\{\\{/.*}}");
    Pattern anyAlphabetical = Pattern.compile("[A-z]+");
    templatesInUse
        .forEach(template -> {
          Matcher matcher = endConditional.matcher(template.getFront_html());
          matcher.results().forEach(result -> {
            int start = result.start();
            int end = result.end();
            String fieldName = template.getFront_html().substring(start + 3, end - 2);
            String endOfCardFieldsRemoved = template.getFront_html().substring(start).replaceAll("\\{\\{.*}}", "");
            if (!anyAlphabetical.matcher(endOfCardFieldsRemoved).find() &&
                !(fieldName.startsWith("\uD83D\uDD39") || fieldName.startsWith("✨"))) {
              Model model = Iterables.getOnlyElement(modelsByModelId.get(template.getModel_id()));
              if (IGNORE_MODEL_NAMES.stream().noneMatch(regex -> model.getName().matches(regex))) {
                log.error("Template with name '%s' in note '%s' has field '%s' that controls creation of " +
                        "new cards, but the field is not labeled with \uD83D\uDD39 or ✨.",
                    template.getName(),
                    model.getName(),
                    fieldName);
                violations.put(this.getClass(), String.format("\"note:%s\"", model.getName()));
              }
            }
          });
        });
  }
}
