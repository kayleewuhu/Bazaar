package Tests.JsonObjects;

import java.util.ArrayList;
import java.util.List;

import Referee.PlayerInfo;

public class PlayerJson {
  private List<String> wallet;
  private int score;

  public PlayerJson(PlayerInfo player) {
    this.wallet = new PebbleCollectionJson(player.getWalletCopy()).toListOfString();
    this.score = player.getScore();
  }

  public PlayerInfo parseIntoObject() {
    PebbleCollectionJson pebbles = new PebbleCollectionJson(this.wallet);
    return new PlayerInfo(pebbles.parseIntoObject(), this.score, "unknown", null, new ArrayList<>());
  }
}
