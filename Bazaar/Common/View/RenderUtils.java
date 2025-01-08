package Common.View;

import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;

public class RenderUtils {

  // Takes in two images and renders them beside each other
  public static BufferedImage besideAlignTop(BufferedImage left, BufferedImage right, int padding) {
    int totalWidth = left.getWidth() + right.getWidth() + padding;
    int maxHeight = Math.max(left.getHeight(), right.getHeight());
    BufferedImage combinedImage = new BufferedImage(totalWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = combinedImage.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.drawImage(left, 0, 0, null);
    g2d.drawImage(right, left.getWidth() + padding, 0, null);
    g2d.dispose();
    return combinedImage;
  }

  // Takes in a list of images and renders them all stacked beside each other
  public static BufferedImage horizontalStackAlignTop(List<BufferedImage> images, int padding) {
    if (images == null || images.isEmpty()) {
      return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }
    BufferedImage combinedImage = images.get(0);
    for (int i = 1; i < images.size(); i++) {
      combinedImage = besideAlignTop(combinedImage, images.get(i), padding);
    }
    return combinedImage;
  }

  // Takes in two images and renders them above and below each other
  public static BufferedImage aboveAlignedLeft(BufferedImage top, BufferedImage bottom, int padding) {
    int totalHeight = top.getHeight() + bottom.getHeight() + padding;
    int maxWidth = Math.max(top.getWidth(), bottom.getWidth());
    BufferedImage combinedImage = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = combinedImage.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.drawImage(top, 0, 0, null);
    g2d.drawImage(bottom, 0, top.getHeight() + padding, null);
    g2d.dispose();
    return combinedImage;
  }

  // Takes in a list of images and renders them all stacked above each other
  public static BufferedImage verticalStackAlignLeft(List<BufferedImage> images, int padding) {
    if (images == null || images.isEmpty()) {
      return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }
    BufferedImage combinedImage = images.get(0);
    for (int i = 1; i < images.size(); i++) {
      combinedImage = aboveAlignedLeft(combinedImage, images.get(i), padding);
    }
    return combinedImage;
  }

  // Takes in two images and renders the top overlayed onto the bottom
  public static BufferedImage overlayAlignTopLeft(BufferedImage top, BufferedImage bottom) {
    int width = Math.max(top.getWidth(), bottom.getWidth());
    int height = Math.max(top.getHeight(), bottom.getHeight());
    BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = combinedImage.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.drawImage(bottom, 0, 0, null);
    g2d.drawImage(top, 0, 0, null);
    g2d.dispose();
    return combinedImage;
  }

  // Renders an image with the given image on top of a rectangle background of a certain color
  public static BufferedImage createBackground(BufferedImage image, Color color) {
    int width = image.getWidth();
    int height = image.getHeight();
    BufferedImage background = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = background.createGraphics();
    g2d.setColor(color);
    g2d.fillRect(0, 0, width, height);
    return overlayAlignTopLeft(image, background);
  }

  // Renders the given text as an image
  public static BufferedImage text(String text, Color fontColor, int textSize) {
    BufferedImage dummy = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = dummy.createGraphics();
    g2d.setFont(new Font("Serif", Font.PLAIN, textSize));
    g2d.setColor(fontColor);
    FontMetrics fm = g2d.getFontMetrics();
    int textWidth = fm.stringWidth(text);
    int textHeight = fm.getAscent();
    BufferedImage textImage = new BufferedImage(textWidth,  textHeight, BufferedImage.TYPE_INT_ARGB);
    g2d = textImage.createGraphics();
    g2d.setFont(new Font("Serif", Font.PLAIN, textSize));
    g2d.setColor(fontColor);
    int x = (textImage.getWidth() - textWidth) / 2;
    int y = (textImage.getHeight() - fm.getHeight()) / 2 + textHeight;
    g2d.drawString(text, x, y);
    g2d.dispose();
    return textImage;
  }
}
