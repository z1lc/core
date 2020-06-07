package com.robertsanek.data.derived.anki;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.data.etl.local.sqllite.anki.Model;
import com.robertsanek.data.etl.local.sqllite.anki.Review;
import com.robertsanek.data.quality.anki.DataQualityBase;

public class ReviewTimePerCategoryDeriver extends Etl<ReviewTimePerCategory> {

  private static final Map<String, String> MODEL_NAME_TO_CATEGORY = ImmutableMap.<String, String>builder()
      .put("Interview Question", "Computing")
      .put("Software", "Computing")
      .put("Programming Language Function", "Computing")
      .put("AWS Service", "Computing")
      .put("Algorithm", "Computing")
      .put("Command-Line", "Computing")
      .put("_IEEE 754", "Computing")
      .put("_Java Primitive Data Type", "Computing")
      .put("_LaTeX", "Computing")
      .put("_Linux Directory", "Computing")
      .put("_Logic Gates", "Computing")
      .put("_OSI Model Network Layers", "Computing")
      .put("_Parallelization Library", "Computing")
      .put("_Power of Two", "Computing")
      .put("_SI Prefix", "Computing")
      .put("_Unix command types", "Computing")
      .put("R Data Structure", "Computing")

      .put("Spotify", "Culture")
      .put("Venue", "Culture")
      .put("Work of Art", "Culture")
      .put("Movie/TV", "Culture")
      .put("Music Album", "Culture")

      .put("Currency", "General Knowledge")
      .put("Elements", "General Knowledge")
      .put("Geography", "General Knowledge")
      .put("Supreme Court Decision", "General Knowledge")
      .put("_Business Type", "General Knowledge")
      .put("_Number Sets", "General Knowledge")
      .put("_Poker Hands", "General Knowledge")
      .put("_Resin identification code", "General Knowledge")
      .put("_Shoe Construction", "General Knowledge")
      .put("Airport IATA", "General Knowledge")
      .put("National Park", "General Knowledge")
      .put("Option", "General Knowledge")
      .put("Robo Advisor", "General Knowledge")
      .put("_US Amendment", "General Knowledge")
      .put("NCAA Division I Conference", "General Knowledge")

      .put("Disease", "Health")
      .put("Drug", "Health")
      .put("_BPM", "Health")
      .put("_Nutrient", "Health")
      .put("_Water Treatment Method", "Health")

      .put("Alphabets", "Language")
      .put("z spanish sentences deck", "Language")
      .put("IPA Pronunciation", "Language")

      .put("_WF Data System", "Work")
      .put("_WF Engineering Function", "Work")
      .put("_WF Product Management Area", "Work")
      .put("_WF Stoplight", "Work")
      .put("_WF Subprograms", "Work")
      .build();

  private static final Map<String, String> TAG_TO_CATEGORY = ImmutableMap.<String, String>builder()
      .put("z::Computer_Science", "Computing")
      .put("Shared_From_Others::Will::DDIA", "Computing")
      .put("z::Computer_Science::Interview_Prep", "Computing")
      .put("z::Computer_Science::Powers_of_Two", "Computing")
      .put("z::Computer_Science::Networking", "Computing")
      .put("Shared_From_Others::Will::Dynamo", "Computing")

      .put("z::Culture", "Culture")
      .put("z::Other::Coachella", "Culture")
      .put("India", "Culture")
      .put("z::Japan", "Culture")

      .put("z::General_Knowledge::The_Office", "General Knowledge")
      .put("z::Finance", "General Knowledge")
      .put("z::General_Knowledge", "General Knowledge")
      .put("Shared_From_Others::Quizlet::Civics", "General Knowledge")
      .put("z::Mathematics", "General Knowledge")
      .put("z::Memory", "General Knowledge")
      .put("z::Other::Quotes", "General Knowledge")
      .put("z::Book_Summaries::Bad_Blood", "General Knowledge")
      .put("Shared_From_Others::Will::1984", "General Knowledge")
      .put("z::Book_Summaries::Corporate_Confidential", "General Knowledge")
      .put("Shared_From_Others::Will::Evicted", "General Knowledge")
      .put("z::Other::NBA_Players", "General Knowledge")
      .put("z::Book_Summaries::Measure_What_Matters", "General Knowledge")

      .put("z::Nutrition", "Health")
      .put("z::Health", "Health")
      .put("z::Medicine", "Health")
      .put("z::Exercise", "Health")
      .put("z::Drugs", "Health")

      .put("z::Languages::Spanish", "Language")

      .put("z::Work::Stripe", "Work")
      .put("z::Other::Wealthfront_People", "Work")
      .put("z::Other::Conductor_People", "Work")
      .build();

  private static final Map<String, String> CONTEXT_TO_CATEGORY = ImmutableMap.<String, String>builder()
      .put("software", "Computing")
      .put("data systems", "Computing")
      .put("Silicon Valley", "Computing")
      .put("Java", "Computing")
      .put("computing", "Computing")

      .put("author", "Culture")
      .put("artist", "Culture")
      .put("actor", "Culture")
      .put("TV", "Culture")
      .put("music festival", "Culture")

      .put("US politics", "General Knowledge")
      .put("Memory cognitive bias", "General Knowledge")
      .put("friends", "General Knowledge")
      .put("int'l politics", "General Knowledge")
      .put("US business", "General Knowledge")
      .put("US businesses", "General Knowledge")
      .put("researcher", "General Knowledge")
      .put("credit card churning", "General Knowledge")

      .put("drugs", "Health")
      .put("shrooms", "Health")
      .put("air cleaners", "Health")
      .put("air purifiers", "Health")
      .put("cannabis", "Health")
      .put("medicine", "Health")
      .put("nutrition", "Health")

      .put("WF", "Work")
      .put("Stripe", "Work")
      .build();

  private final AtomicLong logicalClock = new AtomicLong(1);

  @Override
  public List<ReviewTimePerCategory> getObjects() {
    List<Review> reviews = DataQualityBase.getAllReviews();
    return reviews.stream()
        .collect(Collectors.groupingBy(review -> review.getCreated_at()
            .withNano(0)
            .withSecond(0)
            .withMinute(0)
            .withHour(0)))
        .entrySet()
        .stream()
        .flatMap(pair -> {
          ZonedDateTime day = pair.getKey();
          List<Review> reviewsOnDay = pair.getValue();
          return reviewsOnDay.stream()
              .collect(Collectors.groupingBy(this::reviewToCategory))
              .entrySet()
              .stream()
              .map(entry -> {
                return ReviewTimePerCategory.ReviewTimePerCategoryBuilder.aReviewTimePerTag()
                    .withId(logicalClock.getAndIncrement())
                    .withDay(day)
                    .withCategory(entry.getKey())
                    .withTime_ms(entry.getValue().stream().mapToLong(Review::getTime_ms).sum())
                    .build();
              });
        })
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  public String reviewToCategory(Review review) {
    return Optional.ofNullable(DataQualityBase.getCardsByCardId().get(review.getCard_id()))
        .map(card -> DataQualityBase.getNotesByNoteId().get(card.getNote_id()))
        .map(note -> {
          Model model = Iterables.getOnlyElement(DataQualityBase.getModelsByModelId().get(note.getModel_id()));

          //First match by tag
          for (Map.Entry<String, String> nameAndCategory : TAG_TO_CATEGORY.entrySet()) {
            String tagNameSubstringToFind = nameAndCategory.getKey();
            Optional<String> maybeTag = DataQualityBase.splitCsvIntoCommaSeparatedList(note.getTags()).stream()
                .filter(tag -> tag.contains(tagNameSubstringToFind))
                .findAny();
            if (maybeTag.isPresent()) {
              return nameAndCategory.getValue();
            }
          }

          //Then match by context
          List<String> fields = DataQualityBase.splitCsvIntoCommaSeparatedList(note.getFields());
          int contextIndex = DataQualityBase.splitCsvIntoCommaSeparatedList(model.getFields())
              .indexOf("Context \uD83D\uDCA1");
          if (contextIndex > -1 && contextIndex < fields.size()) {
            String maybeContext = fields.get(contextIndex);
            if (CONTEXT_TO_CATEGORY.containsKey(maybeContext)) {
              return CONTEXT_TO_CATEGORY.get(maybeContext);
            }
          }

          //Finally fall back to model name
          for (Map.Entry<String, String> nameAndCategory : MODEL_NAME_TO_CATEGORY.entrySet()) {
            if (model.getName().contains(nameAndCategory.getKey())) {
              return nameAndCategory.getValue();
            }
          }

          return "Other";
        })
        .orElse("Other");
  }

}
