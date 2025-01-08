package Common;

import Common.Data.Card;

import java.util.List;

// This class represents the default game mode, where a player will not earn any bonus points.
public class DefaultGameMode implements IGameMode{
    // calculates the bonus points the given cards will earn in this game mode
    public int calculateBonus(List<Card> cards) {
        return Constants.DEFAULT_BONUS_POINTS;
    }
}
