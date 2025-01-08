package Common.Data;

import java.awt.*;
import java.util.Random;

// This enum represents all the possible colors of a Pebble.
public enum PebbleColor {
    RED, WHITE, BLUE, GREEN, YELLOW;

    // returns the corresponding Color object
    public Color toColor() {
        return switch (this) {
            case RED -> Color.RED;
            case WHITE -> Color.WHITE;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
            case YELLOW -> Color.YELLOW;
        };
    }

    // returns a random Pebble color using the given random object
    public static PebbleColor getRandomPebble(Random rand) {
        PebbleColor[] pebbles = PebbleColor.values();
        return pebbles[rand.nextInt(pebbles.length)];
    }

    public static PebbleColor fromString(String colorStr) {
        return valueOf(colorStr.toUpperCase());
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public char toChar() {
        return this.toString().charAt(0);
    }
}
