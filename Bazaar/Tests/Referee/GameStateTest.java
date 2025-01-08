package Tests.Referee;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Deque;

import Common.Data.BidirectionalEquation;
import Common.Data.Card;
import Common.Data.CardCollection;
import Common.Data.CardPurchases;
import Common.Data.DrawPebble;
import Common.Data.EquationTable;
import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;
import Referee.Game_State;
import Referee.PlayerInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

// This class tests game state functions.
public class GameStateTest {
  @Test
  void testPerformDrawPebbleOrExchanges() {
    PebbleCollection in1 = new PebbleCollection();
    in1 = in1.updatePebbleQuantity(PebbleColor.BLUE, 2);
    PebbleCollection out1 = new PebbleCollection();
    out1 = out1.updatePebbleQuantity(PebbleColor.WHITE, 1);
    BidirectionalEquation eq1 = new BidirectionalEquation(in1, out1);
    EquationTable equations = new EquationTable(new ArrayList<>(Arrays.asList(eq1)));

    PebbleCollection bank1 = new PebbleCollection();
    bank1 = bank1.updatePebbleQuantity(PebbleColor.RED, 1);
    CardCollection cards = new CardCollection(new ArrayList<>(), new ArrayList<>());
    PlayerInfo player = new PlayerInfo();
    Deque<PlayerInfo> playerInfos = new ArrayDeque<>(Arrays.asList(player));

    Game_State gs1 = new Game_State(bank1, cards, playerInfos);
    Game_State gs1Out = gs1.performDrawPebbleOrExchanges(new DrawPebble(), equations);
    Game_State gs1Expected = new Game_State(new PebbleCollection(), cards, playerInfos);
    assertEquals(gs1Out, gs1Expected);
  }


  // Initial game state is an empty bank, 1 player with 3 Reds and 2 Blues and 0 points, and 1 visible card with face that costs 3 Reds and 2 Blues.
  // Expected output game state is a bank with 3 Reds and 2 Blues, 1 player with empty wallet and 8 points, and no cards.
  @Test
  public void testPerformCardPurchases() {
    PebbleCollection ThreeRedTwoBlue = new PebbleCollection(0);
    ThreeRedTwoBlue = ThreeRedTwoBlue.updatePebbleQuantity(PebbleColor.RED, 3);
    ThreeRedTwoBlue = ThreeRedTwoBlue.updatePebbleQuantity(PebbleColor.BLUE, 2);
    Card card = new Card(ThreeRedTwoBlue, true);
    CardPurchases action = new CardPurchases(new ArrayList<>(List.of(card)));
    CardCollection cards = new CardCollection(new ArrayList<>(List.of(card)), new ArrayList<>());
    PlayerInfo activePlayer = new PlayerInfo(ThreeRedTwoBlue);
    Game_State gameState = new Game_State(new PebbleCollection(0), cards, new ArrayDeque<>(List.of(activePlayer)));
    Game_State outputGameState = gameState.performCardPurchases(action);
    CardCollection noCards = new CardCollection(new ArrayList<>(), new ArrayList<>());
    Game_State expectedGameState = new Game_State(ThreeRedTwoBlue, noCards, new ArrayDeque<>(List.of(activePlayer)));
    assertEquals(expectedGameState, outputGameState);
  }
}
