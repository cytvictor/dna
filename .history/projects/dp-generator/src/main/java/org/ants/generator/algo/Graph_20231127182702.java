package org.ants.generator.algo;

import java.util.*;

/**
 * A generic graph data structure.
 * 
 * @param <TNode>   The type of the vertices.
 * @param <TWeight> The type of the edge weights.
 */
public class Graph<TNode, TWeight extends Comparable<TWeight>> {
  private Map<TNode, List<Edge<TNode, TWeight>>> adjacencyList;

  public Graph() {
    adjacencyList = new HashMap<>();
  }

  public void addEdge(TNode source, TNode destination, TWeight weight) {
    adjacencyList.computeIfAbsent(source, k -> new ArrayList<>()).add(new Edge<>(destination, weight));
  }

  public void updateEdge(TNode source, TNode destination, TWeight newWeight) {
    for (Edge<TNode, TWeight> edge : adjacencyList.get(source)) {
      if (edge.getDestination().equals(destination)) {
        edge.setWeight(newWeight);
      }
    }

    for (Edge<TNode, TWeight> edge : adjacencyList.get(destination)) {
      if (edge.getDestination().equals(source)) {
        edge.setWeight(newWeight);
      }
    }
  }

  public Set<TNode> getVertices() {
    return adjacencyList.keySet();
  }

  public List<Edge<TNode, TWeight>> getNeighbors(TNode vertex) {
    return adjacencyList.getOrDefault(vertex, Collections.emptyList());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Graph {");
    for (TNode vertex : adjacencyList.keySet()) {
      sb.append(vertex.toString() + ": [");
      for (Edge<TNode, TWeight> edge : adjacencyList.get(vertex)) {
        sb.append(edge.toString() + " ");
      }
      sb.append("]\n");
    }
    sb.append("}");
    return sb.toString();
  }
}
