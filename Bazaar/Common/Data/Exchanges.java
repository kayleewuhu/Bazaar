package Common.Data;

import Common.Json.ObjectJsonSerializer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import Common.Pair;
import Common.Rule_Book;
import Common.Turn_State;
import Tests.JsonObjects.ExchangesJson;
import com.google.gson.JsonElement;

// This class represents a player's choice to use a sequence of exchanges that come from the game's
// equations.
public class Exchanges implements DrawPebbleOrExchanges {
  // the exchanges of pebbles that occur (in the order of the list)
  List<UnidirectionalEquation> sequenceOfExchanges;

  public Exchanges() {
    this.sequenceOfExchanges = new ArrayList<>();
  }

  public Exchanges(List<UnidirectionalEquation> exchanges) {
    this.sequenceOfExchanges = exchanges.stream()
            .map(UnidirectionalEquation::getUnidirectionalEquationCopy).collect(Collectors.toList());
  }

  // returns a copy of this sequence of equations
  public List<UnidirectionalEquation> getSequenceOfExchangesCopy() {
    return sequenceOfExchanges.stream()
            .map(UnidirectionalEquation::getUnidirectionalEquationCopy).collect(Collectors.toList());
  }

  // returns a copy of this Exchanges
  public Exchanges getExchangesCopy() {
    return new Exchanges(this.getSequenceOfExchangesCopy());
  }

  // returns how many exchanges/trades are in this Exchanges
  public int totalTrades() {
    return this.sequenceOfExchanges.size();
  }

  // attempts this list of exchanges action on the given turn state and list of equations
  // returns the resulting player's wallet and bank if valid, otherwise empty optional
  @Override
  public Optional<Pair<PebbleCollection, PebbleCollection>> attemptAction(Turn_State turnState, EquationTable equationTable) {
    PebbleCollection newWallet = turnState.activePlayer().getWalletCopy();
    PebbleCollection newBank = turnState.bankPebbles().getPebbleCollectionCopy();
    for (UnidirectionalEquation exchange : this.sequenceOfExchanges) {
      // Check to see if it is possible to use the exchange
      if (!Rule_Book.canDoExchange(exchange, newWallet, newBank, equationTable)) {
        return Optional.empty();
      }
      // Do the exchange and update wallet and bank
      Pair<PebbleCollection, PebbleCollection> newWalletAndBank = exchange.doExchange(newWallet, newBank);
      newWallet = newWalletAndBank.first;
      newBank = newWalletAndBank.second;
    }
    return Optional.of(new Pair<>(newWallet, newBank));
  }

  @Override
  public JsonElement toJson(Gson gson) {
    return ObjectJsonSerializer.serializeExchanges(this);
  }

  // is this exchanges equal to the given object?
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Exchanges otherExchanges)) {
      return false;
    }
    return this.sequenceOfExchanges.equals(otherExchanges.sequenceOfExchanges);
  }
}
