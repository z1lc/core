package com.robertsanek.data.etl.local.sqllite.anki;

import java.time.ZonedDateTime;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

@Entity
@Table(name = "anki_cards")
public class Card {

  private static final Log log = Logs.getLog(Card.class);

  @Id
  Long id;
  ZonedDateTime created_at;
  Long note_id;
  Long deck_id;
  Long template_ordinal;
  ZonedDateTime last_modified_at;
  @Enumerated(value = EnumType.STRING)
  Type type;
  @Enumerated(value = EnumType.STRING)
  Queue queue;
  Long interval_days;
  Long factor;
  Long number_reviews;
  Long number_lapses;

  public static Log getLog() {
    return log;
  }

  public Long getId() {
    return id;
  }

  public ZonedDateTime getCreated_at() {
    return created_at;
  }

  public Long getNote_id() {
    return note_id;
  }

  public Long getDeck_id() {
    return deck_id;
  }

  public Long getTemplate_ordinal() {
    return template_ordinal;
  }

  public ZonedDateTime getLast_modified_at() {
    return last_modified_at;
  }

  public Type getType() {
    return type;
  }

  public Queue getQueue() {
    return queue;
  }

  public Long getInterval_days() {
    return interval_days;
  }

  public Long getFactor() {
    return factor;
  }

  public Long getNumber_reviews() {
    return number_reviews;
  }

  public Long getNumber_lapses() {
    return number_lapses;
  }

  // KEEP IN ORDER!
  public enum Type {
    SUSPENDED(0),
    LEARNING(1),
    NORMAL(2),
    BURIED(3);

    private final Integer value;

    Type(int value) {
      this.value = value;
    }

    public static Type fromValue(long id, int value) {
      return Arrays.stream(Type.values())
          .filter(q -> q.value == value)
          .findAny()
          .orElseGet(() -> {
            log.error(
                "Card cid:%s has int value of %s for type, but no corresponding Type exists. Returning %s as default.",
                id, value, NORMAL);
            return NORMAL;
          });
    }
  }

  // KEEP IN ORDER!
  public enum Queue {
    MANUALLY_BURIED(-3),
    AUTOMATICALLY_BURIED(-2),
    SUSPENDED(-1),
    UNSEEN(0),
    LEARN(1),
    REVIEW(2),
    LEARN2(3), //not sure why there are 2 values for learn, but who cares
    UNKNOWN(999);

    private final Integer value;

    Queue(int value) {
      this.value = value;
    }

    public static Queue fromValue(long id, int value) {
      return Arrays.stream(Queue.values())
          .filter(q -> q.value == value)
          .findAny()
          .orElseGet(() -> {
            log.error("Card cid:%s has int value of %s for queue, but no corresponding Queue exists.", id, value);
            return UNKNOWN;
          });
    }
  }

  public static final class CardBuilder {

    Long id;
    ZonedDateTime created_at;
    Long note_id;
    Long deck_id;
    Long template_ordinal;
    ZonedDateTime last_modified_at;
    Type type;
    Queue queue;
    Long interval_days;
    Long factor;
    Long number_reviews;
    Long number_lapses;

    private CardBuilder() {}

    public static CardBuilder aCard() { return new CardBuilder(); }

    public CardBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public CardBuilder withCreated_at(ZonedDateTime created_at) {
      this.created_at = created_at;
      return this;
    }

    public CardBuilder withNote_id(Long note_id) {
      this.note_id = note_id;
      return this;
    }

    public CardBuilder withDeck_id(Long deck_id) {
      this.deck_id = deck_id;
      return this;
    }

    public CardBuilder withTemplate_ordinal(Long template_ordinal) {
      this.template_ordinal = template_ordinal;
      return this;
    }

    public CardBuilder withLast_modified_at(ZonedDateTime last_modified_at) {
      this.last_modified_at = last_modified_at;
      return this;
    }

    public CardBuilder withType(Type type) {
      this.type = type;
      return this;
    }

    public CardBuilder withQueue(Queue queue) {
      this.queue = queue;
      return this;
    }

    public CardBuilder withInterval_days(Long interval_days) {
      this.interval_days = interval_days;
      return this;
    }

    public CardBuilder withFactor(Long factor) {
      this.factor = factor;
      return this;
    }

    public CardBuilder withNumber_reviews(Long number_reviews) {
      this.number_reviews = number_reviews;
      return this;
    }

    public CardBuilder withNumber_lapses(Long number_lapses) {
      this.number_lapses = number_lapses;
      return this;
    }

    public Card build() {
      Card card = new Card();
      card.deck_id = this.deck_id;
      card.number_lapses = this.number_lapses;
      card.created_at = this.created_at;
      card.last_modified_at = this.last_modified_at;
      card.template_ordinal = this.template_ordinal;
      card.interval_days = this.interval_days;
      card.factor = this.factor;
      card.note_id = this.note_id;
      card.id = this.id;
      card.queue = this.queue;
      card.type = this.type;
      card.number_reviews = this.number_reviews;
      return card;
    }
  }
}
