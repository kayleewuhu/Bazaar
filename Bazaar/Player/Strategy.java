package Player;

import Common.Data.CardPurchases;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Turn_State;
import Common.Pair;

// Represents a Strategy that can be performed given the state during a turn
public interface Strategy {
  // should a random pebble be drawn
  boolean shouldDrawPebble(Turn_State turnState, EquationTable equationTable);
  // what exchanges and card purchases to do
  Pair<Exchanges, CardPurchases> whatExchangesAndCardPurchases(Turn_State turnState, EquationTable equationTable);
  // what card purchases to do
  CardPurchases whatCardPurchases(Turn_State turnState, EquationTable equationTable);
}
