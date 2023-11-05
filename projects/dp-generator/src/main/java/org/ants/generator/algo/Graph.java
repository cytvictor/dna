package org.ants.generator.algo;

import java.util.*;

public class Graph<T, W extends Comparable<W>> {
  private Map<T, List<Edge<T, W>>> adjacencyList;

  public Graph() {
    adjacencyList = new HashMap<>();
  }

  public void addEdge(T source, T destination, W weight) {
    adjacencyList.computeIfAbsent(source, k -> new ArrayList<>()).add(new Edge<>(destination, weight));
  }

  public List<Edge<T, W>> getNeighbors(T vertex) {
    return adjacencyList.getOrDefault(vertex, Collections.emptyList());
  }
}
