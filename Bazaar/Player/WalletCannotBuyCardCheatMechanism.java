package Player;

import Common.Data.Card;
import Common.Data.CardPurchases;
import Common.Data.PebbleCollection;
import Common.Rule_Book;
import Common.Turn_State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This class represents a player that follows a given strategy
// however it will cheat by buying a card that the player's wallet cannot afford
// if possible.
public class WalletCannotBuyCardCheatMechanism extends Mechanism {
    public WalletCannotBuyCardCheatMechanism(String name, Strategy strategy) {
        super(name, strategy);
    }

    @Override
    public CardPurchases requestCards(Turn_State turnState) {
        List<Card> visible = turnState.visibleCards();
        PebbleCollection wallet = turnState.activePlayer().getWalletCopy();

        for (Card card : turnState.visibleCards()) {
            if (!Rule_Book.canBuyCard(visible, wallet, card)) {
                return new CardPurchases(new ArrayList<>(Arrays.asList(card)));
            }
        }
        return super.requestCards(turnState);
    }
}
