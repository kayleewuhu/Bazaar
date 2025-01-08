package Tests.Data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Common.Data.BidirectionalEquation;
import Common.Data.EquationTable;
import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;
import Common.Data.UnidirectionalEquation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EquationTableTest {
  PebbleCollection oneRed;
  PebbleCollection oneBlue;
  EquationTable equationTable;

  private void initialize() {
    this.oneRed = new PebbleCollection(0);
    oneRed = oneRed.putPebbleQuantity(PebbleColor.RED, 1);
    this.oneBlue = new PebbleCollection(0);
    oneBlue = oneBlue.putPebbleQuantity(PebbleColor.BLUE, 1);
    BidirectionalEquation equationOneRedOneBlue = new BidirectionalEquation(oneRed, oneBlue);
    this.equationTable = new EquationTable(new ArrayList<>(List.of(equationOneRedOneBlue)));
  }
  
  @Test
  void testEquationTableConstructor() {
    initialize();
    PebbleCollection twoBlueTwoGreen = new PebbleCollection(0);
    twoBlueTwoGreen = twoBlueTwoGreen.putPebbleQuantity(PebbleColor.BLUE, 2);
    twoBlueTwoGreen = twoBlueTwoGreen.putPebbleQuantity(PebbleColor.GREEN, 2);
    BidirectionalEquation equation = new BidirectionalEquation(oneRed, twoBlueTwoGreen);
    EquationTable equationTable = new EquationTable(List.of(equation));
    assertEquals(new ArrayList<>(List.of(equation)), equationTable.getAllEquationsCopy());
  }

  @Test
  void testGetAllEquationsCopy() {
    initialize();
    BidirectionalEquation bidirectionalEquation = new BidirectionalEquation(oneBlue, oneRed);
    assertEquals(new ArrayList<>(List.of(bidirectionalEquation)), this.equationTable.getAllEquationsCopy());
  }

  @Test
  void testFilter() {
    initialize();
    PebbleCollection wallet = new PebbleCollection(0);
    wallet = wallet.putPebbleQuantity(PebbleColor.RED, 1);
    PebbleCollection bank = new PebbleCollection(0);
    bank = bank.putPebbleQuantity(PebbleColor.BLUE, 1);
    UnidirectionalEquation exchange = new UnidirectionalEquation(oneRed, oneBlue);
    assertEquals(new ArrayList<>(List.of(exchange)), this.equationTable.filter(wallet, bank));
  }

  @Test
  void testContains() {
    initialize();
    UnidirectionalEquation exists = new UnidirectionalEquation(oneRed, oneBlue);
    UnidirectionalEquation doesntExist = new UnidirectionalEquation(oneRed, oneRed);
    assertTrue(equationTable.contains(exists));
    assertFalse(equationTable.contains(doesntExist));
  }
}
