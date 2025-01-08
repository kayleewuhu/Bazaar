package Tests.Data;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Common.Data.PebbleColor;
import Common.Data.PebbleCollection;
import Common.Data.UnidirectionalEquation;
import Common.Pair;

import static org.junit.jupiter.api.Assertions.*;

// Tests all Unidirectional equation public methods.
class UnidirectionalEquationTest {
  PebbleCollection pebblesIn;
  PebbleCollection pebblesOut;
  UnidirectionalEquation rule;
  PebbleCollection playerWallet1;
  PebbleCollection bank1;
  PebbleCollection playerWallet2;
  PebbleCollection bank2;

  void initialize() {
    this.pebblesIn = new PebbleCollection(4, new Random(1));
    this.pebblesOut = new PebbleCollection(2, new Random(2));
    this.rule = new UnidirectionalEquation(this.pebblesIn, this.pebblesOut);

    Map<PebbleColor, Integer> wallet1Map = new HashMap<>();
    wallet1Map.put(PebbleColor.BLUE, 2);
    wallet1Map.put(PebbleColor.RED, 1);
    wallet1Map.put(PebbleColor.GREEN, 2);
    this.playerWallet1 = new PebbleCollection(wallet1Map);

    Map<PebbleColor, Integer> bank1Map = new HashMap<>();
    bank1Map.put(PebbleColor.BLUE, 2);
    bank1Map.put(PebbleColor.GREEN, 2);
    this.bank1 = new PebbleCollection(bank1Map);

    Map<PebbleColor, Integer> wallet2Map = new HashMap<>();
    wallet2Map.put(PebbleColor.BLUE, 2);
    wallet2Map.put(PebbleColor.RED, 1);
    wallet2Map.put(PebbleColor.GREEN, 1);
    this.playerWallet2 = new PebbleCollection(wallet2Map);

    Map<PebbleColor, Integer> bank2Map = new HashMap<>();
    bank2Map.put(PebbleColor.BLUE, 2);
    this.bank2 = new PebbleCollection(bank2Map);
  }

  @Test
  void testGetInputSideCopy() {
    this.initialize();
    assertEquals(this.rule.getInputSideCopy().getPebblesAsList(), this.pebblesIn.getPebblesAsList());
  }

  @Test
  void testGetOutputSideCopy() {
    this.initialize();
    assertEquals(this.rule.getOutputSideCopy().getPebblesAsList(), this.pebblesOut.getPebblesAsList());
  }

  @Test
  void testGetSymbol() {
    this.initialize();
    assertEquals(this.rule.getSymbol(), "→");
  }

  @Test
  void testCanExchange() {
    this.initialize();
    assertTrue(this.rule.canExchange(this.playerWallet1, this.bank1));
    assertFalse(this.rule.canExchange(this.playerWallet2, this.bank1));
    assertFalse(this.rule.canExchange(this.playerWallet1, this.bank2));
  }

  @Test
  void testDoExchange() {
    this.initialize();
    Pair<PebbleCollection, PebbleCollection> pebbles = this.rule.doExchange(this.playerWallet1, this.bank1);
    PebbleCollection playerWalletAfterExchange = this.playerWallet1.getPebbleCollectionCopy();
    playerWalletAfterExchange = playerWalletAfterExchange.removePebbles(this.rule.getInputSideCopy());
    playerWalletAfterExchange = playerWalletAfterExchange.addPebbles(this.rule.getOutputSideCopy());
    assertEquals(pebbles.first, playerWalletAfterExchange);

    PebbleCollection bankAfterExchange = this.bank1.getPebbleCollectionCopy();
    bankAfterExchange = bankAfterExchange.removePebbles(this.rule.getOutputSideCopy());
    bankAfterExchange = bankAfterExchange.addPebbles(this.rule.getInputSideCopy());
    assertEquals(pebbles.second, bankAfterExchange);
  }
}