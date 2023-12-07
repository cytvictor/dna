package org.ants.generator.algo;

import java.util.*;

/**
 * A generic graph data structure.
 * 
 * @param <TNode>   The type of the vertices.
 * @param <TWeight> The type of the edge weights.
 */
public class Graph<TNode> {
  private Map <TNode, Map<TNode, TWeight>> inNeighborMap;
  private Map <TNode, Map<TNode, TWeight>> outNeighborMap;
  // private Map<TNode, List<Edge<TNode, TWeight>>> adjacencyList;
  // private Map<TNode, List<Edge<TNode, TWeight>>> inList;

  public Graph() {
    inNeighborMap = new HashMap<>();
    outNeighborMap = new HashMap<>();
    // adjacencyList = new HashMap<>();
    // inList = new HashMap<>();
  }

  public void addEdge(TNode source, TNode destination, TWeight weight) {
    // adjacencyList.computeIfAbsent(source, k -> new ArrayList<>()).add(new Edge<>(destination, weight));
    // inList.computeIfAbsent(destination, k -> new ArrayList<>()).add(new Edge<>(source, weight));
    inNeighborMap.computeIfAbsent(destination, null).put(source, weight);
    outNeighborMap.computeIfAbsent(source, null).put(destination, weight);
  }

  public void updateEdge(TNode source, TNode destination, TWeight newWeight) {
    // for (Edge<TNode, TWeight> edge : adjacencyList.get(source)) {
    //   if (edge.getDestination().equals(destination)) {
    //     edge.setWeight(newWeight);
    //   }
    // }

    // for (Edge<TNode, TWeight> edge : adjacencyList.get(destination)) {
    //   if (edge.getDestination().equals(source)) {
    //     edge.setWeight(newWeight);
    //   }
    // }
    inNeighborMap.get(destination).put(source, newWeight);
    outNeighborMap.get(source).put(destination, newWeight);
  }

  // public Set<TNode> getVertices() {
  //   return adjacencyList.keySet();
  // }
  
  public Set<TNode> getInNeighbors(TNode vertex) {
    // return adjacencyList.getOrDefault(vertex, Collections.emptyList());
    return inNeighborMap.getOrDefault(vertex, Collections.emptyMap()).keySet();
  }

  public Set<TNode> getOutNeighbors(TNode vertex) {
    // return adjacencyList.getOrDefault(vertex, Collections.emptyList());
    return outNeighborMap.getOrDefault(vertex, Collections.emptyMap()).keySet();
  }

  public TWeight getWeight(TNode source, TNode destination) {
    // for (Edge<TNode, TWeight> edge : adjacencyList.get(source)) {
    //   if (edge.getDestination().equals(destination)) {
    //     return edge.getWeight();
    //   }
    // }
    // return null;
    return inNeighborMap.getOrDefault(destination, Collections.emptyMap()).get(source);
  }
    


  // @Override
  // public String toString() {
  //   StringBuilder sb = new StringBuilder();
  //   sb.append("Graph {");
  //   for (TNode vertex : adjacencyList.keySet()) {
  //     sb.append(vertex.toString() + ": [");
  //     for (Edge<TNode, TWeight> edge : adjacencyList.get(vertex)) {
  //       sb.append(edge.toString() + " ");
  //     }
  //     sb.append("]\n");
  //   }
  //   sb.append("}");
  //   return sb.toString();
  // }
}
