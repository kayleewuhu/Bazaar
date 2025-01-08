package Tests.JsonObjects;

import java.util.List;
import java.util.stream.Collectors;

import Common.Data.CardPurchases;

public class CardPurchasesJson {
  public List<CardJson> cards;

  public CardPurchasesJson (CardPurchases cards) {
    this.cards = cards.getSequenceOfCardPurchasesCopy().stream().map(CardJson::new).collect(Collectors.toList());
  }
}
