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
import com.robertsanek.data.etl.local.sqllite.anki.Field;
import com.robertsanek.data.etl.local.sqllite.anki.Model;
import com.robertsanek.data.etl.local.sqllite.anki.Review;
import com.robertsanek.data.quality.anki.DataQualityBase;

public class ReviewTimePerCategoryDeriver extends Etl<ReviewTimePerCategory> {

  // should only contain zdone-generated models
  private static final Map<Long, String> MODEL_ID_TO_CATEGORY = ImmutableMap.<Long, String>builder()
      .put(1586000000000L, "Music") // SPOTIFY_TRACK_MODEL_ID
      .put(1587000000000L, "Music") // SPOTIFY_ARTIST_MODEL_ID
      .put(1588000000000L, "Movies & TV") // VIDEO_MODEL_ID
      .put(1589000000000L, "Movies & TV") // VIDEO_PERSON_MODEL_ID
      .put(1604800000000L, "General Knowledge") // READWISE_HIGHLIGHT_CLOZE_MODEL_ID
      .put(1607000000000L, "Drinks") // BEER_MODEL_ID
      .put(1621000000000L, "Music") // SPOTIFY_GENRE_MODEL_ID
      .put(1622000000000L, "Venues") // VENUE_MODEL_ID
      .build();

  private static final Map<String, String> BASIC_ID_PREFIXES_TO_CATEGORY  = ImmutableMap.<String, String>builder()
      .put("zdone:car:", "General Knowledge")
      .put("zdone:dawn:", "General Knowledge")
      .put("zdone:chemistry:", "General Knowledge")
      .put("zdone:highlight:", "General Knowledge")
      .put("zdone:reminder:", "General Knowledge")
      .put("zdone:river:", "General Knowledge")
      .put("zdone:wikipedia:", "General Knowledge")
      .put("zdone:bike_lock:", "General Knowledge")
      .put("zdone:gened:", "General Knowledge")
      .put("zdone:mlb:", "Sports")
      .put("zdone:nba:", "Sports")
      .put("zdone:nfl:", "Sports")
      .put("zdone:nhl:", "Sports")
      .put("zdone:cheese:", "Food")
      .put("zdone:coffee:", "Food")
      .put("zdone:pasta:", "Food")
      .put("spotify:artist:", "Music")
      .put("spotify:track:", "Music")
      .put("zdone:genre:", "Music")
      .put("zdone:cocktail:", "Drinks")
      .put("zdone:beer:", "Drinks")
      .put("zdone:exercise:", "Health")
      .put("zdone:ingredient:", "Health")
      .put("zdone:intermittent_fasting:", "Health")
      .put("zdone:person:", "Movies & TV")
      .put("zdone:video:", "Movies & TV")
      .put("zdone:cat:", "Pets")
      .put("zdone:dog:", "Pets")
      .put("zdone:art:", "Art")
      .put("zdone:philosopher:", "Philosophy")
      .put("zdone:historical_event:", "History")
      .put("zdone:venue:", "Venues")
      .put("zdone:incident:", "Work")
      .put("zdone:spanish", "Language")
      .put("zdone:company:", "Companies")
      .put("zdone:perfume:", "Perfumes")
      .put("zdone:watch:", "Watches")
      .build();

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

      .put("Spotify", "Music")
      .put("Venue", "Venues")
      .put("Work of Art", "Art")
      .put("Movie/TV", "Movies & TV")
      .put("Music Album", "Music")

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
      .put("\uD83D\uDCBD software", "Computing")
      .put("<img src=\"database%20%281%29.png\" height=\"24px\"> data engineering", "Computing")
      .put("data systems", "Computing")
      .put("Silicon Valley", "Computing")
      .put("Java", "Computing")
      .put("computing", "Computing")

      .put("author", "Books")
      .put("artist", "Music")
      .put("actor", "Movies & TV")
      .put("TV", "Movies & TV")
      .put("music festival", "Music")

      .put("US politics", "General Knowledge")
      .put("Memory cognitive bias", "General Knowledge")
      .put("friends", "General Knowledge")
      .put("int'l politics", "General Knowledge")
      .put("US business", "General Knowledge")
      .put("US businesses", "General Knowledge")
      .put("researcher", "General Knowledge")
      .put("credit card churning", "General Knowledge")

      .put("drugs", "Health")
      .put("\uD83D\uDC8A drugs", "Health")
      .put("shrooms", "Health")
      .put("air cleaners", "Health")
      .put("air purifiers", "Health")
      .put("cannabis", "Health")
      .put("medicine", "Health")
      .put("\uD83E\uDEC0 medicine", "Health")
      .put("nutrition", "Health")
      .put("\uD83C\uDF4F nutrition", "Health")

      .put("\uD83C\uDDE8\uD83C\uDDFF čeština", "Language")

      .put("WF", "Work")
      .put("Stripe", "Work")
      .put("\uD83D\uDCB3 Stripe", "Work")
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
        .toList();
  }

  @VisibleForTesting
  public String reviewToCategory(Review review) {
    return Optional.ofNullable(DataQualityBase.getCardsByCardId().get(review.getCard_id()))
        .map(card -> DataQualityBase.getNotesByNoteId().get(card.getNote_id()))
        .map(note -> {
          Model model = Iterables.getOnlyElement(DataQualityBase.getModelsByModelId().get(note.getModel_id()));

          //First match by model id
          String maybeModelIdBasedCategory = MODEL_ID_TO_CATEGORY.get(note.getModel_id());
          if (maybeModelIdBasedCategory != null) {
            return maybeModelIdBasedCategory;
          }

          //Basic model ID
          if (note.getModel_id() == 1624000000000L) {
            for (Map.Entry<String, String> id_prefix_to_category : BASIC_ID_PREFIXES_TO_CATEGORY.entrySet()) {
              if (note.getFields().contains(id_prefix_to_category.getKey())) {
                return id_prefix_to_category.getValue();
              }
            }
          }

          //second match by tag
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
          int contextIndex = DataQualityBase.getFieldsByModelId().get(model.getId()).stream()
              .map(Field::getName)
              .toList()
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
