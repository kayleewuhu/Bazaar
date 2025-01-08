package Tests.JsonObjects;

import java.util.List;
import java.util.stream.Collectors;

import Common.Data.Exchanges;

public class ExchangesJson {
  public List<List<List<String>>> rules;

  public ExchangesJson (Exchanges exchanges) {
    this.rules = exchanges.getSequenceOfExchangesCopy().stream().map(UnidirectionalEquationJson::new)
            .map(UnidirectionalEquationJson::toListListString).collect(Collectors.toList());
  }
}
