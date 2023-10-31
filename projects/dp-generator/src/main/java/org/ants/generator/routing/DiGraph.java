package org.ants.generator.routing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DiGraph<T> {

  private final Map<T, Set<T>> adjacencyList = new HashMap<>();

  public void addVertex(T vertex) {
    if (!adjacencyList.containsKey(vertex)) {
      adjacencyList.put(vertex, new HashSet<>());
    }
  }

  public void addEdge(T from, T to) {
    addVertex(from);
    addVertex(to);
    adjacencyList.get(from).add(to);
  }

  public void removeVertex(T vertex) {
    adjacencyList.remove(vertex);
    for (Set<T> neighbors : adjacencyList.values()) {
      neighbors.remove(vertex);
    }
  }

  public void removeEdge(T from, T to) {
    if (adjacencyList.containsKey(from)) {
      adjacencyList.get(from).remove(to);
    }
  }

  public boolean hasVertex(T vertex) {
    return adjacencyList.containsKey(vertex);
  }

  public boolean hasEdge(T from, T to) {
    return adjacencyList.containsKey(from) && adjacencyList.get(from).contains(to);
  }

  public Set<T> getVertices() {
    return adjacencyList.keySet();
  }

  public Set<T> neighbors(T vertex) {
    return adjacencyList.getOrDefault(vertex, new HashSet<>());
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    for (T vertex : adjacencyList.keySet()) {
      builder.append(vertex.toString()).append(" -> ").append(adjacencyList.get(vertex).toString()).append("\n");
    }

    return builder.toString();
  }

  public static void main(String[] args) {
    DiGraph<String> graph = new DiGraph<>();
    graph.addVertex("A");
    graph.addVertex("B");
    graph.addVertex("C");
    graph.addEdge("A", "B");
    graph.addEdge("B", "A");
    graph.addEdge("B", "C");
    System.out.println(graph);
  }
}
