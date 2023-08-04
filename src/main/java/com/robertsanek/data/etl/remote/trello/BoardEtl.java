package com.robertsanek.data.etl.remote.trello;

import com.google.inject.Inject;
import com.robertsanek.data.etl.Etl;

import java.util.List;

public class BoardEtl extends Etl<TrelloBoard> {

  @Inject TrelloConnector trelloConnector;

  @Override
  public List<TrelloBoard> getObjects() {
    return trelloConnector.getApi().getMemberBoards("me").stream()
        .map(board -> TrelloBoard.TrelloBoardBuilder.aTrelloBoard()
            .withId(board.getId())
            .withName(board.getName())
            .withDesc(board.getDesc())
            .withClosed(board.isClosed())
            .build())
        .toList();
  }

}
