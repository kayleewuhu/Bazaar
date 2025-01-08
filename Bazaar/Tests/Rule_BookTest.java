package Tests;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import Common.Constants;
import Common.Data.BidirectionalEquation;
import Common.Data.Card;
import Common.Data.CardCollection;
import Common.Data.CardPurchases;
import Common.Data.DrawPebble;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Data.PebbleColor;
import Common.Data.PebbleCollection;
import Common.Data.UnidirectionalEquation;
import Common.Rule_Book;
import Common.Turn_State;
import Player.MaximizePoints;
import Player.Mechanism;
import Referee.Game_State;
import Referee.PlayerInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// This class tests the functions in the Rule_Book.
public class Rule_BookTest {

  @Test
  public void testCanActivePlayerDoPebbleDrawing() {
    PebbleCollection bank = new PebbleCollection(0);
    Turn_State turnStateWithEmptyBank = new Turn_State(bank, new PlayerInfo(), new ArrayList<>(), new ArrayList<>());
    DrawPebble drawPebble = new DrawPebble();
    EquationTable equationTable = new EquationTable(new ArrayList<>());
    // Bank is empty
    assertFalse(Rule_Book.canActivePlayerDoPebbleDrawingOrExchanges(turnStateWithEmptyBank, drawPebble, equationTable));
    bank = bank.putPebbleQuantity(PebbleColor.RED, 1);
    Turn_State turnStateNotEmptyBank = new Turn_State(bank, new PlayerInfo(), new ArrayList<>(), new ArrayList<>());
    // Bank has a pebble
    assertTrue(Rule_Book.canActivePlayerDoPebbleDrawingOrExchanges(turnStateNotEmptyBank, drawPebble, equationTable));
  }

  @Test
  public void testCanActivePlayerDoExchanges() {
    PebbleCollection bank = new PebbleCollection(1);
    Turn_State turnState = new Turn_State(bank, new PlayerInfo(), new ArrayList<>(), new ArrayList<>());
    PebbleCollection oneRed = new PebbleCollection(0);
    oneRed = oneRed.putPebbleQuantity(PebbleColor.RED, 1);
    PebbleCollection oneBlue = new PebbleCollection(0);
    oneBlue = oneBlue.putPebbleQuantity(PebbleColor.BLUE, 1);
    BidirectionalEquation equationOneRedOneBlue = new BidirectionalEquation(oneRed, oneBlue);
    EquationTable equationTable = new EquationTable(new ArrayList<>(List.of(equationOneRedOneBlue)));
    UnidirectionalEquation exchangeOneRedOneBlue = new UnidirectionalEquation(oneRed, oneBlue);
    Exchanges exchanges = new Exchanges(new ArrayList<>(List.of(exchangeOneRedOneBlue)));
    // active player doesn't have enough pebbles
    assertFalse(Rule_Book.canActivePlayerDoPebbleDrawingOrExchanges(turnState, exchanges, equationTable));
    UnidirectionalEquation exchangeOneRedOneRed = new UnidirectionalEquation(oneRed, oneRed);
    turnState = new Turn_State(bank, new PlayerInfo(oneRed), new ArrayList<>(), new ArrayList<>());
    // active player can make exchanges
    assertTrue(Rule_Book.canActivePlayerDoPebbleDrawingOrExchanges(turnState, exchanges, equationTable));
    exchanges = new Exchanges(new ArrayList<>(List.of(exchangeOneRedOneRed)));
    // equation in exchanges doesn't exist on Equation Table
    assertFalse(Rule_Book.canActivePlayerDoPebbleDrawingOrExchanges(turnState, exchanges, equationTable));
  }

  @Test
  public void testCanActivePlayerDoCardPurchases() {
    PebbleCollection fiveRed = new PebbleCollection(0);
    fiveRed = fiveRed.putPebbleQuantity(PebbleColor.RED, 5);
    Card fiveRedCard = new Card(fiveRed, true);
    CardPurchases cardPurchases = new CardPurchases(List.of(fiveRedCard));
    Turn_State turnState = new Turn_State(new PebbleCollection(0), new PlayerInfo(fiveRed),
            new ArrayList<>(), new ArrayList<>());
    // card in card purchases doesn't exist in visible cards
    assertFalse(Rule_Book.canActivePlayerDoCardPurchases(turnState, cardPurchases));
    turnState = new Turn_State(new PebbleCollection(0), new PlayerInfo(fiveRed), new ArrayList<>(),
            new ArrayList<>(List.of(fiveRedCard)));
    // active player can do card purchases
    assertTrue(Rule_Book.canActivePlayerDoCardPurchases(turnState, cardPurchases));
    PebbleCollection fourRed = new PebbleCollection(0);
    fourRed = fourRed.putPebbleQuantity(PebbleColor.RED, 4);
    turnState = new Turn_State(new PebbleCollection(0), new PlayerInfo(fourRed), new ArrayList<>(),
            new ArrayList<>(List.of(fiveRedCard)));
    // active player doesn't have enough pebbles to buy card
    assertFalse(Rule_Book.canActivePlayerDoCardPurchases(turnState, cardPurchases));
  }

  @Test
  public void testCanDoExchange() {
    PebbleCollection oneRed = new PebbleCollection(0);
    oneRed = oneRed.putPebbleQuantity(PebbleColor.RED, 1);
    PebbleCollection oneBlue = new PebbleCollection(0);
    oneBlue = oneBlue.putPebbleQuantity(PebbleColor.BLUE, 1);
    BidirectionalEquation equationOneRedOneBlue = new BidirectionalEquation(oneRed, oneBlue);
    EquationTable equationTable = new EquationTable(new ArrayList<>(List.of(equationOneRedOneBlue)));
    UnidirectionalEquation exchangeOneRedOneBlue = new UnidirectionalEquation(oneRed, oneBlue);
    // wallet doesn't have enough pebbles
    assertFalse(Rule_Book.canDoExchange(exchangeOneRedOneBlue, new PebbleCollection(0),
            new PebbleCollection(1), equationTable));
    // bank doesn't have enough pebbles
    assertFalse(Rule_Book.canDoExchange(exchangeOneRedOneBlue, new PebbleCollection(1),
            new PebbleCollection(0), equationTable));
    // exchange is possible
    assertTrue(Rule_Book.canDoExchange(exchangeOneRedOneBlue, new PebbleCollection(1),
            new PebbleCollection(1), equationTable));
    UnidirectionalEquation exchangeOneRedOneRed = new UnidirectionalEquation(oneRed, oneRed);
    // exchange doesn't exist on Equation Table
    assertFalse(Rule_Book.canDoExchange(exchangeOneRedOneRed, new PebbleCollection(1),
            new PebbleCollection(0), equationTable));
  }

  @Test
  public void testCanBuyCard() {
    PebbleCollection fiveRed = new PebbleCollection(0);
    fiveRed = fiveRed.putPebbleQuantity(PebbleColor.RED, 5);
    Card fiveRedCard = new Card(fiveRed, true);
    // wallet doesn't have enough pebbles
    assertFalse(Rule_Book.canBuyCard(new ArrayList<>(List.of(fiveRedCard)),
            new PebbleCollection(0), fiveRedCard));
    // card purchase is possible
    assertTrue(Rule_Book.canBuyCard(new ArrayList<>(List.of(fiveRedCard)),
            new PebbleCollection(5), fiveRedCard));
    // card doesn't exist in visible cards
    assertFalse(Rule_Book.canBuyCard(new ArrayList<>(), new PebbleCollection(5), fiveRedCard));
  }

  @Test
  public void testTotalPointsFromCardPurchases() {
    assertEquals(0, Rule_Book.totalPointsFromCardPurchases(new CardPurchases(
            new ArrayList<>()), new PebbleCollection(0)));
    Card smile = new Card(new PebbleCollection(1), true);
    assertEquals(Constants.POINTS_FROM_SMILEY_CARD_AND_0_LEFT, Rule_Book.totalPointsFromCardPurchases(new CardPurchases(
            new ArrayList<>(List.of(smile))), new PebbleCollection(1)));
    Card noSmile = new Card(new PebbleCollection(1), false);
    assertEquals(Constants.POINTS_FROM_PLAIN_CARD_AND_0_LEFT, Rule_Book.totalPointsFromCardPurchases(new CardPurchases(
            new ArrayList<>(List.of(noSmile))), new PebbleCollection(1)));
    assertEquals(Constants.POINTS_FROM_SMILEY_CARD_AND_3_OR_MORE_PEBBLES_LEFT
            + Constants.POINTS_FROM_PLAIN_CARD_AND_0_LEFT, Rule_Book.totalPointsFromCardPurchases(new CardPurchases(
            new ArrayList<>(Arrays.asList(smile, noSmile))), new PebbleCollection(2)));
    assertEquals(Constants.POINTS_FROM_PLAIN_CARD_AND_3_OR_MORE_PEBBLES_LEFT
            + Constants.POINTS_FROM_SMILEY_CARD_AND_0_LEFT, Rule_Book.totalPointsFromCardPurchases(new CardPurchases(
            new ArrayList<>(Arrays.asList(noSmile, smile))), new PebbleCollection(2)));
  }

  @Test
  public void testPointsYieldedIfCardBought() {
    Card smile = new Card(new PebbleCollection(1), true);
    Card noSmile = new Card(new PebbleCollection(1), false);
    assertEquals(Constants.POINTS_FROM_SMILEY_CARD_AND_0_LEFT, Rule_Book.pointsYieldedIfCardBought(smile, new PebbleCollection(1)));
    assertEquals(Constants.POINTS_FROM_SMILEY_CARD_AND_3_OR_MORE_PEBBLES_LEFT, Rule_Book.pointsYieldedIfCardBought(smile, new PebbleCollection(2)));
    assertEquals(Constants.POINTS_FROM_PLAIN_CARD_AND_0_LEFT, Rule_Book.pointsYieldedIfCardBought(noSmile, new PebbleCollection(1)));
    assertEquals(Constants.POINTS_FROM_PLAIN_CARD_AND_3_OR_MORE_PEBBLES_LEFT, Rule_Book.pointsYieldedIfCardBought(noSmile, new PebbleCollection(2)));
  }

  @Test
  public void testIsTheGameOver() {
    Game_State noPlayers = new Game_State(new PebbleCollection(), new CardCollection(new ArrayList<>(),
            new ArrayList<>()), new ArrayDeque<>());
    // game has 0 or 1 players left
    assertTrue(Rule_Book.isTheGameOver(noPlayers));
    PlayerInfo winner = new PlayerInfo(new PebbleCollection(), 20, "Winner", null, new ArrayList<>());
    Game_State winningPlayer = new Game_State(new PebbleCollection(), new CardCollection(new ArrayList<>(),
            new ArrayList<>()), new ArrayDeque<>(List.of(winner)));
    // the active player has won the game
    assertTrue(Rule_Book.isTheGameOver(winningPlayer));
    Game_State noVisibleCards = new Game_State(new PebbleCollection(), new CardCollection(new ArrayList<>(),
            new ArrayList<>()), new ArrayDeque<>());
    // no more visible cards
    assertTrue(Rule_Book.isTheGameOver(noVisibleCards));
    PlayerInfo player = new PlayerInfo(new PebbleCollection(1));
    Card visibleCard = new Card(new PebbleCollection(2), true);
    // bank is out of pebbles and no more cards can be bought
    Game_State outOfPebbles = new Game_State(new PebbleCollection(), new CardCollection(
            new ArrayList<>(List.of(visibleCard)), new ArrayList<>()), new ArrayDeque<>(List.of(player)));
    assertTrue(Rule_Book.isTheGameOver(outOfPebbles));
    Game_State inProgress = new Game_State(new PebbleCollection(1), new CardCollection(
            new ArrayList<>(List.of(visibleCard)), new ArrayList<>()), new ArrayDeque<>(Arrays.asList(player, player)));
    // game is not over
    assertFalse(Rule_Book.isTheGameOver(inProgress));
  }
}
