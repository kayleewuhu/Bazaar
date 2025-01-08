package Tests.Data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Common.Constants;
import Common.Data.Card;
import Common.Data.CardPurchases;
import Common.Data.PebbleCollection;
import Common.Pair;
import Common.Turn_State;
import Referee.PlayerInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardPurchasesTest {

  @Test
  public void testCardPurchasesConstructor() {
    List<Card> cards = new ArrayList<>(Arrays.asList(new Card(new Random())));
    CardPurchases cardPurchases = new CardPurchases(cards);
    assertEquals(cards, cardPurchases.getSequenceOfCardPurchasesCopy());
  }

  @Test
  public void testGetSequenceOfCardPurchasesCopy() {
    List<Card> cards = new ArrayList<>(Arrays.asList(new Card(new Random())));
    CardPurchases cardPurchases = new CardPurchases(cards);
    assertEquals(cards, cardPurchases.getSequenceOfCardPurchasesCopy());
  }

  @Test
  public void testGetCardPurchasesCopy() {
    List<Card> cards = new ArrayList<>(Arrays.asList(new Card(new Random())));
    CardPurchases cardPurchases = new CardPurchases(cards);
    CardPurchases copy = cardPurchases.getCardPurchasesCopy();
    assertEquals(cardPurchases, copy);
  }

  @Test
  public void testTotalPointsWorth() {
    List<Card> cards = new ArrayList<>(Arrays.asList(new Card(new PebbleCollection(1), true),
            new Card(new PebbleCollection(1), false)));
    CardPurchases cardPurchases = new CardPurchases(cards);
    assertEquals(Constants.POINTS_FROM_SMILEY_CARD_AND_3_OR_MORE_PEBBLES_LEFT
            + Constants.POINTS_FROM_PLAIN_CARD_AND_0_LEFT, cardPurchases.totalPointsWorth(new PebbleCollection(2)));
  }

  @Test
  public void testTotalCards() {
    List<Card> cards = new ArrayList<>(Arrays.asList(new Card(new PebbleCollection(1), true),
            new Card(new PebbleCollection(1), false)));
    CardPurchases cardPurchases = new CardPurchases(cards);
    assertEquals(cardPurchases.totalCards(), 2);
  }

  @Test
  void testAttemptAction() {
    List<Card> cards = new ArrayList<>(Arrays.asList(new Card(new PebbleCollection(1), true),
            new Card(new PebbleCollection(1), false)));
    CardPurchases cardPurchases = new CardPurchases(cards);
    Turn_State turnState = new Turn_State(new PebbleCollection(0), new PlayerInfo(new PebbleCollection(2)), new ArrayList<>(), cards);
    Pair<PebbleCollection, PebbleCollection> newWalletAndNewBank = cardPurchases.attemptAction(turnState).get();
    PebbleCollection newWallet = newWalletAndNewBank.first;
    PebbleCollection newBank = newWalletAndNewBank.second;
    assertEquals(new PebbleCollection(0), newWallet);
    assertEquals(new PebbleCollection(2), newBank);
  }

  @Test
  void testEquals() {
    List<Card> cards = new ArrayList<>(Arrays.asList(new Card(new PebbleCollection(1), true),
            new Card(new PebbleCollection(1), false)));
    CardPurchases cardPurchases = new CardPurchases(cards);
    CardPurchases copy = cardPurchases.getCardPurchasesCopy();
    assertTrue(cardPurchases.equals(copy));
  }
}
