package com.robertsanek.data.etl.local.sqllite.anki;

import static com.robertsanek.data.etl.local.sqllite.anki.AnkiEtl.FIELDS_LIMIT;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "anki_decks")
public class Deck {

  @Id
  Long id;
  Long option_group_id;
  boolean collapsed;
  String name;
  @Column(length = FIELDS_LIMIT)
  String description;

  public Long getId() {
    return id;
  }

  public Long getOption_group_id() {
    return option_group_id;
  }

  public boolean isCollapsed() {
    return collapsed;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public static final class DeckBuilder {

    Long id;
    Long option_group_id;
    boolean collapsed;
    String name;
    String description;

    private DeckBuilder() {}

    public static DeckBuilder aDeck() {
      return new DeckBuilder();
    }

    public DeckBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public DeckBuilder withOption_group_id(Long option_group_id) {
      this.option_group_id = option_group_id;
      return this;
    }

    public DeckBuilder withCollapsed(boolean collapsed) {
      this.collapsed = collapsed;
      return this;
    }

    public DeckBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public DeckBuilder withDescription(String description) {
      this.description = description;
      return this;
    }

    public Deck build() {
      Deck deck = new Deck();
      deck.collapsed = this.collapsed;
      deck.description = this.description;
      deck.name = this.name;
      deck.option_group_id = this.option_group_id;
      deck.id = this.id;
      return deck;
    }
  }
}