package Common;

import Common.Data.Card;
import Common.Data.PebbleColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// This class represents the USA game mode, where a player will earn extra bonus points if
// the cards they buy contain the colors red, white, and blue.
public class USAGameMode implements IGameMode {
    // colors needed to earn points
    private final List<PebbleColor> colors = new ArrayList<>(List.of(PebbleColor.RED, PebbleColor.WHITE, PebbleColor.BLUE));

    // calculates the bonus points the given cards will earn in this game mode
    public int calculateBonus(List<Card> cards) {
        HashMap<PebbleColor, Boolean> colorFlags = this.initializeFlagsMap();

        for (Card card : cards) {
            for (PebbleColor color : colors) {
                if (card.containsColor(color)) {
                    colorFlags.put(color, true);
                }
            }
        }
        if (!colorFlags.containsValue(false)) {
            return Constants.USA_BONUS_POINTS;
        }
        return Constants.DEFAULT_BONUS_POINTS;
    }

    // initializes flags for all required colors in this mode to false
    private HashMap<PebbleColor, Boolean> initializeFlagsMap() {
        HashMap<PebbleColor, Boolean> colorFlags = new HashMap<>();

        for (PebbleColor color : this.colors) {
            colorFlags.put(color, false);
        }
        return colorFlags;
    }
}
