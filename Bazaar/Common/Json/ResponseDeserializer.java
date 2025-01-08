package Common.Json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.List;

import Common.Data.CardPurchases;
import Common.Data.DrawPebble;
import Common.Data.DrawPebbleOrExchanges;

// This class contains methods that deserialize the JSON responses from the client player
// into the corresponding data objects for the server's referee to use.
public class ResponseDeserializer {
  // deserializes the void string
  public static void voidDeserialize(JsonElement response) {
    String voidString = ObjectJsonSerializer.jsonElementToString(response);

    if (!voidString.equals("void")) {
      throw new IllegalArgumentException("Incorrect response");
    }
  }

  // deserializes the JSON response into a Draw Pebble or Exchanges object
  public static DrawPebbleOrExchanges pebbleOrExchangesDeserialize(JsonElement response) {
    if (response.isJsonPrimitive() && response.getAsJsonPrimitive().isBoolean()) {
      return new DrawPebble();
    } else if (response.isJsonArray()) {
      return ObjectJsonDeserializer.deserializeExchanges(response);
    } else {
      throw new IllegalArgumentException("Not a valid response");
    }
  }

  public static CardPurchases cardPurchasesDeserialize(JsonElement response) {
    return ObjectJsonDeserializer.deserializeCardPurchases(response);
  }
}
