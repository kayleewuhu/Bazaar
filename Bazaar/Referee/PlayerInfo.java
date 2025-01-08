package Referee;

import Common.Data.Card;
import Common.Data.PebbleCollection;
import Common.Data.PebbleColor;
import Player.PlayerAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data of a player for a Referee to keep track of
 * Currently just a data object only for Referee
 */
public class PlayerInfo {
    // player's wallet, how many pebbles can be given
    private final PebbleCollection wallet;
    // player's current score
    private final int score;
    // player's name
    private final String name;
    // player's reachability/way of connecting
    private final PlayerAPI playerConnection;
    // the cards that the player has bought
    private final List<Card> cardsPurchased;

    public PlayerInfo() {
        this(new PebbleCollection(), 0, "unknown", null, new ArrayList<>());
    }

    public PlayerInfo(PebbleCollection wallet) {
        this(wallet, 0, "unknown", null, new ArrayList<>());
    }

    public PlayerInfo(String name, PlayerAPI player) {
        this(new PebbleCollection(), 0, name, player, new ArrayList<>());
    }

    public PlayerInfo(PebbleCollection wallet, int score, String name, PlayerAPI player, List<Card> cardsPurchased) {
        this.wallet = wallet.getPebbleCollectionCopy();
        this.score = score;
        this.name = name;
        this.playerConnection = player;
        this.cardsPurchased = cardsPurchased;
    }

    // returns the wallet of the player
    public PebbleCollection getWalletCopy() {
        return this.wallet.getPebbleCollectionCopy();
    }

    // returns a copy of this player
    public PlayerInfo getPlayerInfoCopy() {
        return new PlayerInfo(this.wallet, this.score, this.name, this.playerConnection, this.cardsPurchased);
    }

    // returns this player's score
    public int getScore() {
        return this.score;
    }

    // returns this player's name
    public String getName() {
        return this.name;
    }

    // returns this player's mechanism
    public PlayerAPI getPlayer() {
        return this.playerConnection;
    }

    // returns this player info with the wallet set to the given wallet
    public PlayerInfo setWalletTo(PebbleCollection newWallet) {
        return new PlayerInfo(newWallet, this.score, this.name, this.playerConnection, this.cardsPurchased);
    }

    // adds the given points to the player's current score
    public PlayerInfo addPointsToScore(int pointsEarned) {
        return new PlayerInfo(this.wallet, this.score + pointsEarned, this.name, this.playerConnection, this.cardsPurchased);
    }

    // returns a new player info that is a copy of this player but the PlayerAPI is filled in with the given PlayerAPI
    public PlayerInfo connectWithPlayerAPI(PlayerAPI playerConnection) {
        return new PlayerInfo(this.wallet, this.score, PlayerProxy.name(playerConnection), playerConnection, this.cardsPurchased);
    }

    // is this player info equal to the given object?
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PlayerInfo otherPlayerInfo)) {
            return false;
        }
        return this.name.equals(otherPlayerInfo.name);
    }

    // adds the given cards to this player's purchased cards
    public PlayerInfo addCardsToPurchased(List<Card> cards) {
        List<Card> newCards = new ArrayList<>(this.cardsPurchased);
        newCards.addAll(cards);
        return new PlayerInfo(this.wallet, this.score, this.name, this.playerConnection, newCards);
    }

    // returns a copy of the player's purchased cards
    public List<Card> getPurchasedCards() {
        return this.cardsPurchased.stream().map(Card::getCardCopy).collect(Collectors.toList());
    }
}
