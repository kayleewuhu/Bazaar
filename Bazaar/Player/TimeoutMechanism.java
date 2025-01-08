package Player;

import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Data.PlayerMethod;
import Common.Turn_State;

// This class represents a player that will infinitely loop after running a specified method
// a certain number of times.
public class TimeoutMechanism extends Mechanism {
  // the method that the player will infinitely loop in
  PlayerMethod timeOutMethod;
  // the number of times the above method must be called before infinite looping
  int timeOutCount;
  public TimeoutMechanism(String name, Strategy strategy, PlayerMethod timeOutMethod, int timeOutCount) {
    super(name, strategy);
    this.timeOutMethod= timeOutMethod;
    this.timeOutCount = timeOutCount;
  }

  // initializes the equations for the player to use
  @Override
  public void setup(EquationTable equations) {
    if (timeOutMethod == PlayerMethod.SETUP) {
      this.timeOut();
    }
    else {
      super.setup(equations);
    }
  }

  // returns whether the player is going to draw a pebble or perform a series of exchanges
  @Override
  public DrawPebbleOrExchanges requestPebbleOrExchanges(Turn_State turnState) {
    if (timeOutMethod == PlayerMethod.REQUEST_PEBBLE_OR_TRADES) {
      this.timeOut();
    }
      return super.requestPebbleOrExchanges(turnState);
  }

  // returns the cards that player will purchase
  @Override
  public CardPurchases requestCards(Turn_State turnState) {
    if (timeOutMethod == PlayerMethod.REQUEST_CARDS) {
      this.timeOut();
    }

    return super.requestCards(turnState);
  }

  // is informed if this player won or lost
  @Override
  public void win(boolean win) {
    if (timeOutMethod == PlayerMethod.WIN) {
      this.timeOut();
    }
    else {
      super.win(win);
    }
  }

  //decrease the count, if called the given amount of times, enter an infinite loop
  public void timeOut() {
    this.timeOutCount--;
    while (timeOutCount == 0) {
      if
      (Thread.interrupted()) {
        return;
      }
    }
  }
}
