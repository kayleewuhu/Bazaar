package Common.View;

import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Common.Data.CardCollection;

public class CardCollectionRenderer {
  // Renders a card collection as an image at the specified path
  public static void renderImageFile(CardCollection cards, int horizontalPadding, int verticalPadding, int cardWidth, int cardHeight, int pebbleDiameter, int smileSize, String path) {
    BufferedImage image = renderImage(cards, horizontalPadding, verticalPadding, cardWidth, cardHeight, pebbleDiameter, smileSize);
    try {
      File outputFile = new File(path);
      ImageIO.write(image, "png", outputFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Renders the card collection as an image where the visible cards are on the first row and invisble
  // cards are on the second row
  public static BufferedImage renderImage(CardCollection cards, int horizontalPadding, int verticalPadding, int cardWidth, int cardHeight, int pebbleDiameter, int smileSize) {
    List<BufferedImage> visibleCardsAsImages = cards.getVisibleCardsCopy().stream()
            .map(card -> CardRenderer.renderImage(card, cardWidth, cardHeight, pebbleDiameter, smileSize)).toList();
    BufferedImage visibleCardsImage = RenderUtils.horizontalStackAlignTop(visibleCardsAsImages, horizontalPadding);
    List<BufferedImage> invisibleCardsAsImages = cards.getInvisibleCardsCopy().stream()
            .map(card -> CardRenderer.renderImage(card, cardWidth / 4, cardHeight / 4, pebbleDiameter / 4, smileSize/ 4)).toList();
    BufferedImage invisibleCardsImage = RenderUtils.horizontalStackAlignTop(invisibleCardsAsImages, horizontalPadding);
    return RenderUtils.aboveAlignedLeft(visibleCardsImage, invisibleCardsImage, verticalPadding);
  }
}
