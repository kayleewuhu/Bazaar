package Tests.JsonObjects;

import java.util.ArrayList;
import java.util.List;

import Common.Data.UnidirectionalEquation;
import Common.Data.PebbleCollection;

public class UnidirectionalEquationJson {
  private List<List<String>> equation;

  public UnidirectionalEquationJson(UnidirectionalEquation equation) {
    PebbleCollection input = equation.getInputSideCopy();
    PebbleCollection output = equation.getOutputSideCopy();
    this.equation = new ArrayList<>();
    this.equation.add(new PebbleCollectionJson(input).toListOfString());
    this.equation.add(new PebbleCollectionJson(output).toListOfString());
  }

  public UnidirectionalEquationJson(List<List<String>> equation) {
    this.equation = equation;
  }


  public UnidirectionalEquation parseIntoObject() {
    PebbleCollectionJson input = new PebbleCollectionJson(this.equation.get(0));
    PebbleCollectionJson output = new PebbleCollectionJson(this.equation.get(1));
    return new UnidirectionalEquation(input.parseIntoObject(), output.parseIntoObject());
  }

  public List<List<String>> toListListString() {
    return this.equation;
  }
}
