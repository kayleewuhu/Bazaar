package Server;

import java.io.*;

import Common.Constants;
import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Json.MethodSerializer;
import Common.Json.ObjectJsonDeserializer;
import Common.Json.ObjectJsonSerializer;
import Common.Json.ResponseDeserializer;
import Common.Turn_State;
import Player.PlayerAPI;

// This class communicates with the player on a client.
// This is a proxy player on the server's side.
// In every method, the following occurs:
// - serialize the method call into JSON
// - send the method call JSON through the socket
// - read the response JSON from the socket
// - deserialize the response JSON into the corresponding object and return that object
// if the player sends invalid json, remove them
public class Player implements PlayerAPI {
  // the player's name
  private final String name;
  // the stream the player reads from
  private final InputStream in;
  // the stream the player writes to
  private final OutputStream out;

  public Player(String name, InputStream in, OutputStream out) {
    this.name = name;
    this.in = in;
    this.out = out;
  }

  // returns the name of the player
  @Override
  public String name() {
    return this.name;
  }

  //sets up the player with the equation table
  @Override
  public void setup(EquationTable equations) {
      String output = ObjectJsonSerializer.jsonElementToJsonString(MethodSerializer.setupSerialize(equations));
      this.sendOutput(output);
      String response = this.getInput();
      ResponseDeserializer.voidDeserialize(ObjectJsonDeserializer.jsonStringToJsonElement(response));
  }

  //requests the first move from the player, receives a pebble draw or exchanges request in return
  @Override
  public DrawPebbleOrExchanges requestPebbleOrExchanges(Turn_State turnState) {
    String output = ObjectJsonSerializer.jsonElementToJsonString(MethodSerializer.requestPebbleTradesSerialize(turnState));
    this.sendOutput(output);
    String response = this.getInput();
    return ResponseDeserializer.pebbleOrExchangesDeserialize(ObjectJsonDeserializer.jsonStringToJsonElement(response));
  }

  //requests the second move from the player, receives a card purchase request in return
  @Override
  public CardPurchases requestCards(Turn_State turnState) {
    String output = ObjectJsonSerializer.jsonElementToJsonString(MethodSerializer.requestCardPurchasesSerialize(turnState));
    this.sendOutput(output);
    String response = this.getInput();
    return ResponseDeserializer.cardPurchasesDeserialize(ObjectJsonDeserializer.jsonStringToJsonElement(response));
  }

  //notifies a player if they won or lost the game
  @Override
  public void win(boolean win) {
    String output = ObjectJsonSerializer.jsonElementToJsonString(MethodSerializer.winSerialize(win));
    this.sendOutput(output);
    String response = this.getInput();
    ResponseDeserializer.voidDeserialize(ObjectJsonDeserializer.jsonStringToJsonElement(response));
    this.closeStreams();
  }

  // takes in a string that represents a method to be called on a real player
  // sends the string as a json to the client side
  private void sendOutput(String output) {
    try {
      PrintWriter writer = new PrintWriter(this.out, true);
      writer.println(output);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  // receives a json input from the client side representing a response from a real player in json
  // throw an exception if the player does not respond in the allotted time
  // the exception will be caught by the ref
  private String getInput() {
    String input = "";
    BufferedReader reader = new BufferedReader(new InputStreamReader(this.in));
    try {
      input = reader.readLine();
    }
    catch (IOException e) {
      System.out.println("Reading from the socket failed.");
    }
    return input;
  }

  //closes the input and output streams of a player, "shuts down" a player.
  private void closeStreams() {
    try {
      this.in.close();
      this.out.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
