package org.ants.generator.algo;

import java.util.ArrayList;
import java.util.List;

public class NumericalPath<TNode, TWeight extends Comparable<TWeight>> extends Path<TNode, TWeight> {

  public NumericalPath(List<TNode> vertices, TWeight totalWeightight) {
    super(vertices, totalWeightight);
  }

  public NumericalPath(TNode source) {
    super(source);
  }

  @Override
  public NumericalPath<TNode, TWeight> extend(TNode destination, TWeight weight) {
    List<TNode> newVertices = new ArrayList<>(vertices);
    newVertices.add(destination);

    TWeight newTotalWeight = (totalWeight == null) ? weight : totalWeight;
    if (weight != null) {
      newTotalWeight = newTotalWeight.compareTo(weight) > 0 ? weight : newTotalWeight;
    }

    return new NumericalPath<>(newVertices, newTotalWeight);
  }

}
