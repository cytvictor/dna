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

  public void updateEdge(T source, T destination, W newWeight) {
    for (Edge<T, W> edge : adjacencyList.get(source)) {
      if (edge.getDestination().equals(destination)) {
        edge.setWeight(newWeight);
      }
    }

    for (Edge<T, W> edge : adjacencyList.get(destination)) {
      if (edge.getDestination().equals(source)) {
        edge.setWeight(newWeight);
      }
    }
  }

  public Set<T> getVertices() {
    return adjacencyList.keySet();
  }

  public List<Edge<T, W>> getNeighbors(T vertex) {
    return adjacencyList.getOrDefault(vertex, Collections.emptyList());
  }
}
