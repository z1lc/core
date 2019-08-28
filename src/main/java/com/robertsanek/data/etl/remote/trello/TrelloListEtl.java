package com.robertsanek.data.etl.remote.trello;

import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.julienvey.trello.domain.Argument;
import com.robertsanek.data.etl.Etl;

public class TrelloListEtl extends Etl<TrelloList> {

  @Inject TrelloConnector trelloConnector;

  @Override
  public List<TrelloList> getObjects() throws Exception {
    return new BoardEtl().getObjects().stream()
        .map(TrelloBoard::getId)
        .flatMap(boardId -> trelloConnector.getApi().getBoardLists(boardId, new Argument("filter", "all")).stream())
        .map(list -> TrelloList.TrelloListBuilder.aTrelloList()
            .withId(list.getId())
            .withBoardId(list.getIdBoard())
            .withName(list.getName())
            .withClosed(list.isClosed())
            .withPos(list.getPos())
            .build())
        .collect(Collectors.toList());
  }
}
