package Common;
import Common.Data.Card;
import java.util.*;

// This interface represents the mode of the game. Every game will have a mode.
public interface IGameMode {
    // calculates the bonus a player gets at the end of the game based on a list of purchased cards
    public int calculateBonus(List<Card> cards);
}
