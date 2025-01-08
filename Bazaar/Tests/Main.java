package Tests;

import Common.DefaultGameMode;
import Common.Json.ObjectJsonDeserializer;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Data.PebbleCollection;
import Common.Data.UnidirectionalEquation;
import Common.Pair;
import Common.Rule_Book;
import Common.Turn_State;
import Player.DoNothing;
import Player.ExceptionMechanism;
import Player.MaximizeCards;
import Player.MaximizePoints;
import Player.Mechanism;
import Player.PlayerAPI;
import Player.Strategy;
import Referee.Game_State;
import Referee.PlayerInfo;
import Referee.Referee;
import Tests.JsonObjects.BidirectionalEquationJson;
import Tests.JsonObjects.CardPurchasesJson;
import Tests.JsonObjects.EquationTableJson;
import Tests.JsonObjects.ExchangesJson;
import Tests.JsonObjects.GameStateJson;
import Tests.JsonObjects.PebbleCollectionJson;
import Tests.JsonObjects.TurnStateJson;
import Tests.JsonObjects.UnidirectionalEquationJson;
import Referee.Observer;
import Referee.VisualObserver;

import Client.Client;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();
        JsonStreamParser parser = new JsonStreamParser(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        JsonArray players = parser.next().getAsJsonArray();
        List<PlayerAPI> actors = new ArrayList<>();

        for (JsonElement player : players) {
            actors.add(ObjectJsonDeserializer.deserializeActor(player));
        }

        EquationTable equations = ObjectJsonDeserializer.deserializeEquationTable(parser.next());
        Game_State gameState = ObjectJsonDeserializer.deserializeGameState(parser.next());
        Referee ref = new Referee(actors, gameState, equations, new DefaultGameMode());

        for (String arg : args) {
            if (arg.equals("--show")) {
                Observer observer = new VisualObserver();
                ref.addObserver(observer);
            }
        }

        Pair<List<PlayerInfo>, List<PlayerInfo>> winnersAndKicked = ref.runGame();
        List<List<String>> winnersAndKickedNames = ref.winnersAndKickedPlayersNames(winnersAndKicked);
        System.out.println(gson.toJson(winnersAndKickedNames.get(0)));
        System.out.println(gson.toJson(winnersAndKickedNames.get(1)));
    }
//
////    private static void createImages() {
////        CardRenderer.renderImageFile(new Card(Constants.NUM_PEBBLES_ON_CARD, new Random(1)), 100, 200, 20, 40, "./Tests/GameOutput/CardWithoutFace.png");
////        CardRenderer.renderImageFile(new Card(Constants.NUM_PEBBLES_ON_CARD, new Random(2)), 100, 200, 20, 40, "./Tests/GameOutput/CardWithFace.png");
////        CardCollection cardCollection = new CardCollection(new Random(1), Constants.NUM_VISIBLE_CARDS, Constants.NUM_CARDS, Constants.NUM_PEBBLES_ON_CARD);
////        CardCollectionRenderer.renderImageFile(cardCollection, 10, 15, 100, 200, 20, 40, "./Tests/GameOutput/CardCollection.png");
////        EquationTableRenderer.renderImageFile(Constants.EQUATIONS, 20, "./Tests/GameOutput/EquationTable.png");
////        Game_State gameState = new Game_State(
////                new PebbleCollection(100, new Random()),
////                new CardCollection(new Random(), Constants.NUM_VISIBLE_CARDS, Constants.NUM_CARDS, Constants.NUM_PEBBLES_ON_CARD),
////                new ArrayDeque<>(Arrays.asList(
////                        new PlayerInfo(new PebbleCollection(20, new Random())),
////                        new PlayerInfo(new PebbleCollection(20, new Random())),
////                        new PlayerInfo(new PebbleCollection(20, new Random())))));
////        GameStateRenderer.renderImageFile(gameState, "./Tests/GameOutput/GameState.png");
////        TurnStateRenderer.renderImageFile(gameState.getTurnState(), "./Tests/GameOutput/TurnState.png");
////    }
//
//    //private static void test
//    private static void observerTest() {
//        Gson gson = new GsonBuilder().create();
//        List<PlayerAPI> players = new ArrayList<>();
//        players.add(new Mechanism("player 1", new MaximizeCards()));
//        players.add(new Mechanism("player 2", new MaximizePoints()));
//        players.add(new ExceptionMechanism("exception", new DoNothing(), "request-pebble-or-trades"));
//        Referee ref = new Referee(players);
//        Observer saveObserver = new VisualObserver();
//        ref.addObserver(saveObserver);
//        Pair<List<PlayerInfo>, List<PlayerInfo>> winnersAndKicked = ref.runGame();
//        List<PlayerInfo> winners = winnersAndKicked.first;
//        List<PlayerInfo> kicked = winnersAndKicked.second;
//        List<String> winnerNames = winners.stream().map(PlayerInfo::getName).sorted().toList();
//        List<String> kickedNames = kicked.stream().map(PlayerInfo::getName).sorted().toList();
//        System.out.println(gson.toJson(winnerNames));
//        System.out.println(gson.toJson(kickedNames));
//    }
//
//    private static void xturn() {
//        Gson gson = new GsonBuilder().create();
//        JsonStreamParser parser = new JsonStreamParser(
//                new InputStreamReader(System.in, StandardCharsets.UTF_8));
//
//        GameStateJson gameStateJson = gson.fromJson(parser.next(), GameStateJson.class);
//        Game_State gameState = gameStateJson.parseIntoObject();
//        Turn_State turnState = gameState.getTurnState();
//        TurnStateJson turnStateJson = new TurnStateJson(turnState);
//        System.out.println(gson.toJson(turnStateJson));
//    }
//
//    private static void xstrategy() {
//        Gson gson = new GsonBuilder().create();
//        JsonStreamParser parser = new JsonStreamParser(
//                new InputStreamReader(System.in, StandardCharsets.UTF_8));
//        List<List<List<String>>> globalEquationsAsString = gson.fromJson(parser.next().getAsJsonArray(),
//                new TypeToken<List<List<List<String>>>>(){}.getType());
//        List<BidirectionalEquationJson> globalEquations = globalEquationsAsString.stream()
//                .map(BidirectionalEquationJson::new).collect(Collectors.toList());
//        EquationTable equationTable = new EquationTableJson(globalEquations).parseIntoObject();
//        TurnStateJson turnStateJson = gson.fromJson(parser.next(), TurnStateJson.class);
//        Turn_State turnState = turnStateJson.parseIntoObject();
//        String maximizationPolicy = gson.fromJson(parser.next().getAsString(), String.class);
//        Strategy strat;
//        if (maximizationPolicy.equals("purchase-points")) {
//            strat = new MaximizePoints(4);
//        } else {
//            strat = new MaximizeCards(4);
//        }
//        Pair<Exchanges, CardPurchases> bestMove = strat.whatExchangesAndCardPurchases(turnState, equationTable);
//        Exchanges exchanges = bestMove.first;
//        ExchangesJson exchangesJson = new ExchangesJson(exchanges);
//        CardPurchases cardPurchases = bestMove.second;
//        CardPurchasesJson cardPurchasesJson = new CardPurchasesJson(cardPurchases);
//        PebbleCollection updatedWalletAfterExchanges = exchanges.attemptAction(turnState, equationTable).get().first;
//        PlayerInfo updatedPlayerInfoAfterExchanges = new PlayerInfo(updatedWalletAfterExchanges);
//        PebbleCollection updatedBankAfterExchanges = exchanges.attemptAction(turnState, equationTable).get().second;
//        Turn_State turnStateAfterExchanges = new Turn_State(updatedBankAfterExchanges, updatedPlayerInfoAfterExchanges,
//                turnState.remainingPlayerScores(), turnState.visibleCards());
//        PebbleCollection updatedWalletAfterCardPurchases = cardPurchases.attemptAction(turnStateAfterExchanges).get().first;
//        PebbleCollectionJson updatedWalletAfterCardPurchasesJson = new PebbleCollectionJson(updatedWalletAfterCardPurchases);
//        System.out.println(gson.toJson(exchangesJson.rules));
//        System.out.println(gson.toJson(cardPurchasesJson.cards));
//        System.out.println(gson.toJson(Rule_Book.totalPointsFromCardPurchases(cardPurchases, updatedWalletAfterExchanges)));
//        System.out.println(gson.toJson(updatedWalletAfterCardPurchasesJson.toListOfString()));
//    }
//
//    private static void xrules() {
//        Gson gson = new GsonBuilder().create();
//        JsonStreamParser parser = new JsonStreamParser(
//                new InputStreamReader(System.in, StandardCharsets.UTF_8));
//        List<List<List<String>>> globalEquationsAsString = gson.fromJson(parser.next().getAsJsonArray(),
//                new TypeToken<List<List<List<String>>>>(){}.getType());
//        List<BidirectionalEquationJson> globalEquations = globalEquationsAsString.stream()
//                .map(BidirectionalEquationJson::new).collect(Collectors.toList());
//        EquationTable equationTable = new EquationTableJson(globalEquations).parseIntoObject();
//        List<List<List<String>>> rulesAsString = gson.fromJson(parser.next().getAsJsonArray(),
//                new TypeToken<List<List<List<String>>>>(){}.getType());
//        List<UnidirectionalEquation> rules = rulesAsString.stream()
//                .map(UnidirectionalEquationJson::new).map(UnidirectionalEquationJson::parseIntoObject)
//                .collect(Collectors.toList());
//        DrawPebbleOrExchanges exchanges = new Exchanges(rules);
//        Turn_State turnState = gson.fromJson(parser.next(), TurnStateJson.class).parseIntoObject();
//
//
//        Optional<Pair<PebbleCollection, PebbleCollection>> walletAndBank = exchanges.attemptAction(turnState, equationTable);
//        if (walletAndBank.isPresent()) {
//            PebbleCollection wallet = walletAndBank.get().first;
//            PebbleCollection bank = walletAndBank.get().second;
//            PebbleCollectionJson walletJson = new PebbleCollectionJson(wallet);
//            PebbleCollectionJson bankJson = new PebbleCollectionJson(bank);
//            System.out.println(gson.toJson(walletJson.toListOfString()));
//            System.out.println(gson.toJson(bankJson.toListOfString()));
//        } else {
//            System.out.println(gson.toJson(false));
//        }
//    }
}