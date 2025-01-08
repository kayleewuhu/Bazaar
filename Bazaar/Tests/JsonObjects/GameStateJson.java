package Tests.JsonObjects;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import Common.Data.Card;
import Common.Data.CardCollection;
import Referee.Game_State;
import Common.Data.PebbleCollection;
import Referee.PlayerInfo;

public class GameStateJson {
  private List<String> bank;
  private List<CardJson> visibles;
  private List<CardJson> cards;
  private List<PlayerJson> players;

  public GameStateJson(Game_State gameState) {
    this.bank = new PebbleCollectionJson(gameState.getBank()).toListOfString();
    this.visibles = gameState.getCards().getVisibleCardsCopy().stream()
            .map(CardJson::new).collect(Collectors.toList());
    this.cards = gameState.getCards().getInvisibleCardsCopy().stream()
            .map(CardJson::new).collect(Collectors.toList());
    this.players = gameState.getPlayersAsList().stream()
            .map(PlayerJson::new).collect(Collectors.toList());
  }

  public Game_State parseIntoObject() {
    PebbleCollection bankPebbleCollection = new PebbleCollectionJson(this.bank).parseIntoObject();
    List<Card> visibleCards = this.visibles.stream().map(CardJson::parseIntoObject).toList();
    List<Card> invisibleCards = this.cards.stream().map(CardJson::parseIntoObject).toList();
    CardCollection cardCollection = new CardCollection(visibleCards, invisibleCards);
    Deque<PlayerInfo> playerDeque = this.players.stream().map(PlayerJson::parseIntoObject)
            .collect(Collectors.toCollection(ArrayDeque::new));
    return new Game_State(bankPebbleCollection, cardCollection, playerDeque);
  }

}
