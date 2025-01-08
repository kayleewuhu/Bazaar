package Tests.Data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import Common.Data.DrawDeterministicPebble;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;
import Common.Pair;
import Common.Turn_State;
import Referee.PlayerInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DrawDeterministicPebbleTest {

  @Test
  void testAttemptAction() {
    DrawDeterministicPebble draw = new DrawDeterministicPebble();
    Turn_State turnState = new Turn_State(new PebbleCollection(1), new PlayerInfo(), new ArrayList<>(), new ArrayList<>());
    EquationTable equationTable = new EquationTable(new Random());
    Pair<PebbleCollection, PebbleCollection> newWalletAndBank = draw.attemptAction(turnState, equationTable).get();
    PebbleCollection newWallet = newWalletAndBank.first;
    PebbleCollection newBank = newWalletAndBank.second;
    PebbleCollection expectedWallet = new PebbleCollection(0);
    expectedWallet = expectedWallet.putPebbleQuantity(PebbleColor.RED, 1);
    assertEquals(expectedWallet, newWallet);
    turnState = new Turn_State(newBank, new PlayerInfo(newWallet), new ArrayList<>(), new ArrayList<>());
    newWalletAndBank = draw.attemptAction(turnState, equationTable).get();
    newWallet = newWalletAndBank.first;
    expectedWallet = expectedWallet.putPebbleQuantity(PebbleColor.WHITE, 1);
    assertEquals(expectedWallet, newWallet);
  }

  @Test
  void testEquals() {
    DrawDeterministicPebble d1 = new DrawDeterministicPebble();
    DrawPebbleOrExchanges d2 = new DrawDeterministicPebble();
    assertEquals(d1, d2);
  }
}
