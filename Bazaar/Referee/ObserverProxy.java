package Referee;

import java.util.Optional;

import Common.Data.EquationTable;

// Sits in between the Referee and each observer to catch errors an observer might throw
// Calls the methods on the given observer and return an Optional Empty if an error is thrown by the observer
public class ObserverProxy {
  // observer receives the equations of the game
  public static Optional<Boolean> getEquations(Observer observer, EquationTable equations) {
    try {
      observer.getEquations(equations);
      return Optional.of(true);
    }
    catch (Exception e) {
      return Optional.empty();
    }
  }

  // observer is given the updated game state
  public static Optional<Boolean> update(Observer observer, Game_State gameState) {
    try {
      observer.update(gameState);
      return Optional.of(true);
    }
    catch (Exception e) {
      return Optional.empty();
    }
  }

  // observer is notified that the game is over
  public static Optional<Boolean> gameOver(Observer observer) {
    try {
      observer.gameOver();
      return Optional.of(true);
    }
    catch (Exception e) {
      return Optional.empty();
    }
  }
}
