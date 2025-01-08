package Server;
import Common.*;
import Common.Data.EquationTable;
import Common.Json.ObjectJsonDeserializer;
import Common.Json.ObjectJsonSerializer;
import Referee.Game_State;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonStreamParser;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// The entry point for the server program.
// Creates a server, waits for client registration, runs the entire game, returns the result.
public class XServer {
    public static void main(String[] args) {
        Server server = new Server(Integer.parseInt(args[0]));
        JsonStreamParser parser = new JsonStreamParser(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        Gson gson = new GsonBuilder().create();

        EquationTable equations = ObjectJsonDeserializer.deserializeEquationTable(parser.next());
        Game_State gameState = ObjectJsonDeserializer.deserializeGameState(parser.next());
        IGameMode gameMode = new DefaultGameMode();

        if (parser.hasNext()) {
            gameMode = determineGameMode(ObjectJsonSerializer.jsonElementToString(parser.next()));
        }

        server.manageSignUp();
        server.initializeDeterministicGame(gameState, equations, gameMode);
        System.out.println(gson.toJson(server.returnGameResult()));
        server.shutdown();
        System.exit(0);
    }

    //returns the desired game mode given a string that is either USA / SEY
    private static IGameMode determineGameMode(String bonus) {
        Bonus gameMode = Bonus.fromString(bonus);
        return switch (gameMode) {
            case USA -> new USAGameMode();
            case SEY -> new SEYGameMode();
        };
    }
}
