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

  // @Override
  // public NumericalPath<TNode, TWeight> extend(TNode destination, TWeight weight, Path<TNode, TWeight> neighborShortestPath) {

  //   List<TNode> newVertices = new ArrayList<>(vertices);
  //   newVertices.add(destination);

  //   //propagated path total weight
  //   TWeight newTotalWeight = (totalWeight == null) ? weight : totalWeight + weight;
  //   TWeight neighborShortestPaTWeight = neighborShortestPath.getTotalWeight();

  //   if (newTotalWeight.compareTo(neighborShortestPaTWeight) < 0){
  //     newPath = new NumericalPath<>(newVertices, newTotalWeight)
  //     minHeap.offer(newPath);
  //   }
  //   // newTotalWeight = newTotalWeight.compareTo(neighborShortestPaTWeight) > 0 ? neighborShortestPaTWeight : newTotalWeight;
    

  //   return new NumericalPath<>(newVertices, newTotalWeight);
  // }

}
