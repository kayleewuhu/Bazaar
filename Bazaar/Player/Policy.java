package Player;

// This enum represents all possible policies/strategies for a player.
public enum Policy {
    MAX_POINTS("purchase-points"),
    MAX_CARDS("purchase-size");

    private final String policyString;

    Policy(String policyString) {
        this.policyString = policyString;
    }

    public String getPolicyString() {
        return this.policyString;
    }

    // given the policy as a string, returns the corresponding enum value
    // throws an exception if no enum is corresponding to the string that is passed in
    public static Policy fromString(String policyString) {
        policyString = policyString.trim();

        for (Policy policy : Policy.values()) {
            if (policy.getPolicyString().equalsIgnoreCase(policyString)) {
                return policy;
            }
        }
        throw new IllegalArgumentException("No enum constant with policy string: " + policyString);
    }
}
