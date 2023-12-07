package org.ants.generator.algo;

import java.util.*;

public class BellmanFord<TNode> {

  private Map<TNode, Path<TNode, TWeight>> shortestPaths;
  private Map<TNode, Map<TNode, TWeight>> currentWeights; // Map to store current weights of edges

  public BellmanFord() {
    shortestPaths = new HashMap<>();
    currentWeights = new HashMap<>();
  }

  //relaxation
  public void relax(TNode source, TNode destination, TWeight weight) {
    Path<TNode, TWeight> sourceShortestPath = shortestPaths.get(source);
    Path<TNode, TWeight> destinationShortestPath = shortestPaths.get(destination);

    //extend 
    List<TNode> newVertices = new ArrayList<>(sourceShortestPath.getVertices());
    newVertices.add(destination);

    //propagated path total weight
    TWeight newTotalWeight = (sourceShortestPath.getTotalWeight() == null) ? weight
        : sourceShortestPath.getTotalWeight().compareTo(weight) < 0 ? weight : sourceShortestPath.getTotalWeight();

    Path<TNode, TWeight> newPath = new NumericalPath<>(newVertices, newTotalWeight);

    if (newTotalWeight.compareTo(destinationShortestPath.getTotalWeight()) < 0) {
      shortestPaths.put(destination, newPath);
    }
  }


  public Map<TNode, Path<TNode, TWeight>> findShortestPaths(Graph<TNode> graph, TNode source,
      Path<TNode, TWeight> initialPath) {
      shortestPaths.clear();
      currentWeights.clear();

      //initialization -- what's the initial weight for initialpaths?
      for (TNode vertex : graph.getVertices()) {
        shortestPaths.put(vertex, initialPath);
      }

      //relaxation
      for (int i = 0; i < graph.getVertices().size() - 1; i++) {
        for (Edge<TNode, TWeight> edge : graph.getEdges()) {
          TNode sourceVertex = edge.getSource();
          TNode destinationVertex = edge.getDestination();
          TWeight weight = edge.getWeight();

          relax(sourceVertex, destinationVertex, weight);
        }
        
      }


    return shortestPaths;
  }
}

  
  // Function to get the current weight of an edge
  // private TWeight getCurrentWeight(TNode source, TNode destination) {
  //   return currentWeights.getOrDefault(source, new HashMap<>()).get(destination);
  // }

  // // Function to update the current weight of an edge
  // private void updateCurrentWeight(TNode source, TNode destination, TWeight newWeight) {
  //   currentWeights.computeIfAbsent(source, k -> new HashMap<>()).put(destination, newWeight);
  // }

  public static void main(String[] args) {
    // Example usage
    Graph<String, Integer> graph = new Graph<>();
    graph.addEdge("A", "B", 2);
    graph.addEdge("A", "C", 4);
    graph.addEdge("B", "C", 1);
    graph.addEdge("B", "D", 7);
    graph.addEdge("C", "E", 3);
    graph.addEdge("D", "E", 2);

    BellmanFord<String, Integer> bellmanFord = new BellmanFord<>();
    Map<String, Path<String, Integer>> shortestPaths = bellmanFord.findShortestPaths(graph, "A",
        new NumericalPath<>("A"));

    for (Map.Entry<String, Path<String, Integer>> entry : shortestPaths.entrySet()) {
      System.out.println("Shortest paths to " + entry.getKey() + ":");
      Path<String, Integer> path = entry.getValue();
      System.out.println(path);
      
    }

  }
}