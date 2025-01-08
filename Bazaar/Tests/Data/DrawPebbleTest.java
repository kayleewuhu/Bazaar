package Tests.Data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import Common.Data.DrawPebble;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Data.PebbleCollection;
import Common.Pair;
import Common.Turn_State;
import Referee.PlayerInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DrawPebbleTest {

  @Test
  void testAttemptAction() {
    DrawPebble draw = new DrawPebble();
    Turn_State turnState = new Turn_State(new PebbleCollection(1), new PlayerInfo(), new ArrayList<>(), new ArrayList<>());
    EquationTable equationTable = new EquationTable(new Random());
    Pair<PebbleCollection, PebbleCollection> newWalletAndBank = draw.attemptAction(turnState, equationTable).get();
    PebbleCollection newWallet = newWalletAndBank.first;
    PebbleCollection newBank = newWalletAndBank.second;
    assertEquals(1, newWallet.getTotalNumberOfPebbles());
    turnState = new Turn_State(newBank, new PlayerInfo(newWallet), new ArrayList<>(), new ArrayList<>());
    newWalletAndBank = draw.attemptAction(turnState, equationTable).get();
    newWallet = newWalletAndBank.first;
    assertEquals(2, newWallet.getTotalNumberOfPebbles());
  }

  @Test
  void testEquals() {
    DrawPebble d1 = new DrawPebble();
    DrawPebbleOrExchanges d2 = new DrawPebble();
    assertEquals(d1, d2);
  }
}
