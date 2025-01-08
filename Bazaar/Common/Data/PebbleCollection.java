package Common.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Common.Pair;

/**
 * Represents a collection of pebbles.
 * The Map will always be initialized to have all the values of the PebbleColor Enum as keys, with all values 0/positive.
 */
public class PebbleCollection {
    private final Map<PebbleColor, Integer> pebbles;

    public PebbleCollection(Map<PebbleColor, Integer> pebbles) throws IllegalArgumentException {
        this.pebbles = new HashMap<>(pebbles);
        for (PebbleColor color : PebbleColor.values()) {
            this.pebbles.putIfAbsent(color, 0);
            if (this.pebbles.get(color) < 0) {
                throw new IllegalArgumentException("Pebble collection cannot have negative values.");
            }
        }
    }

    public PebbleCollection(List<PebbleColor> pebbles) {
        this();
        for (PebbleColor pebble : pebbles) {
            this.pebbles.put(pebble, this.pebbles.get(pebble) + 1);
        }
    }

    // initializes an empty pebble collection
    public PebbleCollection() {
        this(0);
    }

    // creates a pebble collection where every color has starting quantity amount
    public PebbleCollection(int startingQuantity) throws IllegalArgumentException {
        if (startingQuantity < 0) {
            throw new IllegalArgumentException("Pebble collection cannot have negative values.");
        }
        this.pebbles = new HashMap<>();
        for (PebbleColor color : PebbleColor.values()) {
            this.pebbles.put(color, startingQuantity);
        }
    }

    // creates a pebble collection of size totalPebbles, randomly
    // choosing the colors for the pebbles
    public PebbleCollection(int totalPebbles, Random rand) {
        this();
        this.generateRandomPebbles(totalPebbles, rand);
    }

    // randomly generates pebbles of given amount using given random object
    private void generateRandomPebbles(int totalPebbles, Random rand) {
        PebbleColor[] colors = PebbleColor.values();
        for (int i = 0; i < totalPebbles; i++) {
            PebbleColor pebble = colors[rand.nextInt(colors.length)];
            this.pebbles.put(pebble, this.pebbles.getOrDefault(pebble, 0) + 1);
        }
    }

    // removes a random pebble from this pebble collection and returns it and the new pebble collection
    public Pair<PebbleColor, PebbleCollection> takeRandomPebble() throws IllegalStateException {
        PebbleCollection newPebbleCollection = this.getPebbleCollectionCopy();
        Random rand = new Random();
        if (this.getTotalNumberOfPebbles() <= 0) {
            throw new IllegalStateException("Out of pebbles. None to remove.");
        }
        while (true) {
            PebbleColor randomColor = PebbleColor.getRandomPebble(rand);
            if (newPebbleCollection.pebbles.getOrDefault(randomColor, 0) > 0) {
                newPebbleCollection.pebbles.put(randomColor, this.pebbles.getOrDefault(randomColor, 0) - 1);
                return new Pair<>(randomColor, newPebbleCollection);
            }
        }
    }

    // draws a pebble deterministically from this pebble collection, in a specific order
    public Pair<PebbleColor, PebbleCollection> drawDeterministicPebble() throws IllegalStateException {
        PebbleCollection newPebbleCollection = this.getPebbleCollectionCopy();
        PebbleColor[] pebbleColors = {PebbleColor.RED, PebbleColor.WHITE, PebbleColor.BLUE,
                PebbleColor.GREEN, PebbleColor.YELLOW};
        for (PebbleColor color : pebbleColors) {
            if (newPebbleCollection.getPebbleCountForColor(color) > 0) {
                newPebbleCollection = newPebbleCollection.updatePebbleQuantity(color, -1);
                return new Pair<>(color, newPebbleCollection);
            }
        }
        throw new IllegalStateException("Out of pebbles. None to remove.");
    }

    // copies the pebble map in this collection
    public Map<PebbleColor, Integer> getPebblesMapCopy() {
        return new HashMap<>(this.pebbles);
    }

    // returns a copy of this pebble collection
    public PebbleCollection getPebbleCollectionCopy() {
        return new PebbleCollection(this.getPebblesMapCopy());
    }

    // gets pebbles as a list of pebble colors
    public List<PebbleColor> getPebblesAsList() {
        List<PebbleColor> pebbles = new ArrayList<>();
        for (PebbleColor pebble : this.pebbles.keySet()) {
            for (int count = 0; count < this.pebbles.get(pebble); count++) {
                pebbles.add(pebble);
            }
        }
        return pebbles;
    }

    // returns total number of pebbles in this collection
    public int getTotalNumberOfPebbles() {
        return this.pebbles.values().stream().mapToInt(Integer::intValue).sum();
    }

    // returns the number of pebbles of the given color in this collection
    public int getPebbleCountForColor(PebbleColor color) {
        return this.pebbles.getOrDefault(color, 0);
    }

    // is this pebble collection empty?
    public boolean outOfPebbles() {
        return this.getTotalNumberOfPebbles() == 0;
    }

    // puts the given amount of pebbles of the given color to this collection
    public PebbleCollection putPebbleQuantity(PebbleColor color, int quantity) {
        PebbleCollection newPebbleCollection = this.getPebbleCollectionCopy();
        newPebbleCollection.pebbles.put(color, quantity);
        return newPebbleCollection;
    }

    // adds/removes the given amount of pebbles of the given color to this collection
    // throws an error if this makes the pebble quantity go negative
    public PebbleCollection updatePebbleQuantity(PebbleColor color, int quantity) throws IllegalArgumentException {
        PebbleCollection newPebbleCollection = this.getPebbleCollectionCopy();
        int newPebbleQuantity = this.pebbles.getOrDefault(color,0) + quantity;
        if (newPebbleQuantity < 0) {
            throw new IllegalArgumentException(color + "Pebble Quantity in Pebble Collection went negative.");
        }
        newPebbleCollection.pebbles.put(color, newPebbleQuantity);
        return newPebbleCollection;
    }

    // adds the given pebble collection to this pebble collection and returns a new copy
    public PebbleCollection addPebbles(PebbleCollection toAddPebbles) {
        PebbleCollection newPebbleCollection = this.getPebbleCollectionCopy();
        for (PebbleColor color : PebbleColor.values()) {
            int newCount = newPebbleCollection.pebbles.getOrDefault(color,0)
                    + toAddPebbles.pebbles.getOrDefault(color,0);
            newPebbleCollection.pebbles.put(color, newCount);
        }
        return newPebbleCollection;
    }

    // removes the given pebbles from this pebble collection and returns a new copy
    // throws exception if the pebbles to be removed from this collection is greater than this collection
    public PebbleCollection removePebbles(PebbleCollection toRemovePebbles) throws IllegalArgumentException {
        PebbleCollection newPebbleCollection = this.getPebbleCollectionCopy();
        for (PebbleColor color : PebbleColor.values()) {
            int remainingPebbles = this.pebbles.get(color) - toRemovePebbles.pebbles.get(color);

            if (remainingPebbles < 0) {
                throw new IllegalArgumentException("Illegal request: too many pebbles removed.");
            }
            newPebbleCollection.pebbles.put(color, remainingPebbles);
        }
        return newPebbleCollection;
    }

    /**
     * Given a HashMap of pebbles, checks if this collection is a subset of the other collection, namely for each color,
     * this collection has less than the quantity of the color in the other collection.
     */
    public boolean isSubset(PebbleCollection otherCollection) {
        for (PebbleColor color : PebbleColor.values()) {
            if (this.pebbles.get(color) > otherCollection.pebbles.get(color)) {
                return false;
            }
        }
        return true;
    }

    // is this pebble collection less than the given pebble collection?
    // less than meaning smaller in size, or if equal in size, smaller lexicographically
    public boolean lessThanGivenPebbleCollection(PebbleCollection other) {
        if (this.getTotalNumberOfPebbles() == other.getTotalNumberOfPebbles()) {
            return this.toString().compareTo(other.toString()) < 0;
        } else {
            return this.getTotalNumberOfPebbles() < other.getTotalNumberOfPebbles();
        }
    }

    // converts this pebble collection into a sorted color string where each char represents 1 color
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (PebbleColor color : this.pebbles.keySet()) {
            int count = this.pebbles.get(color);
            sb.append(String.valueOf(color.toChar()).repeat(count));
        }
        char[] charArray = sb.toString().toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    // is this pebble collection equal to the given object?
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PebbleCollection otherPebbles)) {
            return false;
        }
        return this.pebbles.equals(otherPebbles.pebbles);
    }
}
