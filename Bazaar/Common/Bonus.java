package Common;

// This enum represents all possible bonus modes for a game.
public enum Bonus {
    USA("RWB"),
    SEY("SEY");

    private final String bonusString;

    Bonus(String bonusString) {
        this.bonusString = bonusString;
    }

    public String getBonusString() {
        return this.bonusString;
    }

    // given the bonus as a string, returns the corresponding enum value
    // throws an exception if no enum is corresponding to the string that is passed in
    public static Bonus fromString(String bonusString) {
        for (Bonus bonus : Bonus.values()) {
            if (bonus.getBonusString().equalsIgnoreCase(bonusString)) {
                return bonus;
            }
        }
        throw new IllegalArgumentException("No enum constant with bonus string: " + bonusString);
    }
}
