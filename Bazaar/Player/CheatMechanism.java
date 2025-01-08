package Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Common.Data.BidirectionalEquation;
import Common.Data.Card;
import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.Exchanges;
import Common.Data.PebbleCollection;
import Common.Data.UnidirectionalEquation;
import Common.Rule_Book;
import Common.Turn_State;

// This class represents a player that follows a given strategy
// however it will cheat according to the cheat provided.
public class CheatMechanism extends Mechanism {
  // name of this player
  private String name;
  // the way this player will cheat
  private String cheat;

  public CheatMechanism(String name, Strategy strategy, String cheat) {
    super(name, strategy);
    this.name = name;
    this.cheat = cheat;
  }

  // gets the player's request to either draw a pebble or perform exchanges
  // will perform an illegal move based on the player's cheat
  @Override
  public DrawPebbleOrExchanges requestPebbleOrExchanges(Turn_State turnState) {
    switch(this.cheat) {
      case "wallet-cannot-trade":
        return this.walletCannotTrade(turnState);
      case "bank-cannot-trade":
        return this.bankCannotTrade(turnState);
      case "use-non-existent-equation":
        return this.getNonExistentExchanges(turnState);
      default:
        return super.requestPebbleOrExchanges(turnState);
    }
  }

  // gets the player's request to purchase cards
  // will return an illegal move based on the player's cheat
  @Override
  public CardPurchases requestCards(Turn_State turnState) {
    switch(this.cheat) {
      case "buy-unavailable-card":
        this.getNonExistentCardPurchases(turnState);
      case "wallet-cannot-buy-card":
        return this.buyCardThatCannotBeBought(turnState);
      default:
        return super.requestCards(turnState);
    }
  }

  // returns an exchange that contains an equation NOT in the
  // game's equation table
  private Exchanges getNonExistentExchanges(Turn_State turnState) {
    while (true) {
      BidirectionalEquation eq = new BidirectionalEquation(new Random());
      List<UnidirectionalEquation> trades = eq.convertToUnidirectionalEquation();
      if(!this.equationTable.contains(trades.get(0))) {
        return new Exchanges(trades);
      }
    }
  }

  // returns a list of exchanges that cannot be done since the active
  // player's wallet does not have enough pebbles
  private DrawPebbleOrExchanges walletCannotTrade(Turn_State turnState) {
    PebbleCollection wallet = turnState.activePlayer().getWalletCopy();
    for(BidirectionalEquation eq : equationTable.getAllEquationsCopy()) {
      List<UnidirectionalEquation> trades = eq.convertToUnidirectionalEquation();
      UnidirectionalEquation tradeLeft = trades.get(0);
      UnidirectionalEquation tradeRight = trades.get(1);
      if(!tradeLeft.canWalletExchange(wallet) || !tradeRight.canWalletExchange(wallet)) {
        return new Exchanges(trades);
      }
    }
    return super.requestPebbleOrExchanges(turnState);
  }

  // returns a list of exchanges that cannot be done since the bank
  // does not have enough pebbles
  private DrawPebbleOrExchanges bankCannotTrade(Turn_State turnState) {
    PebbleCollection bank = turnState.bankPebbles();
    for(BidirectionalEquation eq : equationTable.getAllEquationsCopy()) {
      List<UnidirectionalEquation> trades = eq.convertToUnidirectionalEquation();
      UnidirectionalEquation tradeLeft = trades.get(0);
      UnidirectionalEquation tradeRight = trades.get(1);
      if(!tradeLeft.canBankExchange(bank) || !tradeRight.canBankExchange(bank)) {
        return new Exchanges(trades);
      }
    }
    return super.requestPebbleOrExchanges(turnState);
  }

  // returns a non-existent card as a purchase request to cheat
  private CardPurchases getNonExistentCardPurchases(Turn_State turnState) {
    while (true) {
      Card card = new Card(new Random());
      if(!turnState.visibleCards().contains(card)) {
        return new CardPurchases(new ArrayList<>(Arrays.asList(card)));
      }
    }
  }

  // buy a card that cannot be bought (not enough pebbles in wallet)
  private CardPurchases buyCardThatCannotBeBought(Turn_State turnState) {
    List<Card> visible = turnState.visibleCards();
    PebbleCollection wallet = turnState.activePlayer().getWalletCopy();

    for (Card card : turnState.visibleCards()) {
      if (!Rule_Book.canBuyCard(visible, wallet, card)) {
        return new CardPurchases(new ArrayList<>(Arrays.asList(card)));
      }
    }
    return super.requestCards(turnState);
  }
}
