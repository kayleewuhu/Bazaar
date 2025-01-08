package Common.Json;

import com.google.gson.Gson;

import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Tests.JsonObjects.CardPurchasesJson;
import com.google.gson.JsonElement;

// This class contains methods that serialize responses from a client player to JSON to send
// to the server.
public class ResponseSerializer {
  private final static Gson gson = new Gson();

  // returns "void" string
  public static JsonElement voidSerialize() {
    return gson.toJsonTree("void");
  }

  // serializes a Draw Pebble or Exchanges object into a JSON string
  public static JsonElement pebbleOrExchangesSerialize(DrawPebbleOrExchanges response) {
    return response.toJson(gson);
  }

  // serializes a Card Purchases object into a JSON string
  public static JsonElement cardPurchasesSerialize(CardPurchases response) {
    return ObjectJsonSerializer.serializeCardPurchases(response);
  }
}
