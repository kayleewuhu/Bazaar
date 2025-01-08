package Common.Data;

import java.util.Optional;

import Common.Pair;
import Common.Turn_State;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

// This interface represents the first decision the player must make during a move.
// Currently, it supports either drawing a pebble, or a list of pebble exchanges.
public interface DrawPebbleOrExchanges {
  // attempts to do this action, if it is invalid, it returns an empty Optional, else it performs the action and
  // returns the resulting player wallet and bank
  Optional<Pair<PebbleCollection, PebbleCollection>> attemptAction(Turn_State turnState, EquationTable equationTable);
  // transforms the object into the JSON representation
  JsonElement toJson(Gson gson);
  // is the given object equals to this DrawPebbleOrExchanges object?
  @Override
  boolean equals(Object obj);
}
