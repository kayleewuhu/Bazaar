package Referee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import Common.Data.EquationTable;
import Common.View.GameStateRenderer;

/**
 * This class is an observer class that saves the individual states it receives as PNG files
 * and pops up a GUI to show the game states.
 *
 * To use this observer, we can attach it to the Referee, and the referee will send game states
 * every time they change.
 */
public class VisualObserver implements Observer {
  // all the game states that the observer has been given
  private List<Game_State> gameStates = new ArrayList<>();
  // the equations from the game
  private EquationTable equations;

  // receives the equations of the game
  @Override
  public void getEquations(EquationTable equations) {
    this.equations = equations;
  }

  // given an updated game state, save it as a png file and saves it in the observer
  @Override
  public void update(Game_State gameState) {
    String currentDir = System.getProperty("user.dir");
    File outputDir = new File(currentDir + File.separator + "Tmp");

    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    String directory = outputDir.toString();
    String path = directory + "/" + this.gameStates.size() + ".png";
    GameStateRenderer.renderImageFile(gameState, this.equations, path);
    this.gameStates.add(gameState);
  }

  // the game is over
  @Override
  public void gameOver() {
    GameView gui = new GameView(this.gameStates, this.equations);
  }
}
