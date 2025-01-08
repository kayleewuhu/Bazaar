package Client;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Data.PlayerMethod;
import Common.Json.MethodDeserializer;
import Common.Json.ObjectJsonDeserializer;
import Common.Json.ObjectJsonSerializer;
import Common.Json.ResponseSerializer;
import Common.Pair;
import Common.Turn_State;
import Player.PlayerAPI;
import com.google.gson.JsonElement;

// This class communicates with the referee on the server side for this player on this client.
// This is a proxy referee on the client side.
// For every JSON received over the socket, the following occurs:
// - deserialize the method call from JSON
// - call the corresponding method from the player
// - serialize the player's response to JSON
// - send the JSON response back to the server over the socket
// if the referee sends invalid json, disconnect and notify client
public class Referee {
  // the client's player that this proxy referee will interact with to determine their moves
  final PlayerAPI player;
  private final InputStream in;
  final OutputStream out;

  public Referee(PlayerAPI player, InputStream in, OutputStream out) {
    this.player = player;
    this.in = in;
    this.out = out;
  }

  // reads the method call JSON from the socket and sends the proper output JSON back through
  public void completeInteraction() {
    String methodJson = this.readInput();
    Pair<String, JsonElement> methodAndArgs = MethodDeserializer.methodDeserialize(methodJson);
    String response = this.getResponse(methodAndArgs.first, methodAndArgs.second);
    this.writeOutput(response);
  }

  // reads input from this socket
  public String readInput() {
    String input = "";
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(this.in));
      input = reader.readLine();
    }
    catch (Exception e) {
    }

    return input;
  }

  public void writeOutput(String output) {
    try {
      PrintWriter writer = new PrintWriter(this.out, true);
      writer.println(output);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  // returns the player's response as JSON according to its method that is called
  public String getResponse(String methodString, JsonElement args) {
    PlayerMethod method = PlayerMethod.fromString(methodString);
      return switch (method) {
        case SETUP -> this.getSetupResponse(args);
        case REQUEST_PEBBLE_OR_TRADES -> this.getPebbleTradesResponse(args);
        case REQUEST_CARDS -> this.getPurchasesResponse(args);
        case WIN -> this.getWinResponse(args);
      };
  }

  private String getSetupResponse(JsonElement args) {
    EquationTable equations = ObjectJsonDeserializer.deserializeEquationTable(args);
    this.player.setup(equations);
    return ObjectJsonSerializer.jsonElementToJsonString(ResponseSerializer.voidSerialize());
  }

  private String getPebbleTradesResponse(JsonElement args) {
    Turn_State turn = ObjectJsonDeserializer.deserializeTurnState(args, this.player.name(), this.player);
    DrawPebbleOrExchanges pebbleOrExchanges = this.player.requestPebbleOrExchanges(turn);
    return ObjectJsonSerializer.jsonElementToJsonString(ResponseSerializer.pebbleOrExchangesSerialize(pebbleOrExchanges));
  }

  private String getPurchasesResponse(JsonElement args) {
    Turn_State turn = ObjectJsonDeserializer.deserializeTurnState(args, this.player.name(), this.player);
    CardPurchases purchases = this.player.requestCards(turn);
    return ObjectJsonSerializer.jsonElementToJsonString(ResponseSerializer.cardPurchasesSerialize(purchases));
  }

  private String getWinResponse(JsonElement args) {
    boolean win = MethodDeserializer.winDeserialize(args);
    this.player.win(win);
    return ObjectJsonSerializer.jsonElementToJsonString(ResponseSerializer.voidSerialize());
  }


  //closes the input and output streams, shuts down the referee.
  public void closeStreams() {
    try {
      this.in.close();
      this.out.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
