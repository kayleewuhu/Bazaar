package Tests.Data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Common.Constants;
import Common.Data.Card;
import Common.Data.CardCollection;
import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardCollectionTest {
  CardCollection cards;
  List<Card> visibles;
  List<Card> invisibles;

  void initialize() {
    Card oneOfEach = new Card(new PebbleCollection(1), false);
    this.visibles = new ArrayList<>(List.of(oneOfEach));
    PebbleCollection red = new PebbleCollection(0);
    red.putPebbleQuantity(PebbleColor.RED, 5);
    Card allRed = new Card(red, true);
    this.invisibles = new ArrayList<>(List.of(allRed));
    this.cards = new CardCollection(this.visibles, this.invisibles);
  }

  @Test
  void testConstructors() {
    CardCollection cards = new CardCollection(new Random());
    assertEquals(Constants.NUM_VISIBLE_CARDS, cards.getVisibleCardsCopy().size());
    assertEquals(Constants.NUM_CARDS-Constants.NUM_VISIBLE_CARDS, cards.getInvisibleCardsCopy().size());
  }

  @Test
  void testGetVisibleCardsCopy() {
    this.initialize();
    assertEquals(this.visibles, this.cards.getVisibleCardsCopy());
  }

  @Test
  void testGetInvisibleCardsCopy() {
    this.initialize();
    assertEquals(this.invisibles, this.cards.getInvisibleCardsCopy());
  }

  @Test
  void testGetCardCollectionCopy() {
    this.initialize();
    CardCollection copy = this.cards.getCardCollectionCopy();
    assertEquals(this.cards.getVisibleCardsCopy(), copy.getVisibleCardsCopy());
    assertEquals(this.cards.getInvisibleCardsCopy(), copy.getInvisibleCardsCopy());
  }

  @Test
  void testRemoveVisibleCards() {
    this.initialize();
    CardCollection newCC = this.cards.removeVisibleCards(this.visibles);
    assertEquals(new ArrayList<>(), newCC.getVisibleCardsCopy());
  }

  @Test
  void testReplaceAcquiredVisibleCards() {
    this.initialize();
    CardCollection newCC = this.cards.replaceAcquiredVisibleCards();
    List<Card> newList = new ArrayList<>();
    newList.addAll(this.visibles);
    newList.addAll(this.invisibles);
    assertEquals(newList, newCC.getVisibleCardsCopy());
    assertEquals(new ArrayList<>(), newCC.getInvisibleCardsCopy());
  }

  @Test
  void testRemoveBottomInvisibleCardOrAllVisible() {
    this.initialize();
    assertEquals(1, this.invisibles.size());
    assertEquals(1, this.visibles.size());
    CardCollection cards = this.cards.removeBottomInvisibleCardOrAllVisible();
    assertEquals(0, cards.getInvisibleCardsCopy().size());
    assertEquals(1, cards.getVisibleCardsCopy().size());
    cards = cards.removeBottomInvisibleCardOrAllVisible();
    assertEquals(0, cards.getInvisibleCardsCopy().size());
    assertEquals(0, cards.getVisibleCardsCopy().size());
  }

  @Test
  void testNoMoreCards() {
    this.initialize();
    assertFalse(this.cards.noMoreCards());
    CardCollection newCC = this.cards.removeBottomInvisibleCardOrAllVisible();
    newCC = newCC.removeBottomInvisibleCardOrAllVisible();
    assertTrue(newCC.noMoreCards());
  }

  @Test
  void testEquals() {
    this.initialize();
    CardCollection copy = this.cards.getCardCollectionCopy();
    assertEquals(this.cards, copy);
  }
}
