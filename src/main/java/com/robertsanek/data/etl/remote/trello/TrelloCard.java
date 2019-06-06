package com.robertsanek.data.etl.remote.trello;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trello_cards")
public class TrelloCard {

  private static final int MAX_LENGTH = 10_000;

  @Id
  private String id;
  private String name;
  @Column(length = MAX_LENGTH)
  private String description;
  private String idBoard;

  public String getIdBoard() {
    return idBoard;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public static final class TrelloCardBuilder {

    private String id;
    private String name;
    private String desc;
    private String idBoard;

    private TrelloCardBuilder() {
    }

    public static TrelloCardBuilder aTrelloCard() {
      return new TrelloCardBuilder();
    }

    public TrelloCardBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public TrelloCardBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public TrelloCardBuilder withDesc(String desc) {
      this.desc = desc;
      return this;
    }

    public TrelloCardBuilder withIdBoard(String idBoard) {
      this.idBoard = idBoard;
      return this;
    }

    public TrelloCard build() {
      TrelloCard trelloCard = new TrelloCard();
      trelloCard.name = this.name;
      trelloCard.description = this.desc;
      trelloCard.id = this.id;
      trelloCard.idBoard = this.idBoard;
      return trelloCard;
    }
  }
}
