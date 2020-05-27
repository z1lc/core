package com.robertsanek.data.quality.anki;

import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllTemplatesHaveFieldsWithArial20 extends DataQualityBase {

  static final Log log = Logs.getLog(AllTemplatesHaveFieldsWithArial20.class);
  static final Set<String> EXCLUDED_MODELS = Sets.newHashSet(
      "Cloze (overlapping)",
      "Spotify Track",
      "Spotify Artist"
  );

  @Override
  void runDQ() {
    fieldsInUse.stream()
        .filter(field -> !field.getFont_face().equals("Arial") || !field.getFont_size().equals(20L))
        .filter(field -> !(field.getFont_size().equals(5L) &&
            field.getName().equals("\uD83D\uDD39Add Reverse \uD83D\uDD00")))
        .forEach(field -> {
          String modelName = Iterables.getOnlyElement(modelsByModelId.get(field.getModel_id())).getName();
          if (!EXCLUDED_MODELS.contains(modelName)) {
            violations.put(this.getClass(), String.format("\"note:%s\"", modelName));
            log.error("Note '%s': field with name '%s' is %s %spx instead of Arial 20px.",
                modelName, field.getName(), field.getFont_face(), field.getFont_size());
          }
        });
  }

}
