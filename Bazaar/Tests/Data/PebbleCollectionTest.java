package Tests.Data;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import Common.Data.PebbleColor;
import Common.Data.PebbleCollection;
import Common.Pair;

import static org.junit.jupiter.api.Assertions.*;

class PebbleCollectionTest {

    @Test
    void pebbleCollectionPebblesConstructor() {
        Map<PebbleColor, Integer> initial = new HashMap<>();
        initial.put(PebbleColor.RED, 10);
        initial.put(PebbleColor.BLUE, 5);
        PebbleCollection pebbleCollection = new PebbleCollection(initial);
        assertEquals(10, pebbleCollection.getPebbleCountForColor(PebbleColor.RED));
        assertEquals(0, pebbleCollection.getPebbleCountForColor(PebbleColor.WHITE));
        assertEquals(5, pebbleCollection.getPebbleCountForColor(PebbleColor.BLUE));
        assertEquals(0, pebbleCollection.getPebbleCountForColor(PebbleColor.GREEN));
        assertEquals(0, pebbleCollection.getPebbleCountForColor(PebbleColor.YELLOW));
    }

    @Test
    void testTakeRandomPebble() {
        PebbleCollection pebbleCollection = new PebbleCollection(1);
        Pair<PebbleColor, PebbleCollection> colorAndPebbleCollection = pebbleCollection.takeRandomPebble();
        PebbleColor color = colorAndPebbleCollection.first;
        PebbleCollection newPebbleCollection = colorAndPebbleCollection.second;
        assertEquals(newPebbleCollection.getTotalNumberOfPebbles(), 4);
    }

    @Test
    void testDrawDeterministicPebble() {
        PebbleCollection pebbleCollection = new PebbleCollection(1);
        Pair<PebbleColor, PebbleCollection> colorAndPebbleCollection = pebbleCollection.drawDeterministicPebble();
        PebbleColor color = colorAndPebbleCollection.first;
        PebbleCollection newPebbleCollection = colorAndPebbleCollection.second;
        assertEquals(PebbleColor.RED, color);
        PebbleCollection expectedPebbleCollection = new PebbleCollection(1);
        expectedPebbleCollection = expectedPebbleCollection.putPebbleQuantity(PebbleColor.RED, 0);
        assertEquals(expectedPebbleCollection, newPebbleCollection);
    }

    @Test
    void testGetPebblesMapCopy() {
        Map<PebbleColor, Integer> initial = new HashMap<>();
        initial.put(PebbleColor.RED, 10);
        initial.put(PebbleColor.BLUE, 5);
        PebbleCollection pc = new PebbleCollection(initial);
        Map<PebbleColor, Integer> other = pc.getPebblesMapCopy();
        for (PebbleColor color : initial.keySet()) {
            assertEquals(initial.get(color), other.get(color));
        }
    }

    @Test
    void testGetPebbleCollectionCopy() {
        PebbleCollection pebbles = new PebbleCollection();
        pebbles = pebbles.putPebbleQuantity(PebbleColor.YELLOW, 2);
        assertEquals(pebbles, pebbles.getPebbleCollectionCopy());
    }

    @Test
    void testGetPebblesAsList() {
        PebbleCollection pebbles = new PebbleCollection();
        pebbles = pebbles.putPebbleQuantity(PebbleColor.YELLOW, 1);
        pebbles = pebbles.putPebbleQuantity(PebbleColor.BLUE, 1);
        assertEquals(2, pebbles.getPebblesAsList().size());
        assertTrue(pebbles.getPebblesAsList().contains(PebbleColor.BLUE));
        assertTrue(pebbles.getPebblesAsList().contains(PebbleColor.YELLOW));
    }

    @Test
    void testGetTotalNumberOfPebbles() {
        PebbleCollection pebbles = new PebbleCollection();
        pebbles = pebbles.putPebbleQuantity(PebbleColor.GREEN, 5);
        pebbles = pebbles.putPebbleQuantity(PebbleColor.BLUE, 1);
        assertEquals(pebbles.getTotalNumberOfPebbles(), 6);
    }

    @Test
    void testGetPebbleCountForColor() {
        PebbleCollection pebbles = new PebbleCollection();
        pebbles = pebbles.putPebbleQuantity(PebbleColor.RED, 3);
        pebbles = pebbles.putPebbleQuantity(PebbleColor.WHITE, 1);
        assertEquals(pebbles.getPebbleCountForColor(PebbleColor.RED), 3);
        assertEquals(pebbles.getPebbleCountForColor(PebbleColor.WHITE), 1);
        assertEquals(pebbles.getPebbleCountForColor(PebbleColor.GREEN), 0);
        assertEquals(pebbles.getPebbleCountForColor(PebbleColor.BLUE), 0);
        assertEquals(pebbles.getPebbleCountForColor(PebbleColor.YELLOW), 0);
    }

    @Test
    void testOutOfPebbles() {
        PebbleCollection pebbles = new PebbleCollection();
        assertTrue(pebbles.outOfPebbles());
        pebbles = pebbles.putPebbleQuantity(PebbleColor.YELLOW, 1);
        assertFalse(pebbles.outOfPebbles());
    }

    @Test
    void testPutPebbleQuantity() {
        PebbleCollection pc = new PebbleCollection(new HashMap<>());
        assertEquals(0, pc.getPebblesMapCopy().get(PebbleColor.RED));
        pc = pc.putPebbleQuantity(PebbleColor.RED, 10);
        assertEquals(10, pc.getPebblesMapCopy().get(PebbleColor.RED));
    }

    @Test
    void testUpdatePebbleQuantity() {
        PebbleCollection pc = new PebbleCollection(new HashMap<>());
        assertEquals(0, pc.getPebblesMapCopy().get(PebbleColor.RED));
        pc = pc.updatePebbleQuantity(PebbleColor.RED, 3);
        assertEquals(3, pc.getPebblesMapCopy().get(PebbleColor.RED));
        pc = pc.updatePebbleQuantity(PebbleColor.RED, -1);
        assertEquals(2, pc.getPebblesMapCopy().get(PebbleColor.RED));
    }

    @Test
    void testAddPebbles() {
        HashMap<PebbleColor, Integer> initial = new HashMap<>();
        HashMap<PebbleColor, Integer> toAdd = new HashMap<>();
        toAdd.put(PebbleColor.RED, 3);
        PebbleCollection pc = new PebbleCollection(initial);
        pc = pc.addPebbles(new PebbleCollection(toAdd));
        assertEquals(3, pc.getPebblesMapCopy().get(PebbleColor.RED));
    }

    @Test
    void testRemovePebbles() {
        HashMap<PebbleColor, Integer> initial = new HashMap<>();
        initial.put(PebbleColor.RED, 3);
        HashMap<PebbleColor, Integer> toRemove = new HashMap<>();
        toRemove.put(PebbleColor.RED, 2);
        PebbleCollection pc = new PebbleCollection(initial);
        pc = pc.removePebbles(new PebbleCollection(toRemove));
        assertEquals(pc.getPebblesMapCopy().get(PebbleColor.RED), 1);
    }

    @Test
    void testIsSubset() {
        HashMap<PebbleColor, Integer> initial = new HashMap<>();
        initial.put(PebbleColor.RED, 3);
        initial.put(PebbleColor.BLUE, 2);
        initial.put(PebbleColor.YELLOW, 1);
        PebbleCollection current = new PebbleCollection(initial);
        HashMap<PebbleColor, Integer> otherMap = new HashMap<>();
        PebbleCollection other = new PebbleCollection(otherMap);
        assertFalse(current.isSubset(other));
        assertTrue(other.isSubset(current));
        assertTrue(current.isSubset(current));
    }

    @Test
    void testLessThanGivenPebbleCollection() {
        PebbleCollection smaller = new PebbleCollection();
        smaller = smaller.putPebbleQuantity(PebbleColor.BLUE, 2);
        PebbleCollection greater = new PebbleCollection();
        greater = greater.putPebbleQuantity(PebbleColor.YELLOW, 2);
        assertTrue(smaller.lessThanGivenPebbleCollection(greater));
        assertFalse(greater.lessThanGivenPebbleCollection(smaller));
    }

    @Test
    void testToString() {
        PebbleCollection pebbles = new PebbleCollection();
        pebbles = pebbles.putPebbleQuantity(PebbleColor.WHITE, 2);
        assertEquals(pebbles.toString(), "ww");
        pebbles = pebbles.putPebbleQuantity(PebbleColor.YELLOW, 1);
        assertEquals(pebbles.toString(), "wwy");
    }

    @Test
    void testEquals() {
        PebbleCollection pebbles = new PebbleCollection();
        pebbles = pebbles.putPebbleQuantity(PebbleColor.RED, 1);
        PebbleCollection copy = pebbles.getPebbleCollectionCopy();
        assertEquals(pebbles, copy);
        copy = copy.putPebbleQuantity(PebbleColor.YELLOW, 1);
        assertNotEquals(pebbles, copy);
    }
}