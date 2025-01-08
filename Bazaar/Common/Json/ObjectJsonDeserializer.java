package Common.Json;

import Common.Data.*;
import Player.*;
import Referee.Game_State;
import Referee.PlayerInfo;
import com.google.gson.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import Common.Turn_State;

// This class contains all methods to deserialize JSON representations into actual object representations.
public class ObjectJsonDeserializer {
  public static JsonElement jsonStringToJsonElement(String str) {
    return JsonParser.parseString(str);
  }

  public static PebbleColor deserializePebble(JsonElement pebble) {
    return PebbleColor.fromString(pebble.getAsString());
  }

  public static PebbleCollection deserializePebbleCollection(JsonElement pebbles) {
    JsonArray pebblesArray = pebbles.getAsJsonArray();
    List<PebbleColor> pebbleColors = new ArrayList<>();

    for (JsonElement pebble : pebblesArray) {
      PebbleColor pebbleColor = ObjectJsonDeserializer.deserializePebble(pebble);
      pebbleColors.add(pebbleColor);
    }
    return new PebbleCollection(pebbleColors);
  }

  public static BidirectionalEquation deserializeBidirectionalEquation(JsonElement biEquation) {
    JsonArray equationArray = biEquation.getAsJsonArray();
    return new BidirectionalEquation(ObjectJsonDeserializer.deserializePebbleCollection(equationArray.get(0)),
            ObjectJsonDeserializer.deserializePebbleCollection(equationArray.get(1)));
  }

  public static UnidirectionalEquation deserializeUnidirectionalEquation(JsonElement uniEquation) {
    JsonArray equationArray = uniEquation.getAsJsonArray();
    return new UnidirectionalEquation(ObjectJsonDeserializer.deserializePebbleCollection(equationArray.get(0)),
            ObjectJsonDeserializer.deserializePebbleCollection(equationArray.get(1)));
  }

  public static EquationTable deserializeEquationTable(JsonElement equations) {
    JsonArray equationsArray = equations.getAsJsonArray();
    List<BidirectionalEquation> equationTable = new ArrayList<>();

    for (JsonElement eq : equationsArray) {
      BidirectionalEquation deserializedEquation = ObjectJsonDeserializer.deserializeBidirectionalEquation(eq);
      equationTable.add(deserializedEquation);
    }
    return new EquationTable(equationTable);
  }

  public static Card deserializeCard(JsonElement card) {
    JsonObject cardObject = card.getAsJsonObject();
    PebbleCollection pebbles = ObjectJsonDeserializer.deserializePebbleCollection(cardObject.get("pebbles"));
    boolean face = cardObject.get("face?").getAsBoolean();
    return new Card(pebbles, face);
  }

  public static List<Card> deserializeCards(JsonElement cards) {
    JsonArray cardsArray = cards.getAsJsonArray();
    List<Card> cardList = new ArrayList<>();

    for (JsonElement card : cardsArray) {
      cardList.add(ObjectJsonDeserializer.deserializeCard(card));
    }
    return cardList;
  }

  public static PlayerInfo deserializePlayerInfo(JsonElement playerInfo, String name, PlayerAPI player) {
    JsonObject playerInfoObject = playerInfo.getAsJsonObject();

    List<Card> cards = ObjectJsonDeserializer.deserializeCards(playerInfoObject.get("cards"));
    PebbleCollection wallet = ObjectJsonDeserializer.deserializePebbleCollection(playerInfoObject.get("wallet"));
    int score = playerInfoObject.get("score").getAsInt();

    return new PlayerInfo(wallet, score, name, player, cards);
  }

  public static List<Integer> deserializeScores(JsonElement scores) {
    JsonArray scoresArray = scores.getAsJsonArray();
    List<Integer> scoresList = new ArrayList<>();

    for (JsonElement score : scoresArray) {
      scoresList.add(score.getAsInt());
    }
    return scoresList;
  }

  public static Turn_State deserializeTurnState(JsonElement turnState, String name, PlayerAPI player) {
    JsonObject turnStateObject = turnState.getAsJsonObject();

    PebbleCollection bank = ObjectJsonDeserializer.deserializePebbleCollection(turnStateObject.get("bank"));
    List<Card> cards = ObjectJsonDeserializer.deserializeCards(turnStateObject.get("cards"));
    PlayerInfo playerInfo = ObjectJsonDeserializer.deserializePlayerInfo(turnStateObject.get("active"), name, player);
    List<Integer> scores = ObjectJsonDeserializer.deserializeScores(turnStateObject.get("scores"));

    return new Turn_State(bank, playerInfo, scores, cards);
  }

  public static CardPurchases deserializeCardPurchases(JsonElement cardPurchases) {
    List<Card> cards = ObjectJsonDeserializer.deserializeCards(cardPurchases);
    return new CardPurchases(cards);
  }

  public static Exchanges deserializeExchanges(JsonElement exchanges) {
    JsonArray exchangesArray = exchanges.getAsJsonArray();
    List<UnidirectionalEquation> uniEqs = new ArrayList<>();

    for (JsonElement exchange : exchangesArray) {
      uniEqs.add(ObjectJsonDeserializer.deserializeUnidirectionalEquation(exchange));
    }
    return new Exchanges(uniEqs);
  }

  // connections to players are not provided, will need to connect a list of PlayerAPIs to the
  // returned list of PlayerInfos
  public static List<PlayerInfo> deserializePlayerInfos(JsonElement playerInfos) {
    JsonArray playerInfosArray = playerInfos.getAsJsonArray();
    List<PlayerInfo> playerInfoList = new ArrayList<>();

    for (JsonElement playerInfo : playerInfosArray) {
      playerInfoList.add(ObjectJsonDeserializer.deserializePlayerInfo(playerInfo, "unknown", null));
    }
    return playerInfoList;
  }

  // assumes the gameState passed in is for testing, before connections to players are provided
  // must connect PlayerAPIs to the PlayerInfos in this game state before running a game
  public static Game_State deserializeGameState(JsonElement gameState) {
    JsonObject gameStateObject = gameState.getAsJsonObject();

    PebbleCollection bank = ObjectJsonDeserializer.deserializePebbleCollection(gameStateObject.get("bank"));
    List<Card> visibleCards = ObjectJsonDeserializer.deserializeCards(gameStateObject.get("visibles"));
    List<Card> invisibleCards = ObjectJsonDeserializer.deserializeCards(gameStateObject.get("cards"));
    List<PlayerInfo> players = ObjectJsonDeserializer.deserializePlayerInfos(gameStateObject.get("players"));

    return new Game_State(bank, new CardCollection(visibleCards, invisibleCards), new ArrayDeque<>(players));
  }

  public static PlayerAPI deserializeActor(JsonElement actor) {
    JsonArray actorArray = actor.getAsJsonArray();
    return switch (actorArray.size()) {
      case 2 -> new Mechanism(ObjectJsonSerializer.jsonElementToString(actorArray.get(0)),
              ObjectJsonDeserializer.deserializePolicy(actorArray.get(1)));
      case 3 -> new ExceptionMechanism(ObjectJsonSerializer.jsonElementToString(actorArray.get(0)),
              ObjectJsonDeserializer.deserializePolicy(actorArray.get(1)),
              PlayerMethod.fromString(ObjectJsonSerializer.jsonElementToString(actorArray.get(2))));
      case 4 -> {
        if (ObjectJsonSerializer.jsonElementToString(actorArray.get(2)).equals("a cheat")) {
          yield ObjectJsonDeserializer.deserializeCheatActor(actorArray);
        } else {
          yield new TimeoutMechanism(ObjectJsonSerializer.jsonElementToString(actorArray.get(0)),
                  ObjectJsonDeserializer.deserializePolicy(actorArray.get(1)),
                  PlayerMethod.fromString(ObjectJsonSerializer.jsonElementToString(actorArray.get(2))),
                  actorArray.get(3).getAsInt());
        }
      }
        default -> throw new IllegalStateException("Actor not recognized");
    };
  }

  public static Strategy deserializePolicy(JsonElement policyJson) {
    Policy policy = Policy.fromString(ObjectJsonSerializer.jsonElementToString(policyJson));

    return switch (policy) {
      case MAX_POINTS -> new MaximizePoints();
      case MAX_CARDS -> new MaximizeCards();
    };
  }

  private static PlayerAPI deserializeCheatActor(JsonArray cheatActor) {
    String name = ObjectJsonSerializer.jsonElementToString(cheatActor.get(0));
    Strategy strategy = ObjectJsonDeserializer.deserializePolicy(cheatActor.get(1));

    return switch (Cheat.fromString(ObjectJsonSerializer.jsonElementToString(cheatActor.get(3)))) {
        case NO_EQUATION -> new NonExistentEquationCheatMechanism(name, strategy);
        case BANK_NO_TRADE -> new BankCannotTradeCheatMechanism(name, strategy);
        case WALLET_NO_TRADE -> new WalletCannotTradeCheatMechanism(name, strategy);
        case NO_CARD -> new UnavailableCardCheatMechanism(name, strategy);
        case WALLET_NO_CARD -> new WalletCannotBuyCardCheatMechanism(name, strategy);
    };
  }
}
