package org.ants.generator.algo;

public class Edge<TNode, TWeight extends Comparable<TWeight>> {
  private TNode destination;
  private TWeight weight;

  public Edge(TNode destination, TWeight weight) {
    this.destination = destination;
    this.weight = weight;
  }

  public TNode getDestination() {
    return destination;
  }

  public TWeight getWeight() {
    return weight;
  }

  public void setWeight(TWeight weight) {
    this.weight = weight;
  }

  @Override
  public String toString() {
    return "Edge{" +
        "destination=" + destination +
        ", weight=" + weight +
        "}";
  }

}
