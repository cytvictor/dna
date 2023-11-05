package org.ants.generator.algo;

import java.util.*;

public class Path<T, W extends Comparable<W>> implements Comparable<Path<T, W>> {
  private List<T> vertices;
  private W totalWeight;

  public Path(T source) {
    this.vertices = new ArrayList<>();
    this.vertices.add(source);
    this.totalWeight = null;
  }

  public Path(List<T> vertices, W totalWeight) {
    this.vertices = vertices;
    this.totalWeight = totalWeight;
  }

  public List<T> getVertices() {
    return vertices;
  }

  public T getDestination() {
    return vertices.get(vertices.size() - 1);
  }

  public Path<T, W> extend(T destination, W weight) {
    List<T> newVertices = new ArrayList<>(vertices);
    newVertices.add(destination);

    W newTotalWeight = (totalWeight == null) ? weight : totalWeight;
    if (weight != null) {
      newTotalWeight = newTotalWeight.compareTo(weight) > 0 ? weight : newTotalWeight;
    }

    return new Path<>(newVertices, newTotalWeight);
  }

  public W getTotalWeight() {
    return totalWeight;
  }

  @Override
  public int compareTo(Path<T, W> other) {
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
