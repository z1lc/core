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
  String name;
  @Deprecated Long option_group_id;
  @Deprecated boolean collapsed;
  @Column(length = FIELDS_LIMIT)
  @Deprecated String description;

  public Long getId() {
    return id;
  }

  @Deprecated
  public Long getOption_group_id() {
    return option_group_id;
  }

  @Deprecated
  public boolean isCollapsed() {
    return collapsed;
  }

  public String getName() {
    return name;
  }

  @Deprecated
  public String getDescription() {
    return description;
  }

  public static final class DeckBuilder {

    Long id;
    String name;
    @Deprecated Long option_group_id;
    @Deprecated boolean collapsed;
    @Deprecated String description;

    private DeckBuilder() {}

    public static DeckBuilder aDeck() {
      return new DeckBuilder();
    }

    public DeckBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    @Deprecated
    public DeckBuilder withOption_group_id(Long option_group_id) {
      this.option_group_id = option_group_id;
      return this;
    }

    @Deprecated
    public DeckBuilder withCollapsed(boolean collapsed) {
      this.collapsed = collapsed;
      return this;
    }

    public DeckBuilder withName(String name) {
      this.name = name;
      return this;
    }

    @Deprecated
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