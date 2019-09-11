package com.robertsanek.data.quality.anki;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.robertsanek.data.derived.anki.CardNewReps;
import com.robertsanek.data.derived.anki.NewRepsPerCardDeriver;

public class NewRepsPerCardDeriverTest {

  @Test
  @Disabled("integration")
  public void name() {
    List<CardNewReps> objects = new NewRepsPerCardDeriver().getObjects().stream()
        .filter(nr -> DataQualityBase.cardByCardId.get(nr.getCard_id())
            .getCreated_at().isAfter(ZonedDateTime.now().minusDays(30)))
        .collect(Collectors.toList());
    long sum = objects.stream()
        .mapToLong(CardNewReps::getReps_to_graduate)
        .sum();
    long count = objects.size();
    System.out.println((double) sum / count);
  }

  @Test
  @Disabled("integration")
  public void integration2() {
    List<CardNewReps> ret = new NewRepsPerCardDeriver().getObjects();
    long correct = ret.stream()
        .filter(CardNewReps::isGotFirstReviewAfterGraduationCorrect)
        .filter(card -> card.getGraduating_interval() == 2)
        .count();
    long total = ret.stream()
        .filter(CardNewReps::isReviewedGraduationRepetitionOnTime)
        .filter(card -> card.getGraduating_interval() == 2)
        .count();
    System.out.println((double) correct / total);
  }

}