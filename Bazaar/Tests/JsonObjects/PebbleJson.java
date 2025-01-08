package Tests.JsonObjects;

import Common.Data.PebbleColor;

public class PebbleJson {
  private String color;

  public PebbleJson(PebbleColor pebble) {
    this.color = pebble.toString();
  }

  public PebbleJson(String color) {
    this.color = color;
  }

  public PebbleColor parseIntoObject() {
    return PebbleColor.fromString(this.color);
  }

  public String toString() {
    return this.color;
  }
}