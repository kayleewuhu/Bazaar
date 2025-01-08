package Tests.Data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Common.Data.BidirectionalEquation;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;
import Common.Data.UnidirectionalEquation;
import Common.Pair;
import Common.Turn_State;
import Referee.PlayerInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangesTest {
  BidirectionalEquation equation;
  List<UnidirectionalEquation> rules;
  Exchanges exchanges;

  private void initialize() {
    PebbleCollection input = new PebbleCollection();
    input = input.putPebbleQuantity(PebbleColor.BLUE, 2);
    PebbleCollection output = new PebbleCollection();
    output = output.putPebbleQuantity(PebbleColor.RED, 3);
    this.equation = new BidirectionalEquation(input, output);
    UnidirectionalEquation exchange = new UnidirectionalEquation(input, output);
    this.rules = new ArrayList<>(Arrays.asList(exchange, exchange));
    this.exchanges = new Exchanges(this.rules);
  }

  @Test
  void testGetSequenceOfExchangesCopy() {
    this.initialize();
    assertEquals(this.rules, this.exchanges.getSequenceOfExchangesCopy());
  }

  @Test
  void testGetExchangesCopy() {
    this.initialize();
    assertEquals(this.exchanges, this.exchanges.getExchangesCopy());
  }

  @Test
  void testTotalTrades() {
    this.initialize();
    assertEquals(this.exchanges.totalTrades(), 2);
  }

  @Test
  void testAttemptAction() {
    this.initialize();
    Turn_State turnState = new Turn_State(new PebbleCollection(6), new PlayerInfo(new PebbleCollection(6)), new ArrayList<>(), new ArrayList<>());
    EquationTable equationTable = new EquationTable(new ArrayList<>(Arrays.asList(this.equation)));
    Pair<PebbleCollection, PebbleCollection> newWalletAndBank = this.exchanges.attemptAction(turnState, equationTable).get();
    PebbleCollection newWallet = newWalletAndBank.first;
    PebbleCollection newBank = newWalletAndBank.second;
    assertEquals("bbggggggrrrrrrrrrrrrwwwwwwyyyyyy", newWallet.toString());
    assertEquals("bbbbbbbbbbggggggwwwwwwyyyyyy", newBank.toString());
  }

  @Test
  void testEquals() {
    this.initialize();
    PebbleCollection input = new PebbleCollection();
    input = input.putPebbleQuantity(PebbleColor.BLUE, 2);
    PebbleCollection output = new PebbleCollection();
    output = output.putPebbleQuantity(PebbleColor.RED, 3);
    UnidirectionalEquation exchange = new UnidirectionalEquation(input, output);
    Exchanges other = new Exchanges(new ArrayList<>(Arrays.asList(exchange, exchange)));
    assertEquals(other, this.exchanges);
  }
}
