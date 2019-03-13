package com.robertsanek.data.quality.anki;

import com.google.common.collect.Iterables;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class AllTemplatesHaveFieldsWithArial20 extends DataQualityBase {

  static final Log log = Logs.getLog(AllTemplatesHaveFieldsWithArial20.class);

  @Override
  void runDQ() {
    fieldsInUse.stream()
        .filter(field -> !field.getFont_face().equals("Arial") || !field.getFont_size().equals(20L))
        .filter(field -> !(field.getFont_size().equals(5L) &&
            field.getName().equals("\uD83D\uDD39Add Reverse \uD83D\uDD00")))
        .forEach(field -> {
          String modelName = Iterables.getOnlyElement(modelsByModelId.get(field.getModel_id())).getName();
          if (!modelName.contains("Cloze (overlapping)")) {
            violations.put(this.getClass(), String.format("\"note:%s\"", modelName));
            log.error("Note '%s': field with name '%s' is %s %spx instead of Arial 20px.",
                modelName, field.getName(), field.getFont_face(), field.getFont_size());
          }
        });
  }

}
