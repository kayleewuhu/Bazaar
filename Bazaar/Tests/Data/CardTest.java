package Tests.Data;
import org.junit.jupiter.api.Test;

import java.util.Random;

import Common.Data.Card;
import Common.Data.PebbleColor;
import Common.Data.PebbleCollection;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testCardRandomConstructor() {
        Random rand = new Random(0);
        Card c = new Card(rand);
        assertTrue(c.hasSmiley());
        assertEquals(1, c.getCardPebblesCopy().getPebbleCountForColor(PebbleColor.RED));
        assertEquals(0, c.getCardPebblesCopy().getPebbleCountForColor(PebbleColor.WHITE));
        assertEquals(0, c.getCardPebblesCopy().getPebbleCountForColor(PebbleColor.BLUE));
        assertEquals(1, c.getCardPebblesCopy().getPebbleCountForColor(PebbleColor.GREEN));
        assertEquals(1, c.getCardPebblesCopy().getPebbleCountForColor(PebbleColor.YELLOW));
    }

    @Test
    void testCardConstructor() {
        PebbleCollection pebbleCollection = new PebbleCollection(3);
        Card c = new Card(pebbleCollection, false);
        assertFalse(c.hasSmiley());
        assertEquals(3, c.getCardPebblesCopy().getPebbleCountForColor(PebbleColor.RED));
        assertEquals(3, c.getCardPebblesCopy().getPebbleCountForColor(PebbleColor.WHITE));
        assertEquals(3, c.getCardPebblesCopy().getPebbleCountForColor(PebbleColor.BLUE));
        assertEquals(3, c.getCardPebblesCopy().getPebbleCountForColor(PebbleColor.GREEN));
        assertEquals(3, c.getCardPebblesCopy().getPebbleCountForColor(PebbleColor.YELLOW));
    }

    @Test
    void testHasSmiley() {
        Card noSmile = new Card(new PebbleCollection(0), false);
        Card withSmile = new Card(new PebbleCollection(0), true);
        assertFalse(noSmile.hasSmiley());
        assertTrue(withSmile.hasSmiley());
    }

    @Test
    void testGetCardPebblesCopy() {
        PebbleCollection pebbleCollection = new PebbleCollection(3);
        Card c = new Card(pebbleCollection, true);
        PebbleCollection copy = c.getCardPebblesCopy();
        assertNotSame(pebbleCollection, copy);
        for (PebbleColor color : PebbleColor.values()) {
            assertEquals(pebbleCollection.getPebbleCountForColor(color), copy.getPebbleCountForColor(color));
        }
    }

    @Test
    void testGetCardCopy() {
        PebbleCollection pebbleCollection = new PebbleCollection(3);
        Card c = new Card(pebbleCollection, true);
        Card copy = c.getCardCopy();
        assertNotSame(c, copy);
        for (PebbleColor color : PebbleColor.values()) {
            assertEquals(c.getCardPebblesCopy().getPebbleCountForColor(color), copy.getCardPebblesCopy().getPebbleCountForColor(color));
        }
        assertEquals(c.hasSmiley(), copy.hasSmiley());
    }

    @Test
    void testLessThanGivenCard() {
        PebbleCollection pebbles = new PebbleCollection(0);
        pebbles = pebbles.putPebbleQuantity(PebbleColor.RED, 5);
        Card noFace = new Card(pebbles, false);
        Card face = new Card(pebbles, true);
        assertTrue(noFace.lessThanGivenCard(face));
        assertFalse(face.lessThanGivenCard(noFace));
    }

    @Test
    void testEquals() {
        PebbleCollection pebbles = new PebbleCollection(0);
        pebbles = pebbles.putPebbleQuantity(PebbleColor.RED, 5);
        Card noFace = new Card(pebbles, false);
        Card face = new Card(pebbles, true);
        assertNotEquals(noFace, face);
        assertEquals(face, face);
    }
}