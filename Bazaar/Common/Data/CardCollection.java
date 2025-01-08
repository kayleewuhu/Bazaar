package Common.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Common.Constants;

// This class represents all the cards in the game.
public class CardCollection {
  // the cards visible in the game
  private final List<Card> visibleCards;
  // the cards that are face down
  private final List<Card> invisibleCards;

  public CardCollection(Random rand) {
    this.visibleCards = new ArrayList<>();
    for (int i = 0; i < Constants.NUM_VISIBLE_CARDS; i++) {
      this.visibleCards.add(new Card(rand));
    }

    this.invisibleCards = new ArrayList<>();
    for (int i = 0; i < Constants.NUM_CARDS - Constants.NUM_VISIBLE_CARDS; i++) {
      this.invisibleCards.add(new Card(rand));
    }
  }

  public CardCollection(List<Card> visibleCards, List<Card> invisibleCards) {
    this.visibleCards = new ArrayList<>();
    for (Card card : visibleCards) {
      this.visibleCards.add(card.getCardCopy());
    }
    this.invisibleCards = new ArrayList<>();
    for (Card card : invisibleCards) {
      this.invisibleCards.add(card.getCardCopy());
    }
  }

  // returns a deep copy of the visible cards in this collection
  public List<Card> getVisibleCardsCopy() {
    List<Card> deepCopy = new ArrayList<>();
    for (Card visibleCard : this.visibleCards) {
      deepCopy.add(visibleCard.getCardCopy());
    }
    return deepCopy;
  }

  // returns a deep copy of the invisible cards in this collection
  public List<Card> getInvisibleCardsCopy() {
    List<Card> deepCopy = new ArrayList<>();
    for (Card invisibleCard : this.invisibleCards) {
      deepCopy.add(invisibleCard.getCardCopy());
    }
    return deepCopy;
  }

  // returns a copy of this card collection
  public CardCollection getCardCollectionCopy() {
    return new CardCollection(this.visibleCards, this.invisibleCards);
  }

  // returns a new card collection with the given list of visible cards removed
  // throws exception if a visible card in the given list doesn't exist in the list of visible cards
  public CardCollection removeVisibleCards(List<Card> visibleCards) throws IllegalArgumentException {
    for (Card visibleCard : visibleCards) {
      if (!this.visibleCards.contains(visibleCard)) {
        throw new IllegalArgumentException("Visible card to be removed doesn't exist in visible cards.");
      }
      this.visibleCards.remove(visibleCard);
    }
    return new CardCollection(this.visibleCards, this.invisibleCards);
  }

  // returns a new card collection with the acquired visible cards replaced
  public CardCollection replaceAcquiredVisibleCards() {
    while (this.visibleCards.size() < Constants.NUM_VISIBLE_CARDS) {
      if (this.invisibleCards.isEmpty()) {
        break;
      }
      this.visibleCards.add(this.invisibleCards.remove(0));
    }
    return new CardCollection(this.visibleCards, this.invisibleCards);
  }

  // updates the card collection in the game according to the rules of the game
  // if there are invisible cards, remove the bottom card
  // if there are no invisible cards, remove all the visible cards
  public CardCollection removeBottomInvisibleCardOrAllVisible() {
    if (this.invisibleCards.size() > 0) {
      this.invisibleCards.remove(this.invisibleCards.size() - 1);
      return new CardCollection(this.visibleCards, this.invisibleCards);
    }
    return new CardCollection(new ArrayList<>(), new ArrayList<>());
  }

  // is this card collection empty?
  public boolean noMoreCards() {
    return this.visibleCards.isEmpty() && this.invisibleCards.isEmpty();
  }

  // is this card collection equal to the given object?
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CardCollection otherCardCollection)) {
      return false;
    }
    return this.visibleCards.equals(otherCardCollection.visibleCards)
            && this.invisibleCards.equals(otherCardCollection.invisibleCards);
  }
}
