package Referee;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import Common.Constants;
import Common.Data.CardPurchases;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.EquationTable;
import Common.Turn_State;
import Player.PlayerAPI;

// Sits in between the Referee and each Player to translate the player's response to the referee to be handled
// Calls the methods on the given player and return an Optional Empty if an error is thrown by the player
// Will also notify the referee if the request to the player times out (player takes too long to respond)
public class PlayerProxy {
  // returns the player's name
  public static String name(PlayerAPI player) {
    return player.name();
  }

  // sends the Equation Table of the game to the player
  public static Optional<Boolean> setup(PlayerAPI player, EquationTable equations) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<Boolean> future = executor.submit(()-> player.setup(equations), true);
    try {
      return Optional.of(future.get(Constants.MOVE_TIMEOUT_SEC, TimeUnit.SECONDS));
    }
    catch (Exception e) {
      future.cancel(true);
      return Optional.empty();
    }
    finally {
      executor.shutdownNow();
    }
  }

  // gets the player's request to either draw a pebble or perform exchanges
  public static Optional<DrawPebbleOrExchanges> requestPebbleOrExchanges(PlayerAPI player, Turn_State turnState) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<DrawPebbleOrExchanges> future = executor.submit(()-> player.requestPebbleOrExchanges(turnState));
    try {
      return Optional.of(future.get(Constants.MOVE_TIMEOUT_SEC, TimeUnit.SECONDS));
    } catch (Exception e) {
      future.cancel(true);
      return Optional.empty();
    }
    finally {
      executor.shutdownNow();
    }
  }
  // gets the player's request to purchase cards
  public static Optional<CardPurchases> requestCards(PlayerAPI player, Turn_State turnState) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<CardPurchases> future = executor.submit(()-> player.requestCards(turnState));
    try {
      return Optional.of(future.get(Constants.MOVE_TIMEOUT_SEC, TimeUnit.SECONDS));
    } catch (Exception e) {
      future.cancel(true);
      return Optional.empty();
    }
    finally {
      executor.shutdownNow();
    }
  }

  // informs the player if they have won or not
  public static Optional<Boolean> win(PlayerAPI player, boolean win) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<Boolean> future = executor.submit(()-> player.win(win), true);
    try {
      return Optional.of(future.get(Constants.MOVE_TIMEOUT_SEC, TimeUnit.SECONDS));
    }
    catch (Exception e) {
      future.cancel(true);
      return Optional.empty();
    }
    finally {
      executor.shutdownNow();
    }
  }
}
