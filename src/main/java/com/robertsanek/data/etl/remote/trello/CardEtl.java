package com.robertsanek.data.etl.remote.trello;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.google.inject.Inject;
import com.julienvey.trello.domain.Argument;
import com.robertsanek.data.etl.Etl;

public class CardEtl extends Etl<TrelloCard> {

  @Inject TrelloConnector trelloConnector;
  @Inject BoardEtl boardEtl;

  @Override
  public List<TrelloCard> getObjects() {
    return boardEtl.getObjects().stream()
        .map(TrelloBoard::getId)
        .flatMap(boardId -> trelloConnector.getApi().getBoardCards(boardId, new Argument("filter", "all")).stream())
        .map(card -> TrelloCard.TrelloCardBuilder.aTrelloCard()
            .withId(card.getId())
            .withBoardId(card.getIdBoard())
            .withListId(card.getIdList())
            .withName(card.getName())
            .withDescription(card.getDesc())
            .withClosed(card.isClosed())
            .withLastActivity(ZonedDateTime.ofInstant(card.getDateLastActivity().toInstant(), ZoneId.systemDefault()))
            .build())
        .toList();
  }

}
