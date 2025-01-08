package Referee;

import Common.Data.EquationTable;

/**
 * this interface represents the observer interface that can be used to inspect
 * a game from a referee's perspective
 */
public interface Observer {
  /**
   * provides the equation table to the observer
   * @param equations the game's equations
   */
  void getEquations(EquationTable equations);
  /**
   * consumes a game state to update the observer about a change in the state of the game
   * @param gameState takes in a game state
   */
  void update(Game_State gameState);
  /**
   * notifies observer that the game is over
   */
  void gameOver();
}
