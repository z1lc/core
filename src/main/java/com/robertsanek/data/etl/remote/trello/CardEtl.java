package com.robertsanek.data.etl.remote.trello;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.julienvey.trello.domain.Argument;
import com.robertsanek.data.etl.Etl;

public class CardEtl extends Etl<TrelloCard> {

  @Override
  public List<TrelloCard> getObjects() {
    return new BoardEtl().getObjects().stream()
        .map(TrelloBoard::getId)
        .flatMap(boardId -> TrelloConnector.getApi().getBoardCards(boardId, new Argument("filter", "all")).stream())
        .map(card -> TrelloCard.TrelloCardBuilder.aTrelloCard()
            .withId(card.getId())
            .withBoardId(card.getIdBoard())
            .withName(card.getName())
            .withDescription(card.getDesc())
            .withClosed(card.isClosed())
            .withLastActivity(ZonedDateTime.ofInstant(card.getDateLastActivity().toInstant(), ZoneId.systemDefault()))
            .build())
        .collect(Collectors.toList());
  }

}
