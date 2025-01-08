package Tests.Player;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Common.Data.BidirectionalEquation;
import Common.Data.Card;
import Common.Data.CardPurchases;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;
import Common.Data.UnidirectionalEquation;
import Common.Pair;
import Common.Turn_State;
import Player.MaximizePoints;
import Referee.PlayerInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaximizePointsTest {

  EquationTable equationTable;
  MaximizePoints strat = new MaximizePoints();

  private void initialize() {
    PebbleCollection OneRed = new PebbleCollection(0);
    OneRed = OneRed.updatePebbleQuantity(PebbleColor.RED, 1);
    PebbleCollection OneYellow = new PebbleCollection(0);
    OneYellow = OneYellow.updatePebbleQuantity(PebbleColor.YELLOW, 1);
    BidirectionalEquation equation = new BidirectionalEquation(OneRed, OneYellow);
    this.equationTable = new EquationTable(new ArrayList<>(List.of(equation)));
  }

  @Test
  void testShouldDrawPebble() {
    this.initialize();
    Turn_State turnState = new Turn_State(new PebbleCollection(0), new PlayerInfo(), new ArrayList<>(), new ArrayList<>());
    assertTrue(this.strat.shouldDrawPebble(turnState, this.equationTable));
    turnState = new Turn_State(new PebbleCollection(1), new PlayerInfo(new PebbleCollection(1)), new ArrayList<>(), new ArrayList<>());
    assertFalse(this.strat.shouldDrawPebble(turnState, this.equationTable));
  }

  @Test
  void testNoCardsBought() {
    Turn_State turnState = new Turn_State(new PebbleCollection(0), new PlayerInfo(), new ArrayList<>(), new ArrayList<>());
    EquationTable equationTable = new EquationTable(new ArrayList<>());
    CardPurchases cards = this.strat.whatCardPurchases(turnState, equationTable);
    assertEquals(new ArrayList<>(), cards.getSequenceOfCardPurchasesCopy());
  }

  @Test
  void testCardIsBoughtAfterNoExchanges() {
    Card card = new Card(new Random(1));
    Turn_State turnState = new Turn_State(new PebbleCollection(0), new PlayerInfo(new PebbleCollection(5)), new ArrayList<>(), new ArrayList<>(List.of(card)));
    EquationTable equationTable = new EquationTable(new ArrayList<>());
    Pair<Exchanges, CardPurchases> exchangesAndCards = this.strat.whatExchangesAndCardPurchases(turnState, equationTable);
    Exchanges exchanges = exchangesAndCards.first;
    CardPurchases cards = exchangesAndCards.second;
    assertEquals(new ArrayList<>(), exchanges.getSequenceOfExchangesCopy());
    assertEquals(new ArrayList<>(List.of(card)), cards.getSequenceOfCardPurchasesCopy());
  }

  @Test
  void testCardIsBoughtAfterExchange() {
    this.initialize();
    PebbleCollection cardPebbles = new PebbleCollection(0);
    cardPebbles = cardPebbles.putPebbleQuantity(PebbleColor.RED, 5);
    Card card = new Card(cardPebbles, true);
    PebbleCollection wallet = new PebbleCollection(0);
    wallet = wallet.putPebbleQuantity(PebbleColor.RED, 4);
    wallet = wallet.putPebbleQuantity(PebbleColor.YELLOW, 1);
    Turn_State turnState = new Turn_State(new PebbleCollection(1), new PlayerInfo(wallet), new ArrayList<>(), new ArrayList<>(List.of(card)));
    Pair<Exchanges, CardPurchases> exchangesAndCards = this.strat.whatExchangesAndCardPurchases(turnState, this.equationTable);
    Exchanges exchanges = exchangesAndCards.first;
    CardPurchases cards = exchangesAndCards.second;
    PebbleCollection OneRed = new PebbleCollection(0);
    OneRed = OneRed.updatePebbleQuantity(PebbleColor.RED, 1);
    PebbleCollection OneYellow = new PebbleCollection(0);
    OneYellow = OneYellow.updatePebbleQuantity(PebbleColor.YELLOW, 1);
    UnidirectionalEquation exchange = new UnidirectionalEquation(OneYellow, OneRed);
    assertEquals(new ArrayList<>(List.of(exchange)), exchanges.getSequenceOfExchangesCopy());
    assertEquals(new ArrayList<>(List.of(card)), cards.getSequenceOfCardPurchasesCopy());
  }

  @Test
  void testTwoCardsAreBoughtAfterExchange() {
    this.initialize();
    PebbleCollection cardPebbles = new PebbleCollection(0);
    cardPebbles = cardPebbles.putPebbleQuantity(PebbleColor.RED, 5);
    Card card = new Card(cardPebbles, true);
    PebbleCollection wallet = new PebbleCollection(0);
    wallet = wallet.putPebbleQuantity(PebbleColor.RED, 9);
    wallet = wallet.putPebbleQuantity(PebbleColor.YELLOW, 1);
    Turn_State turnState = new Turn_State(new PebbleCollection(2), new PlayerInfo(wallet), new ArrayList<>(), new ArrayList<>(List.of(card, card)));
    Pair<Exchanges, CardPurchases> exchangesAndCards = this.strat.whatExchangesAndCardPurchases(turnState, this.equationTable);
    Exchanges exchanges = exchangesAndCards.first;
    CardPurchases cards = exchangesAndCards.second;
    PebbleCollection OneRed = new PebbleCollection(0);
    OneRed = OneRed.updatePebbleQuantity(PebbleColor.RED, 1);
    PebbleCollection OneYellow = new PebbleCollection(0);
    OneYellow = OneYellow.updatePebbleQuantity(PebbleColor.YELLOW, 1);
    UnidirectionalEquation exchange = new UnidirectionalEquation(OneYellow, OneRed);
    assertEquals(new ArrayList<>(List.of(exchange)), exchanges.getSequenceOfExchangesCopy());
    assertEquals(new ArrayList<>(List.of(card, card)), cards.getSequenceOfCardPurchasesCopy());
  }
}
