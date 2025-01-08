package Common;

// This class represents a pair of two objects.
public class Pair<type1, type2> {
  public final type1 first;
  public final type2 second;

  public Pair(type1 first, type2 second) {
    this.first = first;
    this.second = second;
  }

  // is this pair equal to the given object?
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Pair otherPair)) {
      return false;
    }
    return this.first.equals(otherPair.first) && this.second.equals(otherPair.second);
  }
}
