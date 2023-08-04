package com.robertsanek.data.quality.anki;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;
import com.robertsanek.data.etl.local.sqllite.anki.Card;
import com.robertsanek.data.etl.local.sqllite.anki.Template;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

public class NoTemplateHasAllCorrespondingCardsSuspended extends DataQualityBase {

  static final Log log = Logs.getLog(NoTemplateHasAllCorrespondingCardsSuspended.class);

  @Override
  void runDQ() {
    final List<Long> ALL_SUSPENDED_TEMPLATES_ALLOWED_MODEL_IDS = Lists.newArrayList(
        1444077615554L, //Elements shared deck
        1532358549255L, //spanish sentences shared deck
        1586000000000L, // Spotify Track
        1587000000000L, // Spotify Artist
        1588000000000L, // Video
        1589000000000L, // Video Person
        1604800000000L, // Readwise Highlight
        1607000000000L, // Beer
        1622000000000L, // Venue
        1624000000000L, // Basic
        0L
    );
    modelsInUse.stream()
        .filter(model -> !model.getName().contains("Cloze"))
        .filter(model -> !ALL_SUSPENDED_TEMPLATES_ALLOWED_MODEL_IDS.contains(model.getId()))
        .forEach(model -> {
          Long modelId = model.getId();
          List<Template> modelTemplates = templatesInUse.stream()
              .filter(template -> template.getModel_id().equals(modelId))
              .sorted(Comparator.comparing(Template::getId))
              .toList();
          List<Card> allCards = allNotes.stream()
              .filter(note -> note.getModel_id().equals(modelId))
              .flatMap(note -> cardsByNoteId.get(note.getId()).stream())
              .toList();

          List<Integer> templateOrdinalsActive = allCards.stream()
              .filter(card -> card.getQueue() != Card.Queue.SUSPENDED)
              .map(card -> card.getTemplate_ordinal().intValue())
              .sorted()
              .distinct()
              .toList();

          if (templateOrdinalsActive.size() != modelTemplates.size()) {
            modelTemplates.stream()
                .filter(template -> !templateOrdinalsActive.contains(template.getOrdinal().intValue()))
                .forEach(template ->
                    dqInformation.warn("Note '%s': no active (non-suspended) cards for template '%s'",
                        model.getName(), template.getName()));
          }
        });
  }

}
