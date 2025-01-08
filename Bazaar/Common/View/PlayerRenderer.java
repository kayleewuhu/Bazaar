package Common.View;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import Referee.PlayerInfo;

public class PlayerRenderer {

  // Renders a player as an image that contains their wallet and their score
  public static BufferedImage renderImage(PlayerInfo player) {
    int pebbleDiameter = 20;
    BufferedImage nameImage = RenderUtils.text(player.getName(), Color.BLACK, pebbleDiameter);
    BufferedImage walletImage = PebbleCollectionRenderer.renderCountsImage(player.getWalletCopy(), pebbleDiameter);
    BufferedImage scoreImage = RenderUtils.text("Score: " + player.getScore(), Color.BLACK, pebbleDiameter);
    BufferedImage playerImage = RenderUtils.verticalStackAlignLeft(Arrays.asList(nameImage, walletImage, scoreImage), 10);
    return RenderUtils.createBackground(playerImage, Color.ORANGE);
  }
}
