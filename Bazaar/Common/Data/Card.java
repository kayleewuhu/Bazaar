package Common.Data;

import java.util.List;
import java.util.Random;

import Common.Constants;

// This class represents a card in the game of Bazaar which may contain a face and pebbles.
public class Card {
    // pebbles on the card, the cost of the card
    private final PebbleCollection pebbles;
    // does this card have a smiley face on it?
    private final boolean hasSmiley;

    public Card(Random rand) {
        this.pebbles = new PebbleCollection(Constants.NUM_PEBBLES_ON_CARD, rand);
        this.hasSmiley = rand.nextBoolean();
    }

    public Card(PebbleCollection pebbles, boolean hasSmiley) {
        this.pebbles = pebbles.getPebbleCollectionCopy();
        this.hasSmiley = hasSmiley;
    }

    // does this card have a face?
    public boolean hasSmiley() {
        return this.hasSmiley;
    }

    // returns a copy of this card's pebbles
    public PebbleCollection getCardPebblesCopy() {
        return this.pebbles.getPebbleCollectionCopy();
    }

    // returns a deep copy of this card
    public Card getCardCopy() {
        return new Card(this.pebbles, this.hasSmiley);
    }

    // is this card less than the given card?
    // less than meaning having no smiley is preferred, or if equal in smileyness, smaller pebble collection
    public boolean lessThanGivenCard(Card other) {
        if (!this.hasSmiley && other.hasSmiley) {
            return true;
        } else if (this.hasSmiley && !other.hasSmiley) {
            return false;
        } else {
            return this.pebbles.lessThanGivenPebbleCollection(other.pebbles);
        }
    }

    // does this card contain the given pebble color?
    public boolean containsColor(PebbleColor pebbleColor) {
        return this.pebbles.getPebbleCountForColor(pebbleColor) > 0;
    }

    // is the given object equal to this card?
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Card otherCard)) {
            return false;
        }
        return this.hasSmiley == otherCard.hasSmiley &&
                this.pebbles.equals(otherCard.pebbles);
    }
}
