package Player;

import Common.Data.BidirectionalEquation;
import Common.Data.DrawPebbleOrExchanges;
import Common.Data.Exchanges;
import Common.Data.UnidirectionalEquation;
import Common.Turn_State;

import java.util.List;
import java.util.Random;

// This class represents a player that follows a given strategy
// however it will cheat by using a nonexistent equation for exchanges
// if possible.
public class NonExistentEquationCheatMechanism extends Mechanism {
    public NonExistentEquationCheatMechanism(String name, Strategy strategy) {
        super(name, strategy);
    }

    @Override
    public DrawPebbleOrExchanges requestPebbleOrExchanges(Turn_State turnState) {
        while (true) {
            BidirectionalEquation eq = new BidirectionalEquation(new Random());
            List<UnidirectionalEquation> trades = eq.convertToUnidirectionalEquation();
            if(!this.equationTable.contains(trades.get(0))) {
                return new Exchanges(trades);
            }
        }
    }
}
