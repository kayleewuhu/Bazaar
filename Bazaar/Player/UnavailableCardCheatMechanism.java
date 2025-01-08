package Player;

import Common.Data.Card;
import Common.Data.CardPurchases;
import Common.Turn_State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// This class represents a player that follows a given strategy
// however it will cheat by buying an unavailable/nonexistent card
// if possible.
public class UnavailableCardCheatMechanism extends Mechanism {
    public UnavailableCardCheatMechanism(String name, Strategy strategy) {
        super(name, strategy);
    }

    @Override
    public CardPurchases requestCards(Turn_State turnState) {
        while (true) {
            Card card = new Card(new Random());
            if(!turnState.visibleCards().contains(card)) {
                return new CardPurchases(new ArrayList<>(Arrays.asList(card)));
            }
        }
    }
}
