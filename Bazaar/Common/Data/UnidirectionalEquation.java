package Common.Data;

import java.util.List;

import Common.Pair;

/**
 * Represents an equation that goes from left to right
 */
public class UnidirectionalEquation {
    // input side of the equation
    private final PebbleCollection inputSide;
    // output side of the equation
    private final PebbleCollection outputSide;

    public UnidirectionalEquation(PebbleCollection inputSide, PebbleCollection outputSide) {
        this.inputSide = inputSide.getPebbleCollectionCopy();
        this.outputSide = outputSide.getPebbleCollectionCopy();
    }

    public PebbleCollection getInputSideCopy() {
        return this.inputSide.getPebbleCollectionCopy();
    }

    public PebbleCollection getOutputSideCopy() {
        return this.outputSide.getPebbleCollectionCopy();
    }

    // returns the symbol to be rendered for unidirectional equations
    public String getSymbol() {
        return "→";
    }

    // returns true if this unidirectional equation can be used as a trade between the given wallet and bank
    public boolean canExchange(PebbleCollection wallet, PebbleCollection bank) {
        return this.canWalletExchange(wallet) && this.canBankExchange(bank);
    }

    // can the given pebble collection do this trade?
    public boolean canWalletExchange(PebbleCollection wallet) {
        return this.inputSide.isSubset(wallet);
    }

    // can the given bank perform this trade?
    public boolean canBankExchange(PebbleCollection bank) {
        return this.outputSide.isSubset(bank);
    }

    // uses this equation onto the given wallet and bank
    public Pair<PebbleCollection, PebbleCollection> doExchange(PebbleCollection wallet, PebbleCollection bank) {
        PebbleCollection newWallet = wallet.getPebbleCollectionCopy();
        PebbleCollection newBank = bank.getPebbleCollectionCopy();
        newWallet = newWallet.removePebbles(this.inputSide);
        newWallet = newWallet.addPebbles(this.outputSide);
        newBank = newBank.removePebbles(this.outputSide);
        newBank = newBank.addPebbles(this.inputSide);
        return new Pair<>(newWallet, newBank);
    }

    // Returns a deep copy of this Unidirectional Equation
    public UnidirectionalEquation getUnidirectionalEquationCopy() {
        return new UnidirectionalEquation(this.inputSide, this.outputSide);
    }

    // is this equation less than the given equation?
    // less than meaning input sides as wallets are less than or output sides as wallets are less than
    public boolean lessThanGivenUnidirectionalEquation(UnidirectionalEquation other) {
        if (this.inputSide.lessThanGivenPebbleCollection(other.inputSide)) {
            return true;
        } else if (this.inputSide.equals(other.inputSide)) {
            return this.outputSide.lessThanGivenPebbleCollection(other.outputSide);
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UnidirectionalEquation otherEquation)) {
            return false;
        }
        return this.inputSide.equals(otherEquation.inputSide) && this.outputSide.equals(otherEquation.outputSide);
    }
}
