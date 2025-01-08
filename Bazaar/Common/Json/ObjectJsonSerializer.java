package Common.Json;

import Common.Data.*;
import Common.Turn_State;
import Referee.Game_State;
import Referee.PlayerInfo;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;

// This class contains all methods to serialize actual object representations into JSON representations.
public class ObjectJsonSerializer {
  private final static Gson gson = new Gson();
  // to json string
  public static String jsonElementToJsonString(JsonElement element) {
    return gson.toJson(element);
  }

  // to java string
  public static String jsonElementToString(JsonElement element) {
    return element.getAsString();
  }

  public static String stringToJsonString(String str) {
    return gson.toJson(str);
  }

  public static JsonElement serializePebble(PebbleColor pebble) {
    return gson.toJsonTree(pebble.toString());
  }

  public static JsonElement serializePebbleCollection(PebbleCollection pebbles) {
    List<PebbleColor> pebblesList = pebbles.getPebblesAsList();
    List<JsonElement> pebbleStrings = new ArrayList<>();
    for (PebbleColor pebble : pebblesList) {
      pebbleStrings.add(ObjectJsonSerializer.serializePebble(pebble));
    }
    return gson.toJsonTree(pebbleStrings);
  }

  public static JsonElement serializeBidirectionalEquation(BidirectionalEquation equation) {
    List<JsonElement> eq = new ArrayList<>();
    JsonElement side1 = ObjectJsonSerializer.serializePebbleCollection(equation.getLeftSideCopy());
    JsonElement side2 = ObjectJsonSerializer.serializePebbleCollection(equation.getRightSideCopy());
    eq.add(side1);
    eq.add(side2);
    return gson.toJsonTree(eq);
  }

  public static JsonElement serializeUnidirectionalEquation(UnidirectionalEquation equation) {
    List<JsonElement> eq = new ArrayList<>();
    JsonElement input = ObjectJsonSerializer.serializePebbleCollection(equation.getInputSideCopy());
    JsonElement output = ObjectJsonSerializer.serializePebbleCollection(equation.getOutputSideCopy());
    eq.add(input);
    eq.add(output);
    return gson.toJsonTree(eq);
  }

  public static JsonElement serializeEquationTable(EquationTable equationTable) {
    List<BidirectionalEquation> equations = equationTable.getAllEquationsCopy();
    List<JsonElement> jsonEqs = new ArrayList<>();

    for (BidirectionalEquation equation : equations) {
      jsonEqs.add(ObjectJsonSerializer.serializeBidirectionalEquation(equation));
    }
    return gson.toJsonTree(jsonEqs);
  }

  public static JsonElement serializeCard(Card card) {
    JsonObject cardJson = new JsonObject();
    cardJson.add("pebbles", ObjectJsonSerializer.serializePebbleCollection(card.getCardPebblesCopy()));
    cardJson.add("face?", gson.toJsonTree(card.hasSmiley()));
    return cardJson;
  }

  public static JsonElement serializeCards(List<Card> cards) {
    List<JsonElement> cardJsons = new ArrayList<>();

    for (Card card : cards) {
      cardJsons.add(ObjectJsonSerializer.serializeCard(card));
    }
    return gson.toJsonTree(cardJsons);
  }

  public static JsonElement serializePlayerInfo(PlayerInfo playerInfo) {
    JsonObject playerInfoJson = new JsonObject();
    playerInfoJson.add("cards", ObjectJsonSerializer.serializeCards(playerInfo.getPurchasedCards()));
    playerInfoJson.add("wallet", ObjectJsonSerializer.serializePebbleCollection(playerInfo.getWalletCopy()));
    playerInfoJson.add("score", gson.toJsonTree(playerInfo.getScore()));
    return playerInfoJson;
  }

  public static JsonElement serializeScores(List<Integer> scores) {
    return gson.toJsonTree(scores);
  }

  public static JsonElement serializeTurnState(Turn_State turnState) {
    JsonObject turnStateJson = new JsonObject();
    turnStateJson.add("bank", ObjectJsonSerializer.serializePebbleCollection(turnState.bankPebbles()));
    turnStateJson.add("cards", ObjectJsonSerializer.serializeCards(turnState.visibleCards()));
    turnStateJson.add("active", ObjectJsonSerializer.serializePlayerInfo(turnState.activePlayer()));
    turnStateJson.add("scores", ObjectJsonSerializer.serializeScores(turnState.remainingPlayerScores()));
    return turnStateJson;
  }

  public static JsonElement serializeCardPurchases(CardPurchases cardPurchases) {
    return ObjectJsonSerializer.serializeCards(cardPurchases.getSequenceOfCardPurchasesCopy());
  }

  public static JsonElement serializeExchanges(Exchanges exchanges) {
    List<JsonElement> exchangesJsons = new ArrayList<>();
    for (UnidirectionalEquation eq : exchanges.getSequenceOfExchangesCopy()) {
      exchangesJsons.add(ObjectJsonSerializer.serializeUnidirectionalEquation(eq));
    }
    return gson.toJsonTree(exchangesJsons);
  }

  public static JsonElement serializePlayerInfos(List<PlayerInfo> playerInfos) {
    List<JsonElement> playerInfoJsons = new ArrayList<>();
    for (PlayerInfo playerInfo : playerInfos) {
      playerInfoJsons.add(ObjectJsonSerializer.serializePlayerInfo(playerInfo));
    }
    return gson.toJsonTree(playerInfoJsons);
  }

  public static JsonElement serializeGameState(Game_State gameState) {
    JsonObject gameStateJson = new JsonObject();
    gameStateJson.add("bank", ObjectJsonSerializer.serializePebbleCollection(gameState.getBank()));
    gameStateJson.add("visibles", ObjectJsonSerializer.serializeCards(gameState.getCards().getVisibleCardsCopy()));
    gameStateJson.add("cards", ObjectJsonSerializer.serializeCards(gameState.getCards().getInvisibleCardsCopy()));
    gameStateJson.add("players", ObjectJsonSerializer.serializePlayerInfos(gameState.getPlayersAsList()));
    return gameStateJson;
  }
}
