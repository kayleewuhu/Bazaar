package Common.View;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import Common.Turn_State;

public class TurnStateRenderer {

    // Renders a Turn State as an image at the specified path
    public static void renderImageFile(Turn_State turnState, String path) {
        BufferedImage image = renderImage(turnState);
        try {
            File outputFile = new File(path);
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Renders the Turn State as an image
    public static BufferedImage renderImage(Turn_State turnState) {
        int pebbleDiameter = 20;
        BufferedImage bankImage = PebbleCollectionRenderer.renderCountsImage(turnState.bankPebbles(), pebbleDiameter);
        bankImage = RenderUtils.aboveAlignedLeft(RenderUtils.text("Bank", Color.BLACK, pebbleDiameter), bankImage, 10);
        bankImage = RenderUtils.createBackground(bankImage, Color.PINK);
        BufferedImage playerImage = PlayerRenderer.renderImage(turnState.activePlayer());
        BufferedImage scoresImage = renderScoresList(turnState.remainingPlayerScores());
        List<BufferedImage> cardsAsImages = turnState.visibleCards().stream()
                .map(card -> CardRenderer.renderImage(card, 100, 200, 20, 40)).toList();
        BufferedImage cardsImage = RenderUtils.horizontalStackAlignTop(cardsAsImages, 30);
        return RenderUtils.verticalStackAlignLeft(Arrays.asList(bankImage, playerImage,
                scoresImage, cardsImage), 30);
    }

    // Renders a list of scores as an image
    private static BufferedImage renderScoresList(List<Integer> scores) {
        int fontSize = 20;
        List<String> scoresText = scores.stream().map(score -> Integer.toString(score)).toList();
        String scoresAsString = String.join(", ", scoresText);
        scoresAsString = "Remaining Scores: " + scoresAsString;
        return RenderUtils.createBackground(RenderUtils.text(scoresAsString, Color.BLACK, fontSize), Color.LIGHT_GRAY);
    }
}
