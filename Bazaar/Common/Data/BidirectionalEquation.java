package Common.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Common.Constants;
import Common.Pair;

/**
 * An equation that can be used left to right or right to left to exchange pebbles
 */
public class BidirectionalEquation {
    // left side of the equation
    private final PebbleCollection leftSide;
    // right side of the equation
    private final PebbleCollection rightSide;

    public BidirectionalEquation(Random rand)  {
        Pair<PebbleCollection, PebbleCollection> sides = this.setupEquation(rand);
        this.leftSide = sides.first;
        this.rightSide = sides.second;
    }

    public BidirectionalEquation(PebbleCollection leftSide, PebbleCollection rightSide) {
        this.leftSide = leftSide.getPebbleCollectionCopy();
        this.rightSide = rightSide.getPebbleCollectionCopy();
    }

    // Randomly generates equation, no same pebble color on both sides, each side pebbles are between
    // Constants.MIN_PEBBLES_PER_EQUATION_SIDE and Constants.MAX_PEBBLES_PER_EQUATION_SIDE
    private Pair<PebbleCollection, PebbleCollection> setupEquation(Random rand) {
        PebbleCollection leftSide = new PebbleCollection();
        PebbleCollection rightSide = new PebbleCollection();
        List<PebbleColor> colors = new ArrayList<>(List.of(PebbleColor.values()));
        // puts a random color into the PebbleCollection with a random quantity
        leftSide = leftSide.putPebbleQuantity(colors.remove(rand.nextInt(colors.size())), randomQuantity(Constants.MAX_PEBBLES_PER_EQUATION_SIDE, rand));
        rightSide = rightSide.putPebbleQuantity(colors.remove(rand.nextInt(colors.size())), randomQuantity(Constants.MAX_PEBBLES_PER_EQUATION_SIDE, rand));

        while (colors.size() > 0) {
            PebbleColor color = colors.remove(colors.size() - 1);
            // Should we add this color
            if (rand.nextBoolean()) {
                // Which side to add the pebble on
                if (rand.nextBoolean()) {
                    // Add to leftSide
                    leftSide = leftSide.putPebbleQuantity(color,
                            randomQuantity(Constants.MAX_PEBBLES_PER_EQUATION_SIDE - leftSide.getTotalNumberOfPebbles(), rand));
                } else {
                    // Add to rightSide
                    rightSide = rightSide.putPebbleQuantity(color,
                            randomQuantity(Constants.MAX_PEBBLES_PER_EQUATION_SIDE - rightSide.getTotalNumberOfPebbles(), rand));
                }
            }
        }
        return new Pair<>(leftSide, rightSide);
    }

    // gets a deep copy of this equation
    public BidirectionalEquation getBidirectionalEquationCopy() {
        return new BidirectionalEquation(this.leftSide, this.rightSide);
    }

    // returns a random quantity of pebbles up to the given number, using the provided random object
    private int randomQuantity(int spaceLeft, Random rand) {
        return Math.min(rand.nextInt(Constants.MAX_PEBBLES_PER_EQUATION_SIDE)
                + Constants.MIN_PEBBLES_PER_EQUATION_SIDE, spaceLeft);
    }

    // gets a deep copy of the left side of the equation
    public PebbleCollection getLeftSideCopy() {
        return this.leftSide.getPebbleCollectionCopy();
    }

    // gets a deep copy of the right side of the equation
    public PebbleCollection getRightSideCopy() {
        return this.rightSide.getPebbleCollectionCopy();
    }

    // returns the symbol to be rendered for bidirectional equations
    public String getSymbol() {
        return "↔";
    }

    // converts this bidirectional equation into its unidirectional equations equals
    public List<UnidirectionalEquation> convertToUnidirectionalEquation() {
        return new ArrayList<>(Arrays.asList(new UnidirectionalEquation(this.leftSide, this.rightSide),
                new UnidirectionalEquation(this.rightSide, this.leftSide)));
    }

    // is this bidirectional equation equal to the given one?
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BidirectionalEquation otherEquation)) {
            return false;
        }
        return (this.leftSide.equals(otherEquation.leftSide) && this.rightSide.equals(otherEquation.rightSide) ||
                (this.leftSide.equals(otherEquation.rightSide) && this.rightSide.equals(otherEquation.leftSide)));
    }
}
