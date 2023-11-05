package org.ants.generator.algo;

import java.util.ArrayList;
import java.util.List;

public class Path<TNode, TWeight extends Comparable<TWeight>> extends AbstractPath<TNode, TWeight> {

  public Path(List<TNode> vertices, TWeight totalWeightight) {
    super(vertices, totalWeightight);
  }

  public Path(TNode source) {
    super(source);
  }

  @Override
  public Path<TNode, TWeight> extend(TNode destination, TWeight weight) {
    List<TNode> newVertices = new ArrayList<>(vertices);
    newVertices.add(destination);

    TWeight newTotalWeight = (totalWeight == null) ? weight : totalWeight;
    if (weight != null) {
      newTotalWeight = newTotalWeight.compareTo(weight) > 0 ? weight : newTotalWeight;
    }

    return new Path<>(newVertices, newTotalWeight);
  }

}
