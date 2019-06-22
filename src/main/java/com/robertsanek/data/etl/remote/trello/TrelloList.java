package com.robertsanek.data.etl.remote.trello;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trello_lists")
public class TrelloList {

  @Id
  private String id;
  @Column(name = "board_id")
  private String boardId;
  private String name;
  private boolean closed;
  private int pos;

  public String getId() {
    return id;
  }

  public String getBoardId() {
    return boardId;
  }

  public String getName() {
    return name;
  }

  public boolean isClosed() {
    return closed;
  }

  public int getPos() {
    return pos;
  }

  public static final class TrelloListBuilder {

    private String id;
    private String boardId;
    private String name;
    private boolean closed;
    private int pos;

    private TrelloListBuilder() {}

    public static TrelloListBuilder aTrelloList() {
      return new TrelloListBuilder();
    }

    public TrelloListBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public TrelloListBuilder withBoardId(String boardId) {
      this.boardId = boardId;
      return this;
    }

    public TrelloListBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public TrelloListBuilder withClosed(boolean closed) {
      this.closed = closed;
      return this;
    }

    public TrelloListBuilder withPos(int pos) {
      this.pos = pos;
      return this;
    }

    public TrelloList build() {
      TrelloList trelloList = new TrelloList();
      trelloList.pos = this.pos;
      trelloList.boardId = this.boardId;
      trelloList.name = this.name;
      trelloList.id = this.id;
      trelloList.closed = this.closed;
      return trelloList;
    }
  }
}
