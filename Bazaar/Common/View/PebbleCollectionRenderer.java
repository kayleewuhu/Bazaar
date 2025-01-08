package Common.View;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;

public class PebbleCollectionRenderer {

  // totalSpaceForPebbles is how many pebbles this image should hold. If it's less than the number of pebbles in the
  // collection, some pebbles will be cut out. If it's more than the number of pebbles, invisible "pebbles" / space
  // will be drawn either on the right or leftside.
  // alignLeft is whether the drawn pebbles should be aligned on the left or right
  public static BufferedImage renderHorizontalImage(PebbleCollection pebbles, int pebbleDiameter, int totalSpaceForPebbles, boolean alignLeft) {
    List<PebbleColor> pebblesList = pebbles.getPebblesAsList();
    int pebblesListIdx = 0;
    List<BufferedImage> pebblesAsImages = new ArrayList<>();
    // How many empty spaces should we draw
    int emptyPebblesToDraw = Math.max(0, totalSpaceForPebbles - pebblesList.size());
    // Builds the list of images with the drawn pebbles aligned to the right and blank space on the left
    for (int i = 0; i < totalSpaceForPebbles; i++) {
      if (emptyPebblesToDraw > 0) {
        pebblesAsImages.add(new BufferedImage(pebbleDiameter, pebbleDiameter, BufferedImage.TYPE_INT_ARGB));
        emptyPebblesToDraw--;
      } else {
        pebblesAsImages.add(PebbleRenderer.renderImage(pebblesList.get(pebblesListIdx), pebbleDiameter));
        pebblesListIdx++;
      }
    }
    // If pebbles should be on left, reverse list
    if (alignLeft) {
      Collections.reverse(pebblesAsImages);
    }
    return RenderUtils.horizontalStackAlignTop(pebblesAsImages, 0);
  }

  // for rendering a wallet or bank
  public static BufferedImage renderCountsImage(PebbleCollection pebbles, int pebbleDiameter) {
    List<BufferedImage> pebbleCountAsImages = new ArrayList<>();
    Map<PebbleColor, Integer> counts = pebbles.getPebblesMapCopy();
    for (PebbleColor color : PebbleColor.values()) {
      int count = counts.getOrDefault(color, 0);
      BufferedImage pebble = PebbleRenderer.renderImage(color, pebbleDiameter);
      BufferedImage countText = RenderUtils.text(": " + count, Color.BLACK, pebbleDiameter);
      pebbleCountAsImages.add(RenderUtils.besideAlignTop(pebble, countText, 0));
    }
    return RenderUtils.verticalStackAlignLeft(pebbleCountAsImages, 10);
  }
}
