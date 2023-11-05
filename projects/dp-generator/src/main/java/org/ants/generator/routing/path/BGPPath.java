package org.ants.generator.routing.path;

import java.util.ArrayList;
import java.util.List;

import org.ants.generator.algo.Path;

public class BGPPath<TNode, TWeight extends Comparable<TWeight>> extends Path<TNode, TWeight> {
  public BGPPath(TNode source) {
    super(source);
  }

  public BGPPath(List<TNode> vertices, TWeight totalWeightight) {
    super(vertices, totalWeightight);
  }

  @Override
  public Path<TNode, TWeight> extend(TNode destination, TWeight weight) {
    List<TNode> newVertices = new ArrayList<>(vertices);
    newVertices.add(destination);

    TWeight newTotalWeight = (totalWeight == null) ? weight : totalWeight;
    if (weight != null) {
      newTotalWeight = newTotalWeight.compareTo(weight) > 0 ? weight : newTotalWeight;
    }

    return new BGPPath<>(newVertices, newTotalWeight);
  }
}