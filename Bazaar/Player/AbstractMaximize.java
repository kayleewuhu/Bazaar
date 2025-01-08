package Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import Common.Data.Card;
import Common.Data.CardPurchases;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Data.PebbleCollection;
import Common.Pair;
import Common.Rule_Book;
import Common.Turn_State;
import Common.Data.UnidirectionalEquation;

// This abstract class represents a Strategy that performs
// a move that maximizes a specific attribute of the player.
public abstract class AbstractMaximize implements Strategy {
  // Moves that are the best based on the Strategy's maximization
  List<Pair<Exchanges, CardPurchases>> bestCandidatesSoFar = new ArrayList<>();
  // maximum number of exchanges that can be performed in a single turn
  protected final int maxExchangeSearchDepth;

  public AbstractMaximize(int maxExchangeSearchDepth) {
    this.maxExchangeSearchDepth = maxExchangeSearchDepth;
  }

  public AbstractMaximize() {
    this(4);
  }

  // should this strategy draw a pebble and forgo exchanges
  @Override
  public boolean shouldDrawPebble(Turn_State turnState, EquationTable equationTable) {
    PebbleCollection wallet = turnState.activePlayer().getWalletCopy();
    PebbleCollection bank = turnState.bankPebbles();
    List<UnidirectionalEquation> availableExchanges = equationTable.filter(wallet, bank);
    return availableExchanges.isEmpty();
  }

  // what exchanges and card purchases to do
  @Override
  public Pair<Exchanges, CardPurchases> whatExchangesAndCardPurchases(Turn_State turnState, EquationTable equationTable) {
    // Clear bestCandidatesSoFar list
    this.bestCandidatesSoFar.clear();
    // Generate candidates
    this.addExchangesAndCardPurchasesCandidates(turnState, equationTable);
    // Tie-breaking
    this.exchangesAndCardPurchasesTieBreak(turnState, equationTable);
    // If bestCandidates doesn't have exactly 1 candidate, throw an error
    if (this.bestCandidatesSoFar.size() != 1) {
      throw new IllegalStateException("Tie-breaking failed.");
    }
    // Return best candidate
    return this.bestCandidatesSoFar.get(0);
  }

  @Override
  public CardPurchases whatCardPurchases(Turn_State turnState, EquationTable equationTable) {
    // Clear bestCandidatesSoFar list
    this.bestCandidatesSoFar.clear();
    // Generate candidates
    this.addCardPurchasesCandidates(turnState, equationTable);
    // Tie-breaking
    this.cardPurchasesTieBreak(turnState, equationTable);
    // If bestCandidates doesn't have exactly 1 candidate, throw an error
    if (this.bestCandidatesSoFar.size() != 1) {
      throw new IllegalStateException("Tie-breaking failed.");
    }
    // Return best candidate
    return this.bestCandidatesSoFar.get(0).second;
  }

  // performs tie breaking among candidates for exchange
  // and card purchasing
  private void exchangesAndCardPurchasesTieBreak(Turn_State turnState, EquationTable equationTable) {
    this.keepCandidatesWithLeastAmountOfTrades();
    this.cardPurchasesTieBreak(turnState, equationTable);
    this.keepCandidatesWithSmallestPebbleExchanges();
  }

  // performs tie breaking among candidates for no exchanges but card purchasing
  private void cardPurchasesTieBreak(Turn_State turnState, EquationTable equationTable) {
    this.keepCandidatesWithHighestNumberOfPoints(turnState, equationTable);
    this.keepCandidatesWithLargestNumberOfRemainingPebbles(turnState, equationTable);
    this.keepCandidatesWithSmallestWallet(turnState, equationTable);
    this.keepCandidatesWithSmallestSequenceOfCards();
  }

  // exchange-and-purchase step 1:
  // from equivalent candidates, pick the ones that need the smallest number of trades
  private void keepCandidatesWithLeastAmountOfTrades() {
    int minAmountOfTrades = this.bestCandidatesSoFar.stream()
            .mapToInt(candidate -> candidate.first.totalTrades()).min().orElse(0);
    this.bestCandidatesSoFar = this.bestCandidatesSoFar.stream()
            .filter(candidate -> candidate.first.totalTrades() == minAmountOfTrades).collect(Collectors.toList());
  }

  // exchange-and-purchase step 3:
  // pick the one that uses the smallest pebble-exchanges
  private void keepCandidatesWithSmallestPebbleExchanges() {
    List<UnidirectionalEquation> smallestPebbleExchange = this.bestCandidatesSoFar.get(0).first.getSequenceOfExchangesCopy();
    List<Pair<Exchanges, CardPurchases>> candidatesWithSmallestPebbleExchange = new ArrayList<>();
    for (Pair<Exchanges, CardPurchases> candidate : this.bestCandidatesSoFar) {
      List<UnidirectionalEquation> newCandidateSequenceOfExchanges = candidate.first.getSequenceOfExchangesCopy();
      if (exchangeSequenceLessThanOtherExchangeSequence(newCandidateSequenceOfExchanges, smallestPebbleExchange)
              || newCandidateSequenceOfExchanges.equals(smallestPebbleExchange)) {
        if (exchangeSequenceLessThanOtherExchangeSequence(newCandidateSequenceOfExchanges, smallestPebbleExchange)) {
          candidatesWithSmallestPebbleExchange.clear();
        }
        candidatesWithSmallestPebbleExchange.add(candidate);
        smallestPebbleExchange = newCandidateSequenceOfExchanges;
      }
    }
    this.bestCandidatesSoFar = candidatesWithSmallestPebbleExchange;
  }

  // the first given sequence of exchanges is less than the second given sequence of exchanges
  // less than meaning the size is smaller or the equations in the first list are smaller
  // than the corresponding equations in the second
  private boolean exchangeSequenceLessThanOtherExchangeSequence(List<UnidirectionalEquation> first,
                                                                List<UnidirectionalEquation> second)
  {
    if (first.size() < second.size()) {
      return true;
    }
    for (int i = 0; i < first.size(); i++) {
      if (first.get(i).lessThanGivenUnidirectionalEquation(second.get(i))) {
        return true;
      }
      if (second.get(i).lessThanGivenUnidirectionalEquation(first.get(i))) {
        return false;
      }
    }
    return false;
  }

  // card-purchase step 1:
  // The search process eliminates any candidates that do not yield the highest number of points
  private void keepCandidatesWithHighestNumberOfPoints(Turn_State turnState, EquationTable equationTable) {
    int maxNumberOfPoints = 0;
    List<Pair<Exchanges, CardPurchases>> candidatesWithMaxPoints = new ArrayList<>();
    for (Pair<Exchanges, CardPurchases> candidate : this.bestCandidatesSoFar) {
      int newCandidateTotalPoints = this.candidateTotalPoints(turnState, candidate, equationTable);
      if (newCandidateTotalPoints >= maxNumberOfPoints) {
        if (newCandidateTotalPoints > maxNumberOfPoints) {
          candidatesWithMaxPoints.clear();
        }
        candidatesWithMaxPoints.add(candidate);
        maxNumberOfPoints = newCandidateTotalPoints;
      }
    }
    this.bestCandidatesSoFar = candidatesWithMaxPoints;
  }

  // Calculates how many points a candidate yields
  protected int candidateTotalPoints(Turn_State turnState, Pair<Exchanges, CardPurchases> candidate, EquationTable equationTable) {
    Exchanges candidateExchanges = candidate.first;
    CardPurchases candidateCardPurchases = candidate.second;
    // Get the new wallet after all exchanges have been applied
    PebbleCollection candidateWalletAfterExchanges =
            candidateExchanges.attemptAction(turnState, equationTable).get().first;
    // Calculate the total points the card purchases are worth given the updated wallet
    return Rule_Book.totalPointsFromCardPurchases(candidateCardPurchases, candidateWalletAfterExchanges);
  }

  // card-purchase step 2:
  // The search process eliminates any candidates that do not yield the largest number of remaining pebbles in the player’s wallet
  private void keepCandidatesWithLargestNumberOfRemainingPebbles(Turn_State turnState, EquationTable equationTable) {
    int largestNumberOfRemainingPebbles = 0;
    List<Pair<Exchanges, CardPurchases>> candidatesWithLargestRemainingPebbles = new ArrayList<>();
    for (Pair<Exchanges, CardPurchases> candidate : this.bestCandidatesSoFar) {
      PebbleCollection newCandidateWallet = candidate.first.attemptAction(turnState, equationTable).get().first;
      int newCandidateRemainingPebbles = newCandidateWallet.getTotalNumberOfPebbles();
      if (newCandidateRemainingPebbles >= largestNumberOfRemainingPebbles) {
        if (newCandidateRemainingPebbles > largestNumberOfRemainingPebbles) {
          candidatesWithLargestRemainingPebbles.clear();
        }
        candidatesWithLargestRemainingPebbles.add(candidate);
        largestNumberOfRemainingPebbles = newCandidateRemainingPebbles;
      }
    }
    this.bestCandidatesSoFar = candidatesWithLargestRemainingPebbles;
  }

  // card-purchase step 3:
  // The search process picks the candidate with the smallest wallet, unless all candidates have equal wallets
  private void keepCandidatesWithSmallestWallet(Turn_State turnState, EquationTable equationTable) {
    PebbleCollection smallestWallet = this.bestCandidatesSoFar.get(0).first
            .attemptAction(turnState, equationTable).get().first;
    List<Pair<Exchanges, CardPurchases>> candidatesWithSmallestWallet = new ArrayList<>();
    for (Pair<Exchanges, CardPurchases> candidate : this.bestCandidatesSoFar) {
      PebbleCollection newCandidateWallet = candidate.first.attemptAction(turnState, equationTable).get().first;
      if (newCandidateWallet.lessThanGivenPebbleCollection(smallestWallet) || newCandidateWallet.equals(smallestWallet)) {
        if (newCandidateWallet.lessThanGivenPebbleCollection(smallestWallet)) {
          candidatesWithSmallestWallet.clear();
        }
        candidatesWithSmallestWallet.add(candidate);
        smallestWallet = newCandidateWallet;
      }
    }
    this.bestCandidatesSoFar = candidatesWithSmallestWallet;
  }

  // card-purchase step 4:
  // The search process picks the candidate with the smallest sequence of cards, unless all candidates have equal sequences of cards
  private void keepCandidatesWithSmallestSequenceOfCards() {
    List<Card> smallestSequenceOfCards = this.bestCandidatesSoFar.get(0).second.getSequenceOfCardPurchasesCopy();
    List<Pair<Exchanges, CardPurchases>> candidatesWithSmallestSequenceOfCards = new ArrayList<>();
    for (Pair<Exchanges, CardPurchases> candidate : this.bestCandidatesSoFar) {
      List<Card> newCandidateSequenceOfCards = candidate.second.getSequenceOfCardPurchasesCopy();
      if (cardSequenceLessThanOtherCardSequence(newCandidateSequenceOfCards, smallestSequenceOfCards)
              || newCandidateSequenceOfCards.equals(smallestSequenceOfCards)) {
        if (cardSequenceLessThanOtherCardSequence(newCandidateSequenceOfCards, smallestSequenceOfCards)) {
          candidatesWithSmallestSequenceOfCards.clear();
        }
        candidatesWithSmallestSequenceOfCards.add(candidate);
        smallestSequenceOfCards = newCandidateSequenceOfCards;
      }
    }
    this.bestCandidatesSoFar = candidatesWithSmallestSequenceOfCards;
  }

  // the first given sequence of cards is less than the second given sequence of cards
  // less than meaning the size is smaller or the cards in the first list are smaller
  // than the corresponding cards in the second
  private boolean cardSequenceLessThanOtherCardSequence(List<Card> first, List<Card> second) {
    if (first.size() < second.size()) {
      return true;
    }
    for (int i = 0; i < first.size(); i++) {
      if (first.get(i).lessThanGivenCard(second.get(i))) {
        return true;
      }
      if (second.get(i).lessThanGivenCard(first.get(i))) {
        return false;
      }
    }
    return false;
  }

  // Add all candidates where we do exchanges and card purchases
  private void addExchangesAndCardPurchasesCandidates(Turn_State turnState, EquationTable equationTable) {
    this.exploreExchangeTree(turnState, new Pair<>(new Exchanges(), new CardPurchases()),
            turnState.activePlayer().getWalletCopy(), turnState.bankPebbles(), 0, equationTable);
  }

  // Add all candidates where we just purchase cards
  private void addCardPurchasesCandidates(Turn_State turnState, EquationTable equationTable) {
    this.exploreCardTree(turnState, new Pair<>(new Exchanges(), new CardPurchases()),
            turnState.activePlayer().getWalletCopy(), turnState.visibleCards(), equationTable);
  }

  // Explores all branch options for equation exchanges
  private void exploreExchangeTree(Turn_State turnState, Pair<Exchanges, CardPurchases> currentCandidate,
                                   PebbleCollection currentWallet, PebbleCollection currentBank, int depth,
                                   EquationTable equationTable) {
    Exchanges currentExchanges = currentCandidate.first;
    CardPurchases currentCardPurchases = currentCandidate.second;
    // At each branch, explore a Card tree that starts at this point
    Pair<Exchanges, CardPurchases> currentCardTreeCandidate = new Pair<>(currentExchanges.getExchangesCopy(),
            currentCardPurchases.getCardPurchasesCopy());
    this.exploreCardTree(turnState, currentCardTreeCandidate, currentWallet.getPebbleCollectionCopy(),
            turnState.visibleCards().stream().map(Card::getCardCopy).collect(Collectors.toList()), equationTable);
    // If we've reached the maxDepth, do nothing
    if (depth >= this.maxExchangeSearchDepth) {
      return;
    }
    // Now loop through all the available exchanges at this point and explore their branches
    for (UnidirectionalEquation equation : equationTable.filter(currentWallet, currentBank)) {
      // Update the current candidate
      List<UnidirectionalEquation> updatedListOfExchanges = currentExchanges.getSequenceOfExchangesCopy();
      updatedListOfExchanges.add(equation);
      Exchanges updatedExchanges = new Exchanges(updatedListOfExchanges);
      Pair<Exchanges, CardPurchases> updatedCandidate = new Pair<>(updatedExchanges, currentCardPurchases);
      // Update the wallet and bank
      Pair<PebbleCollection, PebbleCollection> updatedWalletAndUpdatedBank = equation.doExchange(currentWallet, currentBank);
      PebbleCollection updatedWallet = updatedWalletAndUpdatedBank.first;
      PebbleCollection updatedBank = updatedWalletAndUpdatedBank.second;
      this.exploreExchangeTree(turnState, updatedCandidate, updatedWallet, updatedBank, depth+1, equationTable);
    }
  }

  // Explores all branch options for card purchases
  private void exploreCardTree(Turn_State turnState, Pair<Exchanges, CardPurchases> currentCandidate, PebbleCollection currentWallet,
                               List<Card> currentVisibleCards, EquationTable equationTable) {
    // At each branch, add the current candidate as a possible candidate
    this.addCandidate(turnState, currentCandidate, equationTable);

    Exchanges currentExchanges = currentCandidate.first;
    CardPurchases currentCardPurchases = currentCandidate.second;
    // Now loop through all the available cards at this point and explore their branches
    for (Card card : currentVisibleCards) {
      // Check to see if current card can be bought
      if (Rule_Book.canBuyCard(currentVisibleCards, currentWallet, card)) {
        // Update the current candidate
        List<Card> updatedListOfCards = currentCardPurchases.getSequenceOfCardPurchasesCopy();
        updatedListOfCards.add(card);
        CardPurchases updatedCardPurchases = new CardPurchases(updatedListOfCards);
        Pair<Exchanges, CardPurchases> updatedCandidate = new Pair<>(currentExchanges, updatedCardPurchases);
        // Update the wallet and visible cards for going down this branch
        PebbleCollection updatedWallet = currentWallet.getPebbleCollectionCopy();
        updatedWallet = updatedWallet.removePebbles(card.getCardPebblesCopy());
        List<Card> newVisibleCards = new ArrayList<>(currentVisibleCards);
        newVisibleCards.remove(card);
        this.exploreCardTree(turnState, updatedCandidate, updatedWallet, newVisibleCards, equationTable);
      }
    }
  }

  // compares the given candidate to candidates in this Strategy's list of
  // best candidates so far to determine:
  //    list should be cleared and given candidate added if it beats all
  //    given candidate should be added if it ties with all based on greedy goal
  //    given candidate doesn't get added because it's worse than all
  // this keeps the bestCandidatesSoFar list to always contain the best candidate or candidates
  abstract void addCandidate(Turn_State turnState, Pair<Exchanges, CardPurchases> candidate, EquationTable equationTable);
}
