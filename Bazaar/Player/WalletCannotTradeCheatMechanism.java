package Player;

import Common.Data.*;
import Common.Turn_State;

import java.util.List;

// This class represents a player that follows a given strategy
// however it will cheat by making trades that the player's wallet cannot perform
// if possible.
public class WalletCannotTradeCheatMechanism extends Mechanism {
    public WalletCannotTradeCheatMechanism(String name, Strategy strategy) {
        super(name, strategy);
    }

    // gets the player's request to either draw a pebble or perform exchanges
    // will perform an illegal exchange that the wallet cannot perform if possible
    @Override
    public DrawPebbleOrExchanges requestPebbleOrExchanges(Turn_State turnState) {
        PebbleCollection wallet = turnState.activePlayer().getWalletCopy();
        for(BidirectionalEquation eq : equationTable.getAllEquationsCopy()) {
            List<UnidirectionalEquation> trades = eq.convertToUnidirectionalEquation();
            UnidirectionalEquation tradeLeft = trades.get(0);
            UnidirectionalEquation tradeRight = trades.get(1);
            if(!tradeLeft.canWalletExchange(wallet) || !tradeRight.canWalletExchange(wallet)) {
                return new Exchanges(trades);
            }
        }
        return super.requestPebbleOrExchanges(turnState);
    }
}
