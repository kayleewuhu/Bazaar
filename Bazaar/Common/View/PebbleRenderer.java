package Common.View;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Common.Data.PebbleColor;

public class PebbleRenderer {

  // Renders a pebble as an image
  public static BufferedImage renderImage(PebbleColor pebble, int diameter) {
    BufferedImage circleImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = circleImage.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(pebble.toColor());
    g2d.fillOval(0, 0, diameter, diameter);
    g2d.dispose();
    return circleImage;
  }
}
