package Common.View;

import Common.Data.Card;
import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CardRenderer {
    // Renders a Card as an image at the specified path
    public static void renderImageFile(Card card, int width, int height, int pebbleDiameter, int smileSize, String path) {
        BufferedImage image = renderImage(card, width, height, pebbleDiameter, smileSize);
        try {
            File outputFile = new File(path);
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // renders a Card as an image
    public static BufferedImage renderImage(Card card, int width, int height, int pebbleDiameter, int smileSize) {
        BufferedImage cardImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = cardImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Create cyan background
        g2d.setColor(Color.CYAN);
        g2d.fillRect(0, 0, width, height);
        // Create a black border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, width - 1, height - 1);
        // Draw circle of pebbles
        drawPebbleRing(g2d, card.getCardPebblesCopy(), width, height, pebbleDiameter);
        // Draw face
        if (card.hasSmiley()) {
            drawFace(g2d, width, height, smileSize);
        }
        return cardImage;
    }

    // Draws a ring of pebbles that represents the pebble collection on the card to the image
    private static void drawPebbleRing(Graphics2D g2d, PebbleCollection pebbles, int width, int height, int pebbleDiameter) {
        int ringRadius = width / 2 - pebbleDiameter / 2;
        int centerX = width / 2;
        int centerY = height / 2;
        // Find angle to rotate by based on number of pebbles on card
        double angleIncrement = 2 * Math.PI / pebbles.getTotalNumberOfPebbles();
        // Start drawing at the top instead of right
        double startAngle = -Math.PI / 2;
        int pebblesDrawn = 0;
        for (PebbleColor color : pebbles.getPebblesMapCopy().keySet()) {
            for (int i = 0; i < pebbles.getPebblesMapCopy().get(color); i++) {
                // Calculate the new angle to draw the pebble at and where to place it
                double angle = startAngle + pebblesDrawn * angleIncrement;
                int circleX = centerX + (int) (ringRadius * Math.cos(angle)) - pebbleDiameter / 2;
                int circleY = centerY + (int) (ringRadius * Math.sin(angle)) - pebbleDiameter / 2;
                g2d.setColor(color.toColor());
                g2d.drawImage(PebbleRenderer.renderImage(color, pebbleDiameter), circleX, circleY, null);
                pebblesDrawn++;
            }
        }
    }

    // Draws a face in the center of the card image
    private static void drawFace(Graphics2D g2d, int width, int height, int smileSize) {
        int centerX = width / 2;
        int centerY = height / 2;
        try {
            InputStream imgStream = CardRenderer.class.getClassLoader().getResourceAsStream("Common/Assets/smiley.png");
            BufferedImage img = ImageIO.read(imgStream);
            g2d.drawImage(img, centerX-smileSize/2, centerY-smileSize/2, smileSize, smileSize, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
