//package Tests;
//
//import Player.DoNothing;
//import Player.MaximizeCards;
//import Player.Mechanism;
//import Player.PlayerAPI;
//import Server.Server;
//import Client.Client;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class RemoteInteractionTests {
//    @Test
//    // one player registers, client is created before server is
//    // should run for total sign up periods * max wait time
//    void testRegisterOnePlayer() {
//        new Thread(() -> {
//            PlayerAPI player = new Mechanism("Bob", new DoNothing());
//            Client client = new Client(45678, player);
//        }).start();
//
//        Server server = new Server(45678);
//        List<List<String>> results = server.registerPlayersAndRunGame();
//        assertEquals(results, new ArrayList<>(List.of(new ArrayList<>(), new ArrayList<>())));
//    }
//
//    @Test
//    void test2PlayerRegister() {
//        Server server = new Server(45678);
//        new Thread(() -> {
//            PlayerAPI player = new Mechanism("Bob", new DoNothing());
//            Client client = new Client(45678, player);
//        }).start();
//        new Thread(() -> {
//            PlayerAPI player = new Mechanism("Ella", new MaximizeCards());
//            Client client = new Client(45678, player);
//        }).start();
//        List<List<String>> results = server.registerPlayersAndRunGame();
//        assertEquals(results, new ArrayList<>(List.of(new ArrayList<>(List.of("Ella")), new ArrayList<>())));
//    }
//
//    @Test
//    void testNoName() {
//        Server server = new Server(45678);
//        new Thread(() -> {
//            PlayerAPI player = new Mechanism("Bob", new DoNothing());
//            Client client = new Client(45678, player);
//            client.sendName();
//        }).start();
//        new Thread(() -> {
//            PlayerAPI player = new Mechanism("Mom", new DoNothing());
//            Client client = new Client(45678, player);
//        }).start();
//        List<List<String>> results = server.registerPlayersAndRunGame();
//    }
//}
