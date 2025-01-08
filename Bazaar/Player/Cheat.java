package Player;

// This enum represents all possible cheats for a player.
public enum Cheat {
    NO_EQUATION("use-non-existent-equation"),
    BANK_NO_TRADE("bank-cannot-trade"),
    WALLET_NO_TRADE("wallet-cannot-trade"),
    NO_CARD("buy-unavailable-card"),
    WALLET_NO_CARD("wallet-cannot-buy-card");

    private final String cheatString;

    Cheat(String cheatString) {
        this.cheatString = cheatString;
    }

    public String getCheatString() {
        return this.cheatString;
    }

    // given the cheat as a string, returns the corresponding enum value
    // throws an exception if no enum is corresponding to the string that is passed in
    public static Cheat fromString(String cheatString) {
        for (Cheat cheat : Cheat.values()) {
            if (cheat.getCheatString().equalsIgnoreCase(cheatString)) {
                return cheat;
            }
        }
        throw new IllegalArgumentException("No enum constant with cheat string: " + cheatString);
    }
}
