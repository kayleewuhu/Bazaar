package Tests.JsonObjects;

import java.util.ArrayList;
import java.util.List;

import Common.Data.BidirectionalEquation;
import Common.Data.PebbleCollection;

public class BidirectionalEquationJson {
  private List<List<String>> equation;

  public BidirectionalEquationJson(BidirectionalEquation equation) {
    PebbleCollection leftSide = equation.getLeftSideCopy();
    PebbleCollection rightSide = equation.getRightSideCopy();
    this.equation = new ArrayList<>();
    this.equation.add(new PebbleCollectionJson(leftSide).toListOfString());
    this.equation.add(new PebbleCollectionJson(rightSide).toListOfString());
  }

  public BidirectionalEquationJson(List<List<String>> equation) {
    this.equation = equation;
  }

  public BidirectionalEquation parseIntoObject() {
    PebbleCollectionJson leftSide = new PebbleCollectionJson(this.equation.get(0));
    PebbleCollectionJson rightSide = new PebbleCollectionJson(this.equation.get(1));
    return new BidirectionalEquation(leftSide.parseIntoObject(), rightSide.parseIntoObject());
  }
}
