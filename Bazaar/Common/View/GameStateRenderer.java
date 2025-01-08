package Common.View;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import Common.Data.EquationTable;
import Referee.Game_State;


public class GameStateRenderer {

  // Renders a Game State as an image at the specified path
  public static void renderImageFile(Game_State gameState, EquationTable equations, String path) {
    BufferedImage image = renderImage(gameState, equations);
    try {
      File outputFile = new File(path);
      ImageIO.write(image, "png", outputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Renders the Game State as an image
  public static BufferedImage renderImage(Game_State gameState, EquationTable equations) {
    int pebbleDiameter = 20;
    BufferedImage bankImage = PebbleCollectionRenderer.renderCountsImage(gameState.getBank(), pebbleDiameter);
    bankImage = RenderUtils.aboveAlignedLeft(RenderUtils.text("Bank", Color.BLACK, pebbleDiameter), bankImage, 10);
    bankImage = RenderUtils.createBackground(bankImage, Color.PINK);
    BufferedImage cardsImage = CardCollectionRenderer.renderImage(gameState.getCards(), 10, 15, 100, 200, pebbleDiameter, 40);
    List<BufferedImage> playersAsImage = gameState.getPlayersAsList().stream().map(PlayerRenderer::renderImage).toList();
    BufferedImage playersImage = RenderUtils.horizontalStackAlignTop(playersAsImage, 30);
    BufferedImage gameStateImage = RenderUtils.verticalStackAlignLeft(Arrays.asList(bankImage, cardsImage, playersImage), 20);
    BufferedImage equationsImage = EquationTableRenderer.renderImage(equations, pebbleDiameter);
    return RenderUtils.besideAlignTop(equationsImage, gameStateImage, 20);
  }
}
