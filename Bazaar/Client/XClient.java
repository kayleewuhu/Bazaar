package Client;

import Common.Constants;
import Common.Json.ObjectJsonDeserializer;
import Player.PlayerAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

// The entry point for the clients program.
// Creates a separate process for each client.
public class XClient {
    public static void main(String[] args)
    {
        int port = Integer.parseInt(args[0]);
        JsonStreamParser parser = new JsonStreamParser(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        JsonArray players = parser.next().getAsJsonArray();

        for (JsonElement playerAPI : players) {
            PlayerAPI player = ObjectJsonDeserializer.deserializeActor(playerAPI);
            XClient.createClient(player, port);
        }
    }

    //Creates the client for a given player using a given port to a running server
    //spawns a thread for the new client.
    private static void createClient(PlayerAPI player, int port) {
        new Thread(() -> {
                Client client = new Client(port, player);
                client.playGame();
        }).start();

        try {
            TimeUnit.SECONDS.sleep(Constants.NAME_WAIT_SEC);
        }
        catch (InterruptedException e) {
            System.out.println("Sleeping was interrupted: " + e.getMessage());
        }
    }
}
