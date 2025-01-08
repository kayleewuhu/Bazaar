package Common.Json;

import Player.PlayerAPI;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.List;

import Common.Data.EquationTable;
import Common.Pair;
import Common.Turn_State;

// This class contains methods that deserializes method calls from JSON into
// the corresponding method in the player and the parameters for that method
public class MethodDeserializer {
  // deserializes the entire method JSON into the method name and the method's arguments
  // call the appropriate deserializing method on the second object to parse the arguments for the method
  public static Pair<String, JsonElement> methodDeserialize(String methodJson) {
    JsonArray info = JsonParser.parseString(methodJson).getAsJsonArray();
    JsonArray args = info.get(1).getAsJsonArray();
    return new Pair<>(ObjectJsonSerializer.jsonElementToString(info.get(0)), args.get(0));
  }

  // deserializes a boolean from JSON to a boolean object
  public static Boolean winDeserialize(JsonElement args) {
    return args.getAsBoolean();
  }
}
