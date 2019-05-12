package com.robertsanek.data.etl.remote.trello;

import com.robertsanek.data.etl.Etl;

import java.util.List;
import java.util.stream.Collectors;

public class BoardEtl extends Etl<TrelloBoard> {

  @Override
  public List<TrelloBoard> getObjects() {
    return TrelloConnector.getApi().getMemberBoards("me").stream()
        .map(board -> TrelloBoard.TrelloBoardBuilder.aTrelloBoard()
            .withId(board.getId())
            .withName(board.getName())
            .withDesc(board.getDesc())
            .withClosed(board.isClosed())
            .build())
        .collect(Collectors.toList());
  }

}
