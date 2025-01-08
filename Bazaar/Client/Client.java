package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import Common.Constants;

import Common.Json.ObjectJsonSerializer;
import Player.PlayerAPI;

// This class represents the client machine, where the actual player resides. The client
// connects to the server and registers the player.
public class Client {
  // the proxy referee that interacts with the client's player
  private Referee referee;

  // connects to the server, creates a proxy ref, and sends the player's name
  public Client(int port, PlayerAPI player) {
    try {
      Optional<Socket> socket = this.connect(port);

      if (socket.isEmpty()) {
        System.out.println("Connection failed. Server is not available.");
        return;
      }

      this.referee = new Referee(player, socket.get().getInputStream(), socket.get().getOutputStream());
      this.sendName();
    }
    catch (InterruptedException e) {
      System.out.println("Connection to server interrupted: " + e.getMessage());
    }
    catch (IOException e) {
      System.out.println("Getting input and output streams failed: " + e.getMessage());
    }
  }

  // waits to read information sent from the Server and responds
  public void playGame() {
    try {
      while (true) {
        this.referee.completeInteraction();
      }
    }
    catch (Exception e) {
      this.referee.closeStreams();
    }
  }

  // try to connect to the server
  // if the server is not currently running, will retry a given number of times
  private Optional<Socket> connect(int port) throws InterruptedException {
    int numRetries = 0;

    while (numRetries < Constants.CLIENT_CONNECT_MAX_RETRY_ATTEMPTS) {
      try {
        numRetries++;
        Socket socket = new Socket("localhost", port);
        return Optional.of(socket);
      }
      // server is not up, will retry
      catch (IOException e) {
        TimeUnit.SECONDS.sleep(Constants.CLIENT_RETRY_FOR_SERVER_SEC);
      }
    }
    return Optional.empty();
  }

  // send player name to the server
  public void sendName() {
    try {
      PrintWriter out = new PrintWriter(this.referee.out, true);
      out.println(ObjectJsonSerializer.stringToJsonString(this.referee.player.name()));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
