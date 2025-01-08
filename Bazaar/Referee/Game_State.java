package Referee;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import Common.Data.CardCollection;
import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Data.PebbleCollection;
import Common.IGameMode;
import Common.Pair;
import Common.Rule_Book;
import Common.Turn_State;
import Player.PlayerAPI;
import Server.Player;

// This class represents a single state of the game, intended to be used by the Referee.
public class Game_State {
  // the pebbles in the bank
  private final PebbleCollection bank;
  // the cards in the game
  private final CardCollection cards;
  // the players in the game, in order of turn
  private final Deque<PlayerInfo> players;

  public Game_State(PebbleCollection bank, CardCollection cards, Deque<PlayerInfo> players) {
    this.bank = bank.getPebbleCollectionCopy();
    this.cards = cards.getCardCollectionCopy();
    this.players = new ArrayDeque<>();
    for (PlayerInfo player : players) {
      this.players.add(player.getPlayerInfoCopy());
    }
  }

  // returns a copy of the bank's pebbles in this state
  public PebbleCollection getBank() {
    return this.bank.getPebbleCollectionCopy();
  }

  // returns a copy of the cards in this state
  public CardCollection getCards() {
    return this.cards.getCardCollectionCopy();
  }

  // returns a copy of the players in this state
  public Deque<PlayerInfo> getPlayersCopy() {
    return new ArrayDeque<>(this.getPlayersAsList());
  }

  // returns the players as a list
  public List<PlayerInfo> getPlayersAsList() {
    List<PlayerInfo> playersList = new ArrayList<>();
    for (PlayerInfo player : this.players) {
      playersList.add(player.getPlayerInfoCopy());
    }
    return playersList;
  }

  // returns the active player
  public PlayerInfo getActivePlayerCopy() {
    return this.players.getFirst().getPlayerInfoCopy();
  }

  // removes the active player from the game state
  public Game_State removeActivePlayer() {
    this.players.pop();
    return new Game_State(this.bank, this.cards, this.players);
  }

  // rotates the active player in the game
  public Game_State rotateActivePlayer() {
    this.players.add(this.players.pop());
    return new Game_State(this.bank, this.cards, this.players);
  }

  // performs the given action (draw pebble or exchanges) on this game state
  // throws exception if action cannot be done
  public Game_State performDrawPebbleOrExchanges(DrawPebbleOrExchanges action, EquationTable equations)
          throws IllegalArgumentException {
    Turn_State turnState = this.getTurnState();
    Optional<Pair<PebbleCollection, PebbleCollection>> responseWalletAndBank =
            action.attemptAction(turnState, equations);
    // If performing action is unsuccessful, something is terribly wrong
    if (responseWalletAndBank.isEmpty()) {
      throw new IllegalArgumentException("Invalid Draw Pebble or Exchanges action was performed onto Game State.");
    }
    CardCollection updatedCards = this.cards;
    // if trades are made, remove the bottom card from the invisible cards
    if (action instanceof Exchanges) {
      updatedCards = this.cards.removeBottomInvisibleCardOrAllVisible();
    }

    // updates the active player
    Pair<PebbleCollection, PebbleCollection> resultWalletAndBank = responseWalletAndBank.get();
    PlayerInfo updatedActivePlayer = this.getActivePlayerCopy().setWalletTo(resultWalletAndBank.first);
    Deque<PlayerInfo> updatedPlayers = this.updateActivePlayer(updatedActivePlayer);
    return new Game_State(resultWalletAndBank.second, updatedCards, updatedPlayers);
  }

  // performs the given Card Purchase action on this game state
  // throws exception if action cannot be done
  public Game_State performCardPurchases(CardPurchases action) throws IllegalArgumentException {
    Turn_State turnState = this.getTurnState();
    Optional<Pair<PebbleCollection, PebbleCollection>> responseWalletAndBank =
            action.attemptAction(turnState);
    // If performing action is unsuccessful, something is terribly wrong
    if (responseWalletAndBank.isEmpty()) {
      throw new IllegalArgumentException("Invalid Card Purchases action was performed onto Game State.");
    }
    Pair<PebbleCollection, PebbleCollection> resultWalletAndBank = responseWalletAndBank.get();
    // Update the active player
    PlayerInfo updatedActivePlayer = this.getActivePlayerCopy().
            setWalletTo(resultWalletAndBank.first).
            addCardsToPurchased(action.getSequenceOfCardPurchasesCopy());

    updatedActivePlayer = updatedActivePlayer.addPointsToScore(action.totalPointsWorth(turnState.activePlayer().getWalletCopy()));
    Deque<PlayerInfo> updatedPlayers = this.updateActivePlayer(updatedActivePlayer);
    // Update the Card Collection
    CardCollection updatedCardCollection = this.cards.removeVisibleCards(action.getSequenceOfCardPurchasesCopy());
    updatedCardCollection = updatedCardCollection.replaceAcquiredVisibleCards();
    return new Game_State(resultWalletAndBank.second, updatedCardCollection, updatedPlayers);
  }

  // returns the deque of players with the active player updated
  private Deque<PlayerInfo> updateActivePlayer(PlayerInfo updatedActivePlayer) {
    Deque<PlayerInfo> updatedDeque = this.getPlayersCopy();
    updatedDeque.pop();
    updatedDeque.push(updatedActivePlayer);
    return updatedDeque;
  }
    // is the game over?
  public boolean isGameOver() {
    return Rule_Book.isTheGameOver(this);
  }

  // returns the players with the most amount of points in this game state
  public List<PlayerInfo> playersWithMostPoints() {
    int maxPoints = 0;
    List<PlayerInfo> maxPointsPlayers = new ArrayList<>();

    for (PlayerInfo player : this.players) {
      int playerScore = player.getScore();
      if (playerScore > maxPoints) {
        maxPoints = playerScore;
        maxPointsPlayers = new ArrayList<PlayerInfo>(Arrays.asList(player));
      }
      else if (playerScore == maxPoints) {
        maxPointsPlayers.add(player);
      }
    }
    return maxPointsPlayers;
  }

  // Returns the turn state for the active player given this game state
  // the turn state is the information the player will know about the game state
  public Turn_State getTurnState() {
    return new Turn_State(
            this.bank.getPebbleCollectionCopy(),
            this.getActivePlayerCopy().getPlayerInfoCopy(),
            this.getAllInactivePlayerScores(),
            this.cards.getVisibleCardsCopy());
  }

  // Gets a list of all the player scores excluding the current active player
  private List<Integer> getAllInactivePlayerScores() {
    List<Integer> result = new ArrayList<>();
    this.players.stream().skip(1).forEach((player) -> result.add(player.getScore()));
    return result;
  }

  // Connects the given actual players (the way to reach them) with the game's information
  // about the players
  // throws exception if the player information in the game does not align with the provided actors
  public Game_State connectPlayerInfosToPlayerAPIs(List<PlayerAPI> playerConnections) throws IllegalArgumentException {
    if (this.players.size() != playerConnections.size()) {
      throw new IllegalArgumentException("Number of Player Infos and number of actual Players don't match.");
    }
    Deque<PlayerInfo> connectedPlayerInfos = new ArrayDeque<>();
    int i = 0;
    for (PlayerInfo playerInfo : this.players) {
      PlayerInfo connectedPlayer = playerInfo.connectWithPlayerAPI(playerConnections.get(i));
      connectedPlayerInfos.add(connectedPlayer);
      i++;
    }
    return new Game_State(this.bank, this.cards, connectedPlayerInfos);
  }

  // calculates the final scores of all players based on the given game mode
  // awards bonus points
  public Game_State calculateFinalScores(IGameMode gameMode) {
    Deque<PlayerInfo> updatedPlayers = new ArrayDeque<>();

    for (PlayerInfo playerInfo : this.players) {
      PlayerInfo updatedPlayer = playerInfo.addPointsToScore(Rule_Book.calculateBonus(gameMode, playerInfo));
      updatedPlayers.add(updatedPlayer);
    }

    return new Game_State(this.bank, this.cards, updatedPlayers);
  }

  // is this game state equal to the given object?
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Game_State otherGameState)) {
      return false;
    }
    return this.bank.equals(otherGameState.bank) && this.cards.equals(otherGameState.cards)
            && this.players.equals(otherGameState.players);
  }
}