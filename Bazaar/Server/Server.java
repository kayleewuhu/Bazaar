package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.concurrent.*;

import Common.Constants;
import Common.Data.EquationTable;
import Common.IGameMode;
import Common.Json.ObjectJsonDeserializer;
import Common.Json.ObjectJsonSerializer;
import Common.Pair;
import Referee.Game_State;
import Referee.Referee;
import Player.PlayerAPI;
import Referee.PlayerInfo;

// This class represents the Server that begins client registration for the game,
// allows client collection, registers clients, and initializes the Referee to start the game.
// to run game:
// - manageSignUp
// - initializeRandomGame or initializeDeterministicGame
// - returnGameResult
public class Server {
  // the socket the server reads from
  private ServerSocket serverSocket;
  // the list of players registered
  private List<PlayerAPI> players;
  // the Referee to host the game
  private Referee referee;

  public Server(int port) {
    try {
      this.serverSocket = new ServerSocket(port);
      this.players = new ArrayList<>();
    }
    catch (Exception e) {
      System.out.println("Socket unable to open: " + e.getMessage());
    }
  }

  // manages the entire sign up, will run multiple periods if necessary
  public void manageSignUp() {
    for (int num_cycles = 0; num_cycles < Constants.SIGN_UP_WAIT_PERIOD_TIMEOUT; num_cycles++) {
      if (this.players.size() < Constants.MIN_NUM_PLAYERS) {
        this.manageSingleSignUpPeriod();
      }
    }
  }

  // initializes the Referee with only players, creating a random game
  public void initializeRandomGame() {
    if (this.notEnoughPlayersSignedUp()) return;
    this.referee = new Referee(this.players);
  }

  // initializes the Referee with all game info, creating a game with a deterministic result
  public void initializeDeterministicGame(Game_State gameState, EquationTable equationTable, IGameMode gameMode) {
    if (this.notEnoughPlayersSignedUp()) return;
    this.referee = new Referee(this.players, gameState, equationTable, gameMode);
  }

  // return the game result
  public List<List<String>> returnGameResult() {
    if (this.notEnoughPlayersSignedUp()) {
      return new ArrayList<>(List.of(new ArrayList<>(), new ArrayList<>()));
    }

    List<List<String>> winnersAndKicked = this.runGame();
    return winnersAndKicked;
  }

  // if less than the minimum number of players required sign up, close the channel
  // EFFECT: empties the server's list of players
  private boolean notEnoughPlayersSignedUp() {
    if (this.players.size() < Constants.MIN_NUM_PLAYERS) {
      this.players = new ArrayList<>();
      // disconnects all the players by closing the server
      try {
        this.serverSocket.close();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      return true;
    }
    return false;
  }

  // manages a single sign up period
  private void manageSingleSignUpPeriod() {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<?> future = executor.submit(this::acceptPlayers);
    try {
      future.get(Constants.SIGN_UP_WAIT_SEC, TimeUnit.SECONDS);
    }
    catch (TimeoutException e) {
      future.cancel(true);
    }
    catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    finally {
      executor.shutdownNow();
    }
  }

  // accepts players and allows them to register
  private void acceptPlayers() {
    while (!Thread.currentThread().isInterrupted() && this.players.size() < Constants.MAX_NUM_PLAYERS) {
      this.registerPlayer();
    }
  }

  // registers a single player by accepting the connecting and getting the player's name
  // if either fails, the player will not be registered/added to the game
  // EFFECT: if registration is successful, the player is added to the server's list of players
  private void registerPlayer() {
    try {
      Socket playerSocket = this.serverSocket.accept();
      Optional<String> possibleName = this.getPlayerName(playerSocket.getInputStream());

      if (possibleName.isPresent()) {
        this.players.add(new Player(possibleName.get(), playerSocket.getInputStream(), playerSocket.getOutputStream()));
      }
    }
    // socket closed, because registration is up
    catch (SocketException ignored) {}
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  // gets the player's name from the given input stream
  // if the server times out waiting for the name, the player is not properly registered and will not be in the game
  private Optional<String> getPlayerName(InputStream in) {
    Thread.interrupted();
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<String> future = executor.submit(reader::readLine);
    Optional<String> receivedName = Optional.empty();

    try {
      String name = future.get(Constants.NAME_WAIT_SEC, TimeUnit.SECONDS);
      receivedName = Optional.of(ObjectJsonSerializer.jsonElementToString(ObjectJsonDeserializer.jsonStringToJsonElement(name)));
    }
    // timeout exception: the name isn't sent in the expected time
    // interrupted exception: the overall registration timed out in the middle of waiting for the name
    catch (TimeoutException | InterruptedException e) {
      future.cancel(true);
    }
    // unexpected exceptions
    catch (ExecutionException executionException) {
      executionException.printStackTrace();
    }
    finally {
      executor.shutdownNow();
    }
    return receivedName;
  }

  // runs the game and returns the winners and kicked players
  private List<List<String>> runGame() {
    Pair<List<PlayerInfo>, List<PlayerInfo>> answers = this.referee.runGame();
    return this.referee.winnersAndKickedPlayersNames(answers);
  }

  //closes the server socket and shuts the server down.
  public void shutdown() {
    try {
      this.serverSocket.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}