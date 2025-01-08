package Tests.Data;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import Common.Data.BidirectionalEquation;
import Common.Data.PebbleColor;
import Common.Data.PebbleCollection;
import Common.Data.UnidirectionalEquation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BidirectionalEquationTest {
    BidirectionalEquation equation;

    void initialize() {
        PebbleCollection leftSide = new PebbleCollection(0);
        leftSide = leftSide.putPebbleQuantity(PebbleColor.RED, 1);
        PebbleCollection rightSide = new PebbleCollection(1);
        rightSide = rightSide.putPebbleQuantity(PebbleColor.RED, 0);
        this.equation = new BidirectionalEquation(leftSide, rightSide);
    }

    @Test
    void testBidirectionalEquationConstructor() {
        Random rand = new Random();
        BidirectionalEquation eq = new BidirectionalEquation(rand);
        PebbleCollection leftSide = eq.getLeftSideCopy();
        PebbleCollection rightSide = eq.getRightSideCopy();
        int leftSideCount = leftSide.getPebblesMapCopy().values().stream().mapToInt(Integer::intValue).sum();

        assertTrue(leftSideCount >= 1 && leftSideCount <= 4);
        int rightSideCount = rightSide.getPebblesMapCopy().values().stream().mapToInt(Integer::intValue).sum();
        assertTrue(rightSideCount >= 1 && rightSideCount <= 4);
        Map<PebbleColor, Integer> leftMap = leftSide.getPebblesMapCopy();
        Map<PebbleColor, Integer> rightMap = rightSide.getPebblesMapCopy();
        for (PebbleColor p : leftMap.keySet()) {
            if (leftMap.get(p) > 0) {
                assertEquals(0, rightMap.get(p));
            }
        }

        for (PebbleColor p : rightMap.keySet()) {
            if (rightMap.get(p) > 0) {
                assertEquals(0, leftMap.get(p));
            }
        }
    }

    @Test
    void testGetBidirectionalEquationCopy() {
        initialize();
        assertEquals(this.equation.getBidirectionalEquationCopy(), this.equation);
    }

    @Test
    void testGetLeftSideCopy() {
        initialize();
        PebbleCollection left = new PebbleCollection(0);
        left = left.putPebbleQuantity(PebbleColor.RED, 1);
        assertEquals(this.equation.getLeftSideCopy(), left);
    }

    @Test
    void testGetRightSideCopy() {
        initialize();
        PebbleCollection expected = new PebbleCollection(1);
        expected = expected.putPebbleQuantity(PebbleColor.RED, 0);
        assertEquals(expected, this.equation.getRightSideCopy());
    }

    @Test
    void testGetSymbol() {
        initialize();
        assertEquals("↔", this.equation.getSymbol());
    }

    @Test
    void testConvertToUnidirectionalEquationTest() {
        initialize();
        PebbleCollection OneRed = new PebbleCollection(0);
        OneRed = OneRed.updatePebbleQuantity(PebbleColor.RED, 1);
        PebbleCollection OneEverythingButZeroRed = new PebbleCollection(1);
        OneEverythingButZeroRed = OneEverythingButZeroRed.putPebbleQuantity(PebbleColor.RED, 0);
        UnidirectionalEquation equation1 = new UnidirectionalEquation(OneRed, OneEverythingButZeroRed);
        UnidirectionalEquation equation2 = new UnidirectionalEquation(OneEverythingButZeroRed, OneRed);
        List<UnidirectionalEquation> expectedEquations = new ArrayList<>();
        expectedEquations.add(equation1);
        expectedEquations.add(equation2);
        assertEquals(expectedEquations, this.equation.convertToUnidirectionalEquation());
    }

    @Test
    void testEquals() {
        initialize();
        PebbleCollection OneRed = new PebbleCollection(0);
        OneRed = OneRed.updatePebbleQuantity(PebbleColor.RED, 1);
        PebbleCollection OneEverythingButZeroRed = new PebbleCollection(1);
        OneEverythingButZeroRed = OneEverythingButZeroRed.putPebbleQuantity(PebbleColor.RED, 0);
        BidirectionalEquation reverse = new BidirectionalEquation(OneEverythingButZeroRed, OneRed);
        assertEquals(this.equation, reverse);
        assertNotEquals("Not Equation", this.equation);
    }
}