package Common.View;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import Common.Data.BidirectionalEquation;
import Common.Data.EquationTable;

public class EquationTableRenderer {

    // Renders a Equation Table as an image at the specified path
    public static void renderImageFile(EquationTable equations, int pebbleDiameter, String path) {
        BufferedImage image = renderImage(equations, pebbleDiameter);
        try {
            File outputFile = new File(path);
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Renders the Equation Table as an image
    public static BufferedImage renderImage(EquationTable equations, int pebbleDiameter) {
        List<BufferedImage> equationAsImages = equations.getAllEquationsCopy().stream()
                .map(equation -> EquationRenderer.renderBidirectionalImage(equation, pebbleDiameter)).toList();
        BufferedImage equationsImage = RenderUtils.verticalStackAlignLeft(equationAsImages, 10);
        return RenderUtils.createBackground(equationsImage, Color.PINK);
    }
}
