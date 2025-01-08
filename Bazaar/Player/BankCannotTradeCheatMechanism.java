package Player;

import Common.Data.*;
import Common.Turn_State;

import java.util.List;

// This class represents a player that follows a given strategy
// however it will cheat by making trades that the bank cannot perform
// if possible.
public class BankCannotTradeCheatMechanism extends Mechanism {
    public BankCannotTradeCheatMechanism(String name, Strategy strategy) {
        super(name, strategy);
    }

    @Override
    public DrawPebbleOrExchanges requestPebbleOrExchanges(Turn_State turnState) {
        PebbleCollection bank = turnState.bankPebbles();
        for(BidirectionalEquation eq : equationTable.getAllEquationsCopy()) {
            List<UnidirectionalEquation> trades = eq.convertToUnidirectionalEquation();
            UnidirectionalEquation tradeLeft = trades.get(0);
            UnidirectionalEquation tradeRight = trades.get(1);
            if(!tradeLeft.canBankExchange(bank) || !tradeRight.canBankExchange(bank)) {
                return new Exchanges(trades);
            }
        }
        return super.requestPebbleOrExchanges(turnState);
    }
}
