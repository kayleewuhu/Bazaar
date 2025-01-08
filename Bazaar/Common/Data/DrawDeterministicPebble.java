package Common.Data;

import com.google.gson.Gson;

import java.util.Optional;

import Common.Pair;
import Common.Turn_State;
import com.google.gson.JsonElement;

// This class represents action of drawing a pebble deterministically.
public class DrawDeterministicPebble implements DrawPebbleOrExchanges {

  // attempts to draw a pebble from the given turn state's bank deterministically
  // and returns the updated active player's wallet and bank if valid, otherwise empty optional
  @Override
  public Optional<Pair<PebbleCollection, PebbleCollection>> attemptAction(Turn_State turnState, EquationTable equationTable) {
    if (turnState.bankPebbles().outOfPebbles()) {
      return Optional.empty();
    } else {
      PebbleCollection curWallet = turnState.activePlayer().getWalletCopy();
      PebbleCollection curBank = turnState.bankPebbles().getPebbleCollectionCopy();
      Pair<PebbleColor, PebbleCollection> randomPebbleAndNewBank = curBank.drawDeterministicPebble();
      PebbleColor randomPebble = randomPebbleAndNewBank.first;
      PebbleCollection newBank = randomPebbleAndNewBank.second;
      PebbleCollection newWallet = curWallet.updatePebbleQuantity(randomPebble, 1);
      return Optional.of(new Pair<>(newWallet, newBank));
    }
  }

  @Override
  public JsonElement toJson(Gson gson) {
    return gson.toJsonTree(false);
  }

  // is this draw pebble equal to the given object?
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    return obj instanceof DrawDeterministicPebble;
  }
}
