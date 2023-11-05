package org.ants.generator.algo;

public class Edge<T, W extends Comparable<W>> {
  private T destination;
  private W weight;

  public Edge(T destination, W weight) {
    this.destination = destination;
    this.weight = weight;
  }

  public T getDestination() {
    return destination;
  }

  public W getWeight() {
    return weight;
  }
}
