package com.robertsanek.data.etl.remote.trello;

import java.time.ZonedDateTime;

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
  @Column(name = "board_id")
  private String boardId;
  @Column(name = "list_id")
  private String listId;
  private String name;
  @Column(length = MAX_LENGTH)
  private String description;
  private boolean closed;
  @Column(name = "last_activity")
  private ZonedDateTime lastActivity;

  public String getBoardId() {
    return boardId;
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

  public boolean isClosed() {
    return closed;
  }

  public ZonedDateTime getLastActivity() {
    return lastActivity;
  }

  public static final class TrelloCardBuilder {

    private String id;
    private String boardId;
    private String name;
    private String description;
    private boolean closed;
    private ZonedDateTime lastActivity;

    private TrelloCardBuilder() {}

    public static TrelloCardBuilder aTrelloCard() { return new TrelloCardBuilder(); }

    public TrelloCardBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public TrelloCardBuilder withBoardId(String boardId) {
      this.boardId = boardId;
      return this;
    }

    public TrelloCardBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public TrelloCardBuilder withDescription(String description) {
      this.description = description;
      return this;
    }

    public TrelloCardBuilder withClosed(boolean closed) {
      this.closed = closed;
      return this;
    }

    public TrelloCardBuilder withLastActivity(ZonedDateTime lastActivity) {
      this.lastActivity = lastActivity;
      return this;
    }

    public TrelloCard build() {
      TrelloCard trelloCard = new TrelloCard();
      trelloCard.boardId = this.boardId;
      trelloCard.name = this.name;
      trelloCard.lastActivity = this.lastActivity;
      trelloCard.id = this.id;
      trelloCard.closed = this.closed;
      trelloCard.description = this.description;
      return trelloCard;
    }
  }
}
