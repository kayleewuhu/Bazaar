//package Tests.Referee;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayDeque;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import Common.Data.BidirectionalEquation;
//import Common.Data.CardCollection;
//import Common.Data.EquationTable;
//import Common.Data.PebbleCollection;
//import Common.Data.PebbleColor;
//import Common.Pair;
//import Player.DoNothing;
//import Player.ExceptionMechanism;
//import Player.MaximizeCards;
//import Player.MaximizePoints;
//import Player.Mechanism;
//import Player.PlayerAPI;
//import Referee.Game_State;
//import Referee.PlayerInfo;
//import Referee.Referee;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class RefereeTest {
//
//  // The referee is given a gamestate where there are no more cards and there is only one player so the expected output
//  // if that the one player is the winner and there are no kicked players
//  @Test
//  public void testRun1Player() {
//    PlayerAPI player = new Mechanism("Points", new MaximizePoints());
//    PebbleCollection bank = new PebbleCollection(4);
//    CardCollection noCards = new CardCollection(new ArrayList<>(), new ArrayList<>());
//    PlayerInfo playerInfo = new PlayerInfo(player.name(), player);
//    Game_State gameState = new Game_State(bank, noCards, new ArrayDeque<>(List.of(playerInfo)));
//    EquationTable equationTable = new EquationTable(new ArrayList<>());
//    Referee referee = new Referee(new ArrayList<>(List.of(player)), gameState, equationTable);
//    Pair<List<PlayerInfo>, List<PlayerInfo>> output = referee.runGame();
//    List<PlayerInfo> winningPlayers = output.first;
//    List<PlayerInfo> kickedPlayers = output.second;
//    assertEquals(new ArrayList<>(List.of(playerInfo)), winningPlayers);
//    assertEquals(new ArrayList<>(), kickedPlayers);
//  }
//
//  // The referee is given a gamestate where there are 1 equations in the equation table, 20 cards, 100 pebble bank, and one
//  // player with a lot of pebbles in wallet and at a score of 15 and another player with no pebbles and at a score of 0.
//  // The expected output is that the player with the initial score of 15 wins.
//  @Test
//  public void testRun2Players() {
//    PlayerAPI playerPoints = new Mechanism("Points", new MaximizePoints());
//    PlayerAPI playerCards = new Mechanism("Cards", new MaximizeCards());
//    PebbleCollection bank = new PebbleCollection(20);
//    CardCollection cards = new CardCollection(new Random(0));
//    PlayerInfo playerInfoPoints = new PlayerInfo(new PebbleCollection(0), 0, playerPoints.name(), playerPoints);
//    PlayerInfo playerInfoCards = new PlayerInfo(new PebbleCollection(10), 15, playerCards.name(), playerCards);
//    Game_State gameState = new Game_State(bank, cards, new ArrayDeque<>(List.of(playerInfoPoints, playerInfoCards)));
//    PebbleCollection OneRed = new PebbleCollection(0);
//    OneRed = OneRed.updatePebbleQuantity(PebbleColor.RED, 1);
//    PebbleCollection OneEverythingButZeroRed = new PebbleCollection(1);
//    OneEverythingButZeroRed = OneEverythingButZeroRed.putPebbleQuantity(PebbleColor.RED, 0);
//    BidirectionalEquation equation = new BidirectionalEquation(OneEverythingButZeroRed, OneRed);
//    EquationTable equationTable = new EquationTable(new ArrayList<>(List.of(equation)));
//    Referee referee = new Referee(new ArrayList<>(List.of(playerPoints, playerCards)), gameState, equationTable);
//    Pair<List<PlayerInfo>, List<PlayerInfo>> output = referee.runGame();
//    List<PlayerInfo> winningPlayers = output.first;
//    List<PlayerInfo> kickedPlayers = output.second;
//    assertEquals(new ArrayList<>(List.of(playerInfoCards)), winningPlayers);
//    assertEquals(new ArrayList<>(), kickedPlayers);
//  }
//
//  // The referee starts a brand-new game that has one player using maximize points strategy and another player that does nothing.
//  // The expected output is that the player with maximize points strategy wins.
//  @Test
//  public void testRun2PlayersFullGame() {
//    PlayerAPI playerPoints = new Mechanism("Points", new MaximizePoints());
//    PlayerAPI playerDoNothing = new Mechanism("I don't know how to play", new DoNothing());
//    PlayerInfo playerInfoPoints = new PlayerInfo(playerPoints.name(), playerPoints);
//    Referee referee = new Referee(new ArrayList<>(List.of(playerPoints, playerDoNothing)));
//    Pair<List<PlayerInfo>, List<PlayerInfo>> output = referee.runGame();
//    List<PlayerInfo> winningPlayers = output.first;
//    List<PlayerInfo> kickedPlayers = output.second;
//    assertEquals(new ArrayList<>(List.of(playerInfoPoints)), winningPlayers);
//    assertEquals(new ArrayList<>(), kickedPlayers);
//  }
//
//  // The Referee begins a brand-new game with 2 players: one who will throw an exception during game
//  // play and one who will win using a maximize points strategy.
//  // The former player is a dropout, and the latter player will be a winner.
//  @Test
//  public void testRun2PlayersCheater() {
//    PlayerAPI playerPoints = new Mechanism("Points", new MaximizePoints());
//    PlayerAPI cheater = new ExceptionMechanism("Cheater", new MaximizeCards(), "setup");
//    PlayerInfo playerInfoPoints = new PlayerInfo(playerPoints.name(), playerPoints);
//    PlayerInfo playerInfoCheater = new PlayerInfo(cheater.name(), cheater);
//    Referee referee = new Referee(new ArrayList<>(List.of(playerPoints, cheater)));
//    Pair<List<PlayerInfo>, List<PlayerInfo>> output = referee.runGame();
//    List<PlayerInfo> winningPlayers = output.first;
//    List<PlayerInfo> kickedPlayers = output.second;
//    assertEquals(new ArrayList<>(List.of(playerInfoPoints)), winningPlayers);
//    assertEquals(new ArrayList<>(List.of(playerInfoCheater)), kickedPlayers);
//  }
//
//  // The Referee begins a brand-new game with 2 players: one who will win using a maximize points
//  // strategy but throws an exception when being informed of their winning status.
//  // The other player gets no points.
//  // Expected output:
//  // No winners. One dropout player.
//  @Test
//  public void testRun2PlayersWinnerCheater() {
//    PlayerAPI sadPlayer = new Mechanism("I sad", new DoNothing());
//    PlayerAPI cheater = new ExceptionMechanism("WinnerCheater", new MaximizePoints(), "win");
//    PlayerInfo playerInfoCheater = new PlayerInfo(cheater.name(), cheater);
//    Referee referee = new Referee(new ArrayList<>(List.of(sadPlayer, cheater)));
//    Pair<List<PlayerInfo>, List<PlayerInfo>> output = referee.runGame();
//    List<PlayerInfo> winningPlayers = output.first;
//    List<PlayerInfo> kickedPlayers = output.second;
//    assertEquals(new ArrayList<>(), winningPlayers);
//    assertEquals(new ArrayList<>(List.of(playerInfoCheater)), kickedPlayers);
//  }
//}
