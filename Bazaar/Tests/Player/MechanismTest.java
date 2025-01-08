package Tests.Player;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import Common.Data.BidirectionalEquation;
import Common.Data.Card;
import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;
import Common.Data.UnidirectionalEquation;
import Common.Turn_State;
import Player.MaximizePoints;
import Player.Mechanism;
import Referee.PlayerInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MechanismTest {
  Mechanism player;

  @Test
  public void initialize() {
    this.player = new Mechanism("Points", new MaximizePoints());
  }

  // Tests that name() gets the player's name
  @Test
  public void testName() {
    initialize();
    assertEquals("Points", this.player.name());
  }

  // Tests that setup() set's up the equation table in mechanism and that requestPebbleOrExchanges() produces the right
  // exchanges and requestCards() produces the right cards
  // Turn_State will have:
  //    Bank with red: 3
  //    This Player with yellow: 1, blue: 2
  //    No other player scores
  //    One card with red: 3, blue: 2
  // EquationTable will only contain 1 equation: Y = RRR
  // Output for requestPebbleOrExchanges() should be one exchange to get 3 red, and output for requestCards()
  // should be to buy the one card
  @Test
  public void testSetupAndRequestPebbleOrExchangesAndRequestCards() {
    initialize();
    PebbleCollection ThreeRed = new PebbleCollection(0);
    ThreeRed = ThreeRed.updatePebbleQuantity(PebbleColor.RED, 3);
    PebbleCollection OneYellowTwoBlue = new PebbleCollection(0);
    OneYellowTwoBlue = OneYellowTwoBlue.updatePebbleQuantity(PebbleColor.YELLOW, 1);
    OneYellowTwoBlue = OneYellowTwoBlue.updatePebbleQuantity(PebbleColor.BLUE, 2);
    PebbleCollection ThreeRedTwoBlue = new PebbleCollection(0);
    ThreeRedTwoBlue = ThreeRedTwoBlue.updatePebbleQuantity(PebbleColor.RED, 3);
    ThreeRedTwoBlue = ThreeRedTwoBlue.updatePebbleQuantity(PebbleColor.BLUE, 2);
    PebbleCollection OneYellow = new PebbleCollection(0);
    OneYellow = OneYellow.updatePebbleQuantity(PebbleColor.YELLOW, 1);
    PlayerInfo activePlayer = new PlayerInfo(OneYellowTwoBlue);
    Card card = new Card(ThreeRedTwoBlue, true);
    Turn_State turnState = new Turn_State(ThreeRed, activePlayer, new ArrayList<>(), new ArrayList<>(List.of(card)));
    BidirectionalEquation equation = new BidirectionalEquation(OneYellow, ThreeRed);
    List<BidirectionalEquation> equations = new ArrayList<>(List.of(equation));
    EquationTable equationTable = new EquationTable(equations);
    this.player.setup(equationTable);
    DrawPebbleOrExchanges firstAction = this.player.requestPebbleOrExchanges(turnState);
    UnidirectionalEquation exchange = new UnidirectionalEquation(OneYellow, ThreeRed);
    Exchanges expectedExchanges = new Exchanges(new ArrayList<>(List.of(exchange)));
    assertEquals(expectedExchanges, firstAction);
    PlayerInfo updatedActivePlayer = activePlayer.setWalletTo(ThreeRedTwoBlue);
    Turn_State updatedTurnState = new Turn_State(OneYellow, updatedActivePlayer, new ArrayList<>(), new ArrayList<>(List.of(card)));
    CardPurchases secondAction = this.player.requestCards(updatedTurnState);
    CardPurchases expectedCardPurchases = new CardPurchases(new ArrayList<>(List.of(card)));
    assertEquals(expectedCardPurchases, secondAction);
  }
}
