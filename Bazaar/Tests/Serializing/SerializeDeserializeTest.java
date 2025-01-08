package Tests.Serializing;

import Common.Data.*;
import Common.Json.MethodDeserializer;
import Common.Json.MethodSerializer;
import Common.Json.ObjectJsonDeserializer;
import Common.Json.ObjectJsonSerializer;
import Common.Pair;
import Common.Turn_State;
import Player.*;
import Referee.Game_State;
import Referee.PlayerInfo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializeDeserializeTest {
    Gson gson = new Gson();
    PebbleCollection pebbles  = new PebbleCollection();
    PebbleCollection pebbles1 = new PebbleCollection();
    PebbleCollection pebbles2 = new PebbleCollection();
    BidirectionalEquation bi;
    UnidirectionalEquation uni = new UnidirectionalEquation(pebbles1, pebbles2);
    PebbleCollection pebbles3 = new PebbleCollection();
    PebbleCollection pebbles4 = new PebbleCollection();
    BidirectionalEquation bi1;
    List<BidirectionalEquation> eqs = new ArrayList<>();
    EquationTable eqTable;

    PebbleCollection cost1 = new PebbleCollection();
    PebbleCollection cost2 = new PebbleCollection();
    PebbleCollection cost3 = new PebbleCollection();
    PebbleCollection cost4 = new PebbleCollection();
    PebbleCollection cost5 = new PebbleCollection();
    Card card1;
    Card card2;
    Card card3;
    Card card4;
    Card card5;
    List<Card> cards = new ArrayList<>();
    List<Card> cards1 = new ArrayList<>();
    List<Card> cards2 = new ArrayList<>();
    CardCollection visibles;


    PlayerInfo player1;
    PlayerAPI playerAPI1;
    PlayerInfo player2;
    PlayerAPI playerAPI2;
    List<PlayerInfo> playerInfos = new ArrayList<>();
    Deque<PlayerInfo> players = new ArrayDeque<>();

    List<Integer> scores = new ArrayList<>(Arrays.asList(1, 2, 3));

    Turn_State ts;

    CardPurchases purchases;

    Exchanges exchanges;

    Game_State gs;

    public void setup() {
        pebbles = pebbles.putPebbleQuantity(PebbleColor.RED, 3);
        pebbles = pebbles.putPebbleQuantity(PebbleColor.BLUE, 6);
        pebbles = pebbles.putPebbleQuantity(PebbleColor.GREEN, 1);

        pebbles1 = pebbles1.putPebbleQuantity(PebbleColor.RED, 1);
        pebbles1 = pebbles1.putPebbleQuantity(PebbleColor.WHITE, 2);
        pebbles1 = pebbles1.putPebbleQuantity(PebbleColor.GREEN, 1);

        pebbles2 = pebbles2.putPebbleQuantity(PebbleColor.BLUE, 3);

        pebbles3 = pebbles3.putPebbleQuantity(PebbleColor.WHITE, 1);
        pebbles4 = pebbles4.putPebbleQuantity(PebbleColor.YELLOW, 1);
        bi1 = new BidirectionalEquation(pebbles3, pebbles4);
        bi = new BidirectionalEquation(pebbles1, pebbles2);
        eqs.add(bi);
        eqs.add(bi1);
        eqTable = new EquationTable(eqs);

        cost1 = cost1.putPebbleQuantity(PebbleColor.RED, 5);
        cost2 = cost2.putPebbleQuantity(PebbleColor.YELLOW, 5);
        cost3 = cost3.putPebbleQuantity(PebbleColor.WHITE, 5);
        cost4 = cost4.putPebbleQuantity(PebbleColor.BLUE, 5);
        cost5 = cost5.putPebbleQuantity(PebbleColor.GREEN, 5);
        card1 = new Card(cost1, true);
        card2 = new Card(cost2, false);
        card3 = new Card(cost3, true);
        card4 = new Card(cost4, false);
        card5 = new Card(cost5, true);

        cards.add(card1);
        cards.add(card2);
        cards1.add(card3);
        cards1.add(card4);
        cards2.add(card5);
        visibles = new CardCollection(cards2, new ArrayList<>());

        playerAPI1 = new Mechanism("p1", new DoNothing());
        player1 = new PlayerInfo(pebbles, 10, "p1", playerAPI1, cards);

        playerAPI2 = new Mechanism("p2", new DoNothing());
        player2 = new PlayerInfo(pebbles1, 5, "p2", playerAPI2, cards1);

        playerInfos.add(player1);
        playerInfos.add(player2);

        players.add(player1);
        players.add(player2);

        ts = new Turn_State(pebbles1, player1, scores, cards);
        purchases = new CardPurchases(cards);

        exchanges = new Exchanges(new ArrayList<>(Arrays.asList(uni)));

        gs = new Game_State(pebbles3, visibles, players);
    }

    @Test
    void testPebble() {
        PebbleColor blue = PebbleColor.BLUE;
        assertEquals(blue, ObjectJsonDeserializer.deserializePebble(ObjectJsonSerializer.serializePebble(blue)));
    }

    @Test
    void testPebbleCollection() {
        setup();
        assertEquals(pebbles, ObjectJsonDeserializer.deserializePebbleCollection(ObjectJsonSerializer.serializePebbleCollection(pebbles)));
    }

    @Test
    void testBidirectionalEquation() {
        setup();
        assertEquals(bi, ObjectJsonDeserializer.deserializeBidirectionalEquation(ObjectJsonSerializer.serializeBidirectionalEquation(bi)));
    }

    @Test
    void testUnidirectionalEquation() {
        setup();
        assertEquals(uni, ObjectJsonDeserializer.deserializeUnidirectionalEquation(ObjectJsonSerializer.serializeUnidirectionalEquation(uni)));
    }

    @Test
    void testEquationTable() {
        setup();
        assertEquals(eqTable, ObjectJsonDeserializer.deserializeEquationTable(ObjectJsonSerializer.serializeEquationTable(eqTable)));
    }

    @Test
    void testCard() {
        setup();
        assertEquals(card1, ObjectJsonDeserializer.deserializeCard(ObjectJsonSerializer.serializeCard(card1)));
    }


    @Test
    void testCards() {
        setup();
        assertEquals(cards, ObjectJsonDeserializer.deserializeCards(ObjectJsonSerializer.serializeCards(cards)));
    }


    @Test
    void testPlayerInfo() {
        setup();
        assertEquals(player1, ObjectJsonDeserializer.deserializePlayerInfo(ObjectJsonSerializer.serializePlayerInfo(player1), "p1", playerAPI1));
    }

    @Test
    void testScores() {
        setup();
        assertEquals(scores, ObjectJsonDeserializer.deserializeScores(ObjectJsonSerializer.serializeScores(scores)));
    }


    @Test
    void testTurnState() {
        setup();
        assertEquals(ts, ObjectJsonDeserializer.deserializeTurnState(ObjectJsonSerializer.serializeTurnState(ts), "p1", playerAPI1));
    }

    @Test
    void testCardPurchases() {
        setup();
        assertEquals(purchases, ObjectJsonDeserializer.deserializeCardPurchases(ObjectJsonSerializer.serializeCardPurchases(purchases)));
    }

    @Test
    void testExchanges() {
        setup();
        assertEquals(exchanges, ObjectJsonDeserializer.deserializeExchanges(ObjectJsonSerializer.serializeExchanges(exchanges)));
    }

    @Test
    void testPlayerInfos() {
        setup();
        assertEquals(playerInfos, ObjectJsonDeserializer.deserializePlayerInfos(ObjectJsonSerializer.serializePlayerInfos(playerInfos)));
    }

    @Test
    void testGameState() {
        setup();
        assertEquals(players, ObjectJsonDeserializer.deserializeGameState(ObjectJsonSerializer.serializeGameState(gs)));
    }
    
    @Test
    void testSetUp() {
        setup();
        Pair<String, JsonElement> method = MethodDeserializer.methodDeserialize(MethodSerializer.setupSerialize(eqTable).toString());
        assertEquals("\"setup\"", method.first);
        assertEquals(ObjectJsonSerializer.serializeEquationTable(eqTable), method.second);
    }

    @Test
    void testPebbleTrades() {
        setup();
        Pair<String, JsonElement> method = MethodDeserializer.methodDeserialize(MethodSerializer.requestPebbleTradesSerialize(ts).toString());
        assertEquals("\"request-pebble-or-trades\"", method.first);
        assertEquals(ObjectJsonSerializer.serializeTurnState(ts), method.second);
    }

    @Test
    void testActor() {
        String actor = "[\"Bob\", \"purchase-points\"]";
        String actor1 = "[\"Bob\", \"purchase-points\", \"win\"]";
        String actor2 = "[\"Bob\", \"purchase-points\", \"a cheat\", \"buy-unavailable-card\"]";
        String actor3 = "[\"Bob\", \"purchase-points\", \"request-cards\", 4]";

        assertEquals(new Mechanism("Bob", new MaximizeCards()),
                ObjectJsonDeserializer.deserializeActor(ObjectJsonDeserializer.jsonStringToJsonElement(actor)));
        assertEquals(new ExceptionMechanism("Bob", new MaximizePoints(), PlayerMethod.WIN),
                ObjectJsonDeserializer.deserializeActor(ObjectJsonDeserializer.jsonStringToJsonElement(actor1)));
        assertEquals(new UnavailableCardCheatMechanism("Bob", new MaximizePoints()),
                ObjectJsonDeserializer.deserializeActor(ObjectJsonDeserializer.jsonStringToJsonElement(actor2)));
        assertEquals(new TimeoutMechanism("Bob", new MaximizePoints(), PlayerMethod.REQUEST_CARDS, 4),
                ObjectJsonDeserializer.deserializeActor(ObjectJsonDeserializer.jsonStringToJsonElement(actor3)));
    }
}
