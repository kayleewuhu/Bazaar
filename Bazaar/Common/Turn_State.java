package Common;

import java.util.List;

import Common.Data.Card;
import Common.Data.PebbleCollection;
import Referee.PlayerInfo;

// Represents the information known to a player during their turn
public record Turn_State(
        // the pebbles that are in the bank
        PebbleCollection bankPebbles,
        // the active player of this turn
        PlayerInfo activePlayer,
        // the scores of the remaining players not including the active player
        List<Integer> remainingPlayerScores,
        // the list of cards that are visible
        List<Card> visibleCards
) {}
