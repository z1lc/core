package com.robertsanek.data.etl.remote.trello;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trello_boards")
public class TrelloBoard {

  @Id
  private String id;
  private String name;
  private String description;
  private boolean closed;

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

  public static final class TrelloBoardBuilder {
    private String id;
    private String name;
    private String desc;
    private boolean closed;

    private TrelloBoardBuilder() {
    }

    public static TrelloBoardBuilder aTrelloBoard() {
      return new TrelloBoardBuilder();
    }

    public TrelloBoardBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public TrelloBoardBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public TrelloBoardBuilder withDesc(String desc) {
      this.desc = desc;
      return this;
    }

    public TrelloBoardBuilder withClosed(boolean closed) {
      this.closed = closed;
      return this;
    }

    public TrelloBoard build() {
      TrelloBoard trelloBoard = new TrelloBoard();
      trelloBoard.name = this.name;
      trelloBoard.closed = this.closed;
      trelloBoard.description = this.desc;
      trelloBoard.id = this.id;
      return trelloBoard;
    }
  }
}
