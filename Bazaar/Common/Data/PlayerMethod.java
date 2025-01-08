package Common.Data;

// This enum represents all possible methods of a player that can be called.
public enum PlayerMethod {
    SETUP("setup"),
    REQUEST_PEBBLE_OR_TRADES("request-pebble-or-trades"),
    REQUEST_CARDS("request-cards"),
    WIN("win");

    private final String methodString;

    PlayerMethod(String methodString) {
        this.methodString = methodString;
    }

    public String getMethodString() {
        return this.methodString;
    }

    // given the method's name as a string, returns the corresponding enum value
    // throws an exception if no enum is corresponding to the string that is passed in
    public static PlayerMethod fromString(String methodString) {
        for (PlayerMethod method : PlayerMethod.values()) {
            if (method.getMethodString().equalsIgnoreCase(methodString)) {
                return method;
            }
        }
        throw new IllegalArgumentException("No enum constant with method string: " + methodString);
    }
}
