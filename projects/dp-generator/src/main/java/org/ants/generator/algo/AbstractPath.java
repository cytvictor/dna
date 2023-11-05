package org.ants.generator.algo;

import java.util.*;

public abstract class AbstractPath<TNode, TWeight extends Comparable<TWeight>>
    implements Comparable<AbstractPath<TNode, TWeight>> {
  protected List<TNode> vertices;
  protected TWeight totalWeight;

  public AbstractPath(TNode source) {
    this.vertices = new ArrayList<>();
    this.vertices.add(source);
    this.totalWeight = null;
  }

  public AbstractPath(List<TNode> vertices, TWeight totalWeight) {
    this.vertices = vertices;
    this.totalWeight = totalWeight;
  }

  public List<TNode> getVertices() {
    return vertices;
  }

  public TNode getDestination() {
    return vertices.get(vertices.size() - 1);
  }

  public abstract AbstractPath<TNode, TWeight> extend(TNode destination, TWeight weight);

  public TWeight getTotalWeight() {
    return totalWeight;
  }

  @Override
  public int compareTo(AbstractPath<TNode, TWeight> other) {
    if (this.totalWeight == null && other.totalWeight == null) {
      return 0;
    } else if (this.totalWeight == null) {
      return 1;
    } else if (other.totalWeight == null) {
      return -1;
    } else {
      return this.totalWeight.compareTo(other.totalWeight);
    }
  }

  @Override
  public String toString() {
    return "Path{" +
        "vertices=" + vertices +
        ", totalWeight=" + totalWeight +
        '}';
  }
}
