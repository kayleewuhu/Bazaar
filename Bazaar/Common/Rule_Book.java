package Common;

import java.util.List;

import Common.Data.Card;
import Common.Data.CardPurchases;
import Common.Data.EquationTable;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;
import Common.Data.UnidirectionalEquation;
import Referee.Game_State;
import Referee.PlayerInfo;

// This class represents the rules to the game. It handles scoring, legality of moves, and
// termination of the game.
public class Rule_Book {
  // is the pebble drawing or exchanges move valid?
  public static boolean canActivePlayerDoPebbleDrawingOrExchanges(Turn_State turnState,
                                                      DrawPebbleOrExchanges action,
                                                      EquationTable equations) {
    return action.attemptAction(turnState, equations).isPresent();
  }

  // is the card purchases move valid?
  public static boolean canActivePlayerDoCardPurchases(Turn_State turnState, CardPurchases cardsToBeBought) {
    return cardsToBeBought.attemptAction(turnState).isPresent();
  }

  // can the given exchange be performed with the given wallet, bank, and equations?
  public static boolean canDoExchange(UnidirectionalEquation exchange,
                                      PebbleCollection wallet, PebbleCollection bank, EquationTable equationTable) {
    return equationTable.contains(exchange) &&
            exchange.getInputSideCopy().isSubset(wallet) && exchange.getOutputSideCopy().isSubset(bank);
  }

  // can the given wallet buy the given card in the game?
  public static boolean canBuyCard(List<Card> visibleCards, PebbleCollection wallet, Card card) {
    return visibleCards.contains(card) && card.getCardPebblesCopy().isSubset(wallet);
  }

  // returns how many points this sequence of card purchases is worth given a wallet
  public static int totalPointsFromCardPurchases(CardPurchases cardPurchases, PebbleCollection wallet) {
    return cardPurchases.totalPointsWorth(wallet);
  }

  // returns the amount of points yielded if the given card is bought with the given wallet?
  public static int pointsYieldedIfCardBought(Card card, PebbleCollection wallet) {
    int pebblesLeftAfterBuying = wallet.getTotalNumberOfPebbles() -
            card.getCardPebblesCopy().getTotalNumberOfPebbles();
    boolean cardHasSmiley = card.hasSmiley();
    if (pebblesLeftAfterBuying >= 3) {
      return cardHasSmiley ? Constants.POINTS_FROM_SMILEY_CARD_AND_3_OR_MORE_PEBBLES_LEFT
              : Constants.POINTS_FROM_PLAIN_CARD_AND_3_OR_MORE_PEBBLES_LEFT;
    } else if (pebblesLeftAfterBuying == 2) {
      return cardHasSmiley ? Constants.POINTS_FROM_SMILEY_CARD_AND_2_LEFT
              : Constants.POINTS_FROM_PLAIN_CARD_AND_2_LEFT;
    } else if (pebblesLeftAfterBuying == 1) {
      return cardHasSmiley ? Constants.POINTS_FROM_SMILEY_CARD_AND_1_LEFT
              : Constants.POINTS_FROM_PLAIN_CARD_AND_1_LEFT;
    } else {
      return cardHasSmiley ? Constants.POINTS_FROM_SMILEY_CARD_AND_0_LEFT
              : Constants.POINTS_FROM_PLAIN_CARD_AND_0_LEFT;
    }
  }

  // returns the players that are winning in the given game state
  public static List<PlayerInfo> currentWinningPlayers(Game_State gameState) {
    return gameState.playersWithMostPoints();
  }

  // is the game, represented by the given game state, over?
  public static boolean isTheGameOver(Game_State gameState) {
    return (Rule_Book.zeroPlayersLeft(gameState.getPlayersAsList())
            || Rule_Book.activePlayerWinByPoints(gameState.getActivePlayerCopy())
            || gameState.getCards().noMoreCards()
            || (gameState.getBank().outOfPebbles()
            && !Rule_Book.canAnyPlayerBuyAnyVisibleCard(gameState.getPlayersAsList(),
            gameState.getCards().getVisibleCardsCopy())));
  }

  // is there 0 players left in the game?
  private static boolean zeroPlayersLeft(List<PlayerInfo> players) {
    return players.isEmpty();
  }

  // does the active player have enough points to win?
  private static boolean activePlayerWinByPoints(PlayerInfo activePlayer) {
    return activePlayer.getScore() >= Constants.POINTS_TO_WIN;
  }

  // can any player in the game buy any visible card on the board
  private static boolean canAnyPlayerBuyAnyVisibleCard(List<PlayerInfo> players, List<Card> visibleCards) {
    for (PlayerInfo player : players) {
      for (Card card : visibleCards) {
        if (Rule_Book.canBuyCard(visibleCards, player.getWalletCopy(), card)) {
          return true;
        }
      }
    }
    return false;
  }

  // calculates bonus points for the given player in the given game mode
  public static int calculateBonus(IGameMode gameMode, PlayerInfo player) {
    return gameMode.calculateBonus(player.getPurchasedCards());
  }
}