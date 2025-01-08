package Player;

import java.util.List;

import Common.Data.CardPurchases;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Data.PebbleCollection;
import Common.Data.UnidirectionalEquation;
import Common.Pair;
import Common.Turn_State;

// This is a strategy that does nothing for all moves
public class DoNothing implements Strategy {

  // should a random pebble be drawn
  public boolean shouldDrawPebble(Turn_State turnState, EquationTable equationTable) {
    PebbleCollection wallet = turnState.activePlayer().getWalletCopy();
    PebbleCollection bank = turnState.bankPebbles();
    List<UnidirectionalEquation> availableExchanges = equationTable.filter(wallet, bank);
    return availableExchanges.isEmpty();
  }

  // don't do any exchanges or card purchases
  public Pair<Exchanges, CardPurchases> whatExchangesAndCardPurchases(Turn_State turnState, EquationTable equationTable) {
    return new Pair<>(new Exchanges(), new CardPurchases());
  }

  // don't do any card purchases
  public CardPurchases whatCardPurchases(Turn_State turnState, EquationTable equationTable) {
    return new CardPurchases();
  }
}
