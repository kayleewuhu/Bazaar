package Tests.JsonObjects;

import java.util.ArrayList;
import java.util.List;

import Common.Data.BidirectionalEquation;
import Common.Data.EquationTable;

public class EquationTableJson {
  List<BidirectionalEquationJson> equations;

  public EquationTableJson(EquationTable equationTable) {
    this.equations = new ArrayList<>();
    for (BidirectionalEquation equation : equationTable.getAllEquationsCopy()) {
      this.equations.add(new BidirectionalEquationJson(equation));
    }
  }

  public EquationTableJson(List<BidirectionalEquationJson> equations) {
    this.equations = equations;
  }

  public EquationTable parseIntoObject() {
    List<BidirectionalEquation> equations = this.equations.stream().map(BidirectionalEquationJson::parseIntoObject).toList();
    return new EquationTable(equations);
  }
}
