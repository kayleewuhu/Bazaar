package Common.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import Common.Pair;
import Common.Rule_Book;
import Common.Turn_State;

// This class represents the cards that are purchased in a single turn.
public class CardPurchases {
  // the cards that have been purchased in order
  private final List<Card> sequenceOfCardPurchases;

  public CardPurchases() {
    this.sequenceOfCardPurchases = new ArrayList<>();
  }

  public CardPurchases(List<Card> sequenceOfCardPurchases) {
    this.sequenceOfCardPurchases = sequenceOfCardPurchases.stream()
            .map(Card::getCardCopy).collect(Collectors.toList());
  }

  // returns a copy of this sequence of card purchases
  public List<Card> getSequenceOfCardPurchasesCopy() {
    return this.sequenceOfCardPurchases.stream().map(Card::getCardCopy).collect(Collectors.toList());
  }

  // returns a copy of this Card Purchases
  public CardPurchases getCardPurchasesCopy() {
    return new CardPurchases(this.getSequenceOfCardPurchasesCopy());
  }

  // calculates the total amount of points of this sequence of card purchases using the given wallet
  public int totalPointsWorth(PebbleCollection wallet) {
    PebbleCollection currentWallet = wallet.getPebbleCollectionCopy();
    int points = 0;
    for (Card card : this.sequenceOfCardPurchases) {
      points += Rule_Book.pointsYieldedIfCardBought(card, currentWallet);
      currentWallet = currentWallet.removePebbles(card.getCardPebblesCopy());
    }
    return points;
  }

  // returns how many cards are in this card purchases
  public int totalCards() {
    return this.sequenceOfCardPurchases.size();
  }

  // attempts to do this action, if it is invalid, it returns an empty Optional, else it performs the action and
  // returns the resulting player wallet and bank
  public Optional<Pair<PebbleCollection, PebbleCollection>> attemptAction(Turn_State turnState) {
    PebbleCollection newWallet = turnState.activePlayer().getWalletCopy();
    PebbleCollection newBank = turnState.bankPebbles().getPebbleCollectionCopy();
    // Makes a copy of visible cards in turn state since records are immutable
    List<Card> visibleCards = new ArrayList<>(turnState.visibleCards());
    for (Card card : this.sequenceOfCardPurchases) {
      // check if card can be bought
      if (!Rule_Book.canBuyCard(visibleCards, newWallet, card)) {
        return Optional.empty();
      }
      // update variables
      newWallet = newWallet.removePebbles(card.getCardPebblesCopy());
      newBank = newBank.addPebbles(card.getCardPebblesCopy());
      visibleCards.remove(card);
    }
    return Optional.of(new Pair<>(newWallet, newBank));
  }

  // is this card purchases equal to the given object?
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof CardPurchases otherCardPurchases)) {
      return false;
    }
    return this.sequenceOfCardPurchases.equals(otherCardPurchases.sequenceOfCardPurchases);
  }
}
