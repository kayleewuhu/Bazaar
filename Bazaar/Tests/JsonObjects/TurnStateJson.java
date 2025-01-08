package Tests.JsonObjects;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import Common.Data.Card;
import Common.Data.PebbleCollection;
import Common.Turn_State;
import Referee.PlayerInfo;

public class TurnStateJson {
  private List<String> bank;
  private List<CardJson> cards;
  private PlayerJson active;
  private List<Integer> scores;

  public TurnStateJson(Turn_State ts) {
    this.bank = new PebbleCollectionJson(ts.bankPebbles()).toListOfString();
    this.cards = ts.visibleCards().stream().map(CardJson::new).collect(Collectors.toList());
    this.active = new PlayerJson(ts.activePlayer());
    this.scores = ts.remainingPlayerScores();
  }

  public Turn_State parseIntoObject() {
    PebbleCollection bankPebbleCollection = new PebbleCollectionJson(this.bank).parseIntoObject();
    PlayerInfo activePlayer = this.active.parseIntoObject();
    List<Integer> remainingPlayerScores = this.scores;
    List<Card> visibleCards = this.cards.stream().map(CardJson::parseIntoObject).toList();
    return new Turn_State(bankPebbleCollection, activePlayer, remainingPlayerScores, visibleCards);
  }
}
