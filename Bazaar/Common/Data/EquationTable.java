package Common.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import Common.Constants;

// This class represents all the equations in the game.
public class EquationTable {
  // all the equations in the game
  private List<BidirectionalEquation> allEquations;

  public EquationTable(Random random) {
    generateEquationTable(random);
  }

  public EquationTable(List<BidirectionalEquation> allEquations) {
    this.allEquations = new ArrayList<>();
    for (BidirectionalEquation equation : allEquations) {
      this.allEquations.add(equation.getBidirectionalEquationCopy());
    }
  }

  // generate the equations using the given random object
  private void generateEquationTable(Random random) {
    List<BidirectionalEquation> equations = new ArrayList<>();
    for (int equationIdx = 0; equationIdx < Constants.NUM_EQUATIONS; equationIdx++) {
      equations.add(new BidirectionalEquation(random));
    }
    this.allEquations = equations;
  }

  // gets a deep copy of all the equations in table
  public List<BidirectionalEquation> getAllEquationsCopy() {
    List<BidirectionalEquation> deepCopy = new ArrayList<>();
    for (BidirectionalEquation equation : this.allEquations) {
      deepCopy.add(equation.getBidirectionalEquationCopy());
    }
    return deepCopy;
  }

  // returns a list of unidirectional equations from this equation table that can be
  // used for exchanges between the given player and bank
  public List<UnidirectionalEquation> filter(PebbleCollection playerWallet, PebbleCollection bank) {
    List<UnidirectionalEquation> result = this.convertAllEquationsToUni();
    return result.stream()
            .filter((uniEquation) -> uniEquation.canExchange(playerWallet, bank))
            .collect(Collectors.toList());
  }

  // converts this EquationTable into a list of Unidirectional Equations
  private List<UnidirectionalEquation> convertAllEquationsToUni() {
    List<UnidirectionalEquation> result = new ArrayList<>();
    for (BidirectionalEquation be : this.allEquations) {
      result.addAll(be.convertToUnidirectionalEquation());
    }
    return result;
  }

  // does this equation table contain the given Unidirectional equation?
  public boolean contains(UnidirectionalEquation equation) {
    List<UnidirectionalEquation> uniEquations = convertAllEquationsToUni();
    return uniEquations.contains(equation);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof EquationTable otherTable)) {
      return false;
    }

    if (this.allEquations.size() != otherTable.allEquations.size()) {
      return false;
    }

    for (BidirectionalEquation equation : this.allEquations) {
      List<UnidirectionalEquation> unis = equation.convertToUnidirectionalEquation();
      if (!otherTable.contains(unis.get(0)) && !otherTable.contains(unis.get(1))) {
        return false;
      }
    }
    return true;
  }
}
