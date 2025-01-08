package Common.View;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import Common.Constants;
import Common.Data.BidirectionalEquation;

public class EquationRenderer {

  // Renders the BidirectionalEquation as an image
  public static BufferedImage renderBidirectionalImage(BidirectionalEquation equation, int pebbleDiameter) {
    // Left side of equation image
    BufferedImage leftSideImage = PebbleCollectionRenderer.renderHorizontalImage(equation.getLeftSideCopy(),
            pebbleDiameter, Constants.MAX_PEBBLES_PER_EQUATION_SIDE, false);
    // Symbol image
    BufferedImage symbolImage = RenderUtils.text(equation.getSymbol(), Color.BLACK, pebbleDiameter);
    // Right side of equation image
    BufferedImage rightSideImage = PebbleCollectionRenderer.renderHorizontalImage(equation.getRightSideCopy(),
            pebbleDiameter, Constants.MAX_PEBBLES_PER_EQUATION_SIDE, true);
    return RenderUtils.horizontalStackAlignTop(Arrays.asList(leftSideImage, symbolImage, rightSideImage), 10);
  }
}
