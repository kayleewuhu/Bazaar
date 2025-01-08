package Player;

import Common.Data.CardPurchases;
import Common.Data.EquationTable;
import Common.Data.Exchanges;
import Common.Pair;
import Common.Turn_State;

// This Strategy maximizes the points that can be earned
// in a single turn.
public class MaximizePoints extends AbstractMaximize {

  public MaximizePoints(int maxExchangeSearchDepth) {
    super(maxExchangeSearchDepth);
  }

  public MaximizePoints() {
    super();
  }

  // Adds the given candidate to the best candidates list if it yields at least the max points so far
  void addCandidate(Turn_State turnState, Pair<Exchanges, CardPurchases> candidate, EquationTable equationTable) {
    if (this.bestCandidatesSoFar.isEmpty()) {
      this.bestCandidatesSoFar.add(candidate);
    } else if (!this.bestCandidatesSoFar.contains(candidate)) {
      // Calculate how many points the given candidates yields
      int candidateTotalPoints = this.candidateTotalPoints(turnState, candidate, equationTable);
      // Calculate how many points a candidate in the best candidates list yields
      Pair<Exchanges, CardPurchases> bestCandidate = this.bestCandidatesSoFar.get(0);
      int bestCandidateTotalPoints = this.candidateTotalPoints(turnState, bestCandidate, equationTable);
      // If the new candidates points are at least the max points so far
      if (candidateTotalPoints >= bestCandidateTotalPoints) {
        // If the new candidates points are greater than the max points so far, clear the list
        if (candidateTotalPoints > bestCandidateTotalPoints) {
          this.bestCandidatesSoFar.clear();
        }
        // Add candidate into best candidates so far
        this.bestCandidatesSoFar.add(candidate);
      }
    }
  }
}
