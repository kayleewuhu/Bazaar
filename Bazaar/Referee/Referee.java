package Referee;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import Common.*;
import Common.Data.CardCollection;
import Common.Data.CardPurchases;
import Common.Data.DrawDeterministicPebble;
import Common.Data.DrawPebble;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Data.PebbleCollection;
import Player.PlayerAPI;

// This class represents the Referee of the game, who interacts with players and updates
// the state of the game.

// Abnormal interactions that are handled by the Referee:
// - players cannot play out of turn (the referee asks for the information from the player when needed)
// - players cannot perform invalid actions (the referee will kick them from the game)

// Abnormal interactions left to be handled by remote communication phase:
// - whether the player returns a proper response to server calls (json)
// - if the player times out and does not respond within an allotted amount of time
public class Referee {
  // the current state of the game
  private Game_State gameState;
  // the equations in the game
  private final EquationTable equationTable;
  // the players that are kicked from the game
  private final List<PlayerInfo> kickedPlayers;
  // are the pebbles going to be drawn deterministically for the draw pebble action?
  private final boolean deterministicGame;
  // the observers of this game
  private final List<Observer> observers = new ArrayList<>();
  // the mode for this game, used to determine bonus points
  private final IGameMode gameMode;

  // creates a game with the given list of players
  public Referee(List<PlayerAPI> players) {
    this(players, new DefaultGameMode());
  }

  public Referee(List<PlayerAPI> players, IGameMode gameMode) {
    this.gameMode = gameMode;
    this.validNumberOfPlayers(players);
    PebbleCollection bank = new PebbleCollection(Constants.STARTING_BANK_NUM_PEBBLES_PER_COLOR);
    CardCollection cards = new CardCollection(new Random());
    Deque<PlayerInfo> playerInfos = this.createPlayerInfos(players);
    this.gameState = new Game_State(bank, cards, playerInfos);
    this.equationTable = new EquationTable(new Random());
    this.kickedPlayers = new ArrayList<>();
    this.deterministicGame = false;
  }

  // for testing purposes
  public Referee(List<PlayerAPI> players, Game_State gameState, EquationTable equationTable, IGameMode gameMode) {
    this.gameState = gameState.connectPlayerInfosToPlayerAPIs(players);
    this.equationTable = equationTable;
    this.kickedPlayers = new ArrayList<>();
    this.deterministicGame = true;
    this.gameMode = gameMode;
  }

  // throws an exception if the number of given players are not within the allowed number of players
  private void validNumberOfPlayers(List<PlayerAPI> players) throws IllegalArgumentException {
    if ((players.size() > Constants.MAX_NUM_PLAYERS) || (players.size() < Constants.MIN_NUM_PLAYERS)) {
      throw new IllegalArgumentException("Invalid number of players.");
    }
  }

  // creates the referee's knowledge regarding all players given the players
  private Deque<PlayerInfo> createPlayerInfos(List<PlayerAPI> players) {
    Deque<PlayerInfo> result = new ArrayDeque<>();
    for (PlayerAPI player : players) {
      String name = player.name();
      PlayerInfo playerInfo = new PlayerInfo(name, player);
      result.add(playerInfo);
    }
    return result;
  }

  public List<List<String>> winnersAndKickedPlayersNames(Pair<List<PlayerInfo>, List<PlayerInfo>> winnersAndKickedPlayers) {
    List<PlayerInfo> winners = winnersAndKickedPlayers.first;
    List<PlayerInfo> kicked = winnersAndKickedPlayers.second;
    List<String> winnerNames = winners.stream().map(PlayerInfo::getName).sorted().toList();
    List<String> kickedNames = kicked.stream().map(PlayerInfo::getName).sorted().toList();
    return new ArrayList<>(List.of(winnerNames, kickedNames));
  }

  // runs the game to completion
  public Pair<List<PlayerInfo>, List<PlayerInfo>> runGame() {
    this.playerSetup();
    this.giveObserversEquations();
    while (!this.isGameOver()) {
      if (!this.firstStepOfMove()) {
        continue;
      }

      if (!this.secondStepOfMove()) {
        continue;
      }
      // Rotate active player
      this.gameState = this.gameState.rotateActivePlayer();
    }
    // Notify the players whether they have won or lost
    List<PlayerInfo> winners = this.notifyWinnersAndLosers();
    return new Pair<>(winners, this.kickedPlayers);
  }

  // sets up the game for the players by handing them the equations
  private void playerSetup() {
    Deque<PlayerInfo> setupPlayers = new ArrayDeque<>();
    List<PlayerInfo> errorPlayers = new ArrayList<>();
    for (PlayerInfo playerInfo : this.gameState.getPlayersAsList()) {
      if (PlayerProxy.setup(playerInfo.getPlayer(), this.equationTable).isPresent()) {
        setupPlayers.add(playerInfo);
      } else {
        errorPlayers.add(playerInfo);
      }
    }
    this.gameState = new Game_State(this.gameState.getBank(), this.gameState.getCards(), setupPlayers);
    this.kickedPlayers.addAll(errorPlayers);
  }

  // returns true if referee should move onto the second step of a move and false otherwise (if player gets kicked or
  // game is over)
  private boolean firstStepOfMove() {
    Turn_State turnState = this.gameState.getTurnState();
    Optional<DrawPebbleOrExchanges> firstAction = this.requestActivePlayerPebbleOrExchanges(turnState);
    // If Player did illegal action
    if (firstAction.isEmpty()) {
      this.kickActivePlayer();
      this.updateObservers();
      return false;
    }
    this.gameState = gameState.performDrawPebbleOrExchanges(firstAction.get(), this.equationTable);
    this.updateObservers();
    return !this.isGameOver();
  }

  // returns true if referee should finish this turn by rotating active player and false otherwise (if player gets kicked or
  // game is over)
  private boolean secondStepOfMove() {
    Turn_State turnState = this.gameState.getTurnState();
    Optional<CardPurchases> secondAction = this.requestActivePlayerCardPurchases(turnState);
    // If Player did illegal action
    if (secondAction.isEmpty()) {
      this.kickActivePlayer();
      this.updateObservers();
      return false;
    }
    this.gameState = gameState.performCardPurchases(secondAction.get());
    this.updateObservers();
    return !this.isGameOver();
  }

  // requests the active player's draw pebble or exchanges action
  private Optional<DrawPebbleOrExchanges> requestActivePlayerPebbleOrExchanges(Turn_State turnState) {
    PlayerAPI activePlayer = turnState.activePlayer().getPlayer();
    Optional<DrawPebbleOrExchanges> action = PlayerProxy.requestPebbleOrExchanges(activePlayer, turnState);
    if (action.isEmpty() ||
            !Rule_Book.canActivePlayerDoPebbleDrawingOrExchanges(turnState, action.get(), this.equationTable)) {
      return Optional.empty();
    }
    // if this is a deterministic game and the player requested a pebble, make draw pebble deterministic
    if (this.deterministicGame && action.get() instanceof DrawPebble) {
      action = Optional.of(new DrawDeterministicPebble());
    }
    return action;
  }

  // requests the active player's card purchases
  private Optional<CardPurchases> requestActivePlayerCardPurchases(Turn_State turnState) {
    PlayerAPI activePlayer = turnState.activePlayer().getPlayer();
    Optional<CardPurchases> action = PlayerProxy.requestCards(activePlayer, turnState);
    if (action.isEmpty() ||
            !Rule_Book.canActivePlayerDoCardPurchases(turnState, action.get())) {
      return Optional.empty();
    }
    return action;
  }

  // kicks the current player and makes the next player the active one
  private void kickActivePlayer() {
    PlayerInfo activePlayer = this.gameState.getActivePlayerCopy();
    this.kickedPlayers.add(activePlayer);
    this.gameState = this.gameState.removeActivePlayer();
  }

  // is the game over in the current state?
  private boolean isGameOver() {
    return this.gameState.isGameOver();
  }

  // notifies the players still in the game if they won or lost
  // returns the list of winning players
  private List<PlayerInfo> notifyWinnersAndLosers() {
    this.gameState = this.gameState.calculateFinalScores(this.gameMode);
    List<PlayerInfo> winners = Rule_Book.currentWinningPlayers(this.gameState);
    Deque<PlayerInfo> remainingPlayers = new ArrayDeque<>();
    List<PlayerInfo> errorPlayers = new ArrayList<>();
    for (PlayerInfo playerInfo : this.gameState.getPlayersAsList()) {
      if (PlayerProxy.win(playerInfo.getPlayer(), winners.contains(playerInfo)).isPresent()) {
        remainingPlayers.add(playerInfo);
      } else {
        errorPlayers.add(playerInfo);
        winners.remove(playerInfo);
      }
    }
    this.gameState = new Game_State(this.gameState.getBank(), this.gameState.getCards(), remainingPlayers);
    this.kickedPlayers.addAll(errorPlayers);
    this.notifyObserversEndGame();
    return winners;
  }

  // adds an observer to this Referee's list of observers
  //if when the game is run there is a --show flag, we would attach observers to the referee
  public void addObserver(Observer observer) {
    this.observers.add(observer);
  }

  // updates all of the subscribed observers about the game state
  private void updateObservers() {
    for (Observer obs : this.observers) {
      ObserverProxy.update(obs, this.gameState);
    }
  }

  // notifies observers when the game ends
  private void notifyObserversEndGame() {
    for (Observer obs : this.observers) {
      ObserverProxy.gameOver(obs);
    }
  }

  // sends the equations to the observers
  private void giveObserversEquations() {
    for(Observer obs : observers) {
      ObserverProxy.getEquations(obs, this.equationTable);
    }
  }
}
