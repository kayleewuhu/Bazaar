package Common.Json;

import Common.Data.PlayerMethod;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Common.Data.EquationTable;
import Common.Turn_State;
import Tests.JsonObjects.TurnStateJson;
import com.google.gson.JsonElement;

// This class serializes the server referee's method calls into JSON to send to the client.
public class MethodSerializer {
  private final static Gson gson = new Gson();

  // serializes the setup method call into JSON
  public static JsonElement setupSerialize(EquationTable equations) {
    return MethodSerializer.methodSerialize(PlayerMethod.SETUP.getMethodString(),
            ObjectJsonSerializer.serializeEquationTable(equations));
  }

  // serializes the request Pebble or Trades method call into JSON
  public static JsonElement requestPebbleTradesSerialize(Turn_State turn) {
    return MethodSerializer.methodSerialize(PlayerMethod.REQUEST_PEBBLE_OR_TRADES.getMethodString(),
            ObjectJsonSerializer.serializeTurnState(turn));
  }

  // serializes the request Card Purchases method call into JSON
  public static JsonElement requestCardPurchasesSerialize(Turn_State turn) {
    return MethodSerializer.methodSerialize(PlayerMethod.REQUEST_CARDS.getMethodString(),
            ObjectJsonSerializer.serializeTurnState(turn));
  }

  // serializes the win method call into JSON
  public static JsonElement winSerialize(boolean win) {
    return MethodSerializer.methodSerialize(PlayerMethod.WIN.getMethodString(), gson.toJsonTree(win));
  }

  // serializes the given method and its arguments into JSON
  private static JsonElement methodSerialize(String method, JsonElement args) {
    List<JsonElement> methodCallJson = new ArrayList<>();
    methodCallJson.add(gson.toJsonTree(method));
    methodCallJson.add(gson.toJsonTree(new ArrayList<>(List.of(args))));
    return gson.toJsonTree(methodCallJson);
  }
}
