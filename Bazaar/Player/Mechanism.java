package Player;

import Common.Data.CardPurchases;
import Common.Data.DrawPebble;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Turn_State;

// This class represents a player created by the server, which follows a specified strategy.
public class Mechanism implements PlayerAPI {
  // name of this player
  private String name;
  // the player's strategy that determines what the next move will be
  Strategy strategy;
  // the equation table to follow
  EquationTable equationTable;

  public Mechanism(String name, Strategy strategy) {
    this.name = name;
    this.strategy = strategy;
  }

  // returns the name of this player
  @Override
  public String name() {
    return this.name;
  }

  // initializes the equations for the player to use
  @Override
  public void setup(EquationTable equations) {
    this.equationTable = equations;
  }

  // returns whether the player is going to draw a pebble or perform a series of exchanges
  @Override
  public DrawPebbleOrExchanges requestPebbleOrExchanges(Turn_State turnState) {
    if (this.strategy.shouldDrawPebble(turnState, this.equationTable)) {
      return new DrawPebble();
    }
    return this.strategy.whatExchangesAndCardPurchases(turnState, this.equationTable).first;
  }

  // returns the cards that player will purchase
  @Override
  public CardPurchases requestCards(Turn_State turnState) {
    return this.strategy.whatCardPurchases(turnState, this.equationTable);
  }

  // is informed if this player won or lost
  @Override
  public void win(boolean win) {}
}
