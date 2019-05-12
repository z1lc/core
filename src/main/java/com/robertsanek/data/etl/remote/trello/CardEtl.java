package com.robertsanek.data.etl.remote.trello;

import com.robertsanek.data.etl.Etl;

import java.util.List;
import java.util.stream.Collectors;

public class CardEtl extends Etl<TrelloCard> {

  @Override
  public List<TrelloCard> getObjects() {
    return new BoardEtl().getObjects().stream()
        .map(TrelloBoard::getId)
        .flatMap(boardId -> TrelloConnector.getApi().getBoardCards(boardId).stream())
        .map(card -> TrelloCard.TrelloCardBuilder.aTrelloCard()
            .withId(card.getId())
            .withName(card.getName())
            .withDesc(card.getDesc())
            .withIdBoard(card.getIdBoard())
            .build())
        .collect(Collectors.toList());
  }

}
