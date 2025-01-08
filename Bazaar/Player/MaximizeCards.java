package Player;

import Common.Data.CardPurchases;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Pair;
import Common.Turn_State;

// This Strategy maximizes the amount of cards that can be bought
// in a single turn.
public class MaximizeCards extends AbstractMaximize {

  public MaximizeCards(int maxExchangeSearchDepth) {
    super(maxExchangeSearchDepth);
  }

  public MaximizeCards() {
    super();
  }

  // Adds the given candidate to the best candidates list if it yields at least the max points so far
  void addCandidate(Turn_State turnState, Pair<Exchanges, CardPurchases> candidate, EquationTable equationTable) {
    if (this.bestCandidatesSoFar.isEmpty()) {
      this.bestCandidatesSoFar.add(candidate);
    } else if (!this.bestCandidatesSoFar.contains(candidate)) {
      int candidateTotalCards = candidate.second.totalCards();
      int bestCandidateTotalCard = this.bestCandidatesSoFar.get(0).second.totalCards();
      // If the new candidates points are at least the max points so far
      if (candidateTotalCards >= bestCandidateTotalCard) {
        // If the new candidates points are greater than the max points so far, clear the list
        if (candidateTotalCards > bestCandidateTotalCard) {
          this.bestCandidatesSoFar.clear();
        }
        // Add candidate into best candidates so far
        this.bestCandidatesSoFar.add(candidate);
      }
    }
  }
}