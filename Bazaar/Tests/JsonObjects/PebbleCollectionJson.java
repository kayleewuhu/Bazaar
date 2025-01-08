package Tests.JsonObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Common.Data.PebbleColor;
import Common.Data.PebbleCollection;

public class PebbleCollectionJson {
  private List<PebbleJson> pebbles;

  public PebbleCollectionJson(PebbleCollection pebbles) {
    List<PebbleColor> pebblesList = pebbles.getPebblesAsList();
    List<PebbleJson> pebblesJsonList = new ArrayList<>();

    for (PebbleColor pebble : pebblesList) {
      pebblesJsonList.add(new PebbleJson(pebble));
    }

    this.pebbles = pebblesJsonList;
  }

  public PebbleCollectionJson(List<String> pebbles) {
    this.pebbles = pebbles.stream().map(PebbleJson::new).collect(Collectors.toList());
  }

  public PebbleCollection parseIntoObject() {
    Map<PebbleColor, Integer> pebblesMap = new HashMap<>();
    for (PebbleJson pebble : this.pebbles) {
      PebbleColor color = pebble.parseIntoObject();
      pebblesMap.put(color, pebblesMap.getOrDefault(color, 0) + 1);
    }
    return new PebbleCollection(pebblesMap);
  }

  public List<String> toListOfString() {
    return this.pebbles.stream().map(PebbleJson::toString).collect(Collectors.toList());
  }
}
