package Player;

import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Turn_State;

// This interface represents what a player has to be able to respond to/how the referee will interact with player
public interface PlayerAPI {
  // returns the player's name
  String name();
  // sends the Equation Table of the game to the player
  void setup(EquationTable equations);
  // gets the player's request to either draw a pebble or perform exchanges
  DrawPebbleOrExchanges requestPebbleOrExchanges(Turn_State turnState);
  // gets the player's request to purchase cards
  CardPurchases requestCards(Turn_State turnState);
  // informs the player if they have won or not
  void win(boolean win);
}
