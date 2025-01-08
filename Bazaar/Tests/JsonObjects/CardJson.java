package Tests.JsonObjects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import Common.Data.Card;
import Common.Data.PebbleCollection;

public class CardJson {
  private List<String> pebbles;

  @SerializedName("face?")
  private boolean hasSmiley;

  public CardJson(Card card) {
    this.pebbles = new PebbleCollectionJson(card.getCardPebblesCopy()).toListOfString();
    this.hasSmiley = card.hasSmiley();
  }

  public Card parseIntoObject() {
    PebbleCollectionJson pebblesJson = new PebbleCollectionJson(this.pebbles);
    PebbleCollection pebbleCollection = pebblesJson.parseIntoObject();
    return new Card(pebbleCollection, hasSmiley);
  }
}
