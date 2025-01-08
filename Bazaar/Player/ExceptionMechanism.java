package Player;

import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Data.PlayerMethod;
import Common.Turn_State;

// This class represents a player that follows a given strategy
// however it will throw an exception when the corresponding method is called
public class ExceptionMechanism extends Mechanism {
  // method that will throw the exception
  private final PlayerMethod exceptionMethod;

  public ExceptionMechanism(String name, Strategy strategy, PlayerMethod exceptionMethod) {
    super(name, strategy);
    this.exceptionMethod = exceptionMethod;
  }

  // initializes the equations for the player to use
  @Override
  public void setup(EquationTable equations) throws RuntimeException {
    if (exceptionMethod == PlayerMethod.SETUP) {
      throw new RuntimeException("setup Failed");
    }
    super.setup(equations);
  }

  // returns whether the player is going to draw a pebble or perform a series of exchanges
  @Override
  public DrawPebbleOrExchanges requestPebbleOrExchanges(Turn_State turnState) throws RuntimeException {
    if (exceptionMethod == PlayerMethod.REQUEST_PEBBLE_OR_TRADES) {
      throw new RuntimeException("requestPebbleOrExchanges Failed");
    }
    return super.requestPebbleOrExchanges(turnState);
  }

  // returns the cards that player will purchase
  @Override
  public CardPurchases requestCards(Turn_State turnState) throws RuntimeException {
    if (exceptionMethod == PlayerMethod.REQUEST_CARDS) {
      throw new RuntimeException("requestCards Failed");
    }
    return super.requestCards(turnState);
  }

  // is informed if this player won or lost
  @Override
  public void win(boolean win) throws RuntimeException {
    if (exceptionMethod == PlayerMethod.WIN) {
      throw new RuntimeException("win Failed");
    }
  }
}
