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
    Path<TNode, TWeight> neighborShortestPath = shortestPaths.get(destination);
    Path<TNode, TWeight> destinationShortestPath = shortestPaths.get(destination);

    //extend 
    List<TNode> newVertices = new ArrayList<>(sourceShortestPath.getVertices());
    newVertices.add(destination);

    //propagated path total weight
    TWeight currentPathWeight =sourceShortestPath.getTotalWeight();
    TWeight newTotalWeight = (currentPathWeight == null) ? weight : currentPathWeight.add(weight);
    
    TWeight neighborShortestPaTWeight = (neighborShortestPath == null) 
    ? new TWeight(Integer.MAX_VALUE): neighborShortestPath.getTotalWeight();

    if (newTotalWeight.compareTo(destinationShortestPath.getTotalWeight()) < 0) {
      NumericalPath<TNode,TWeight> newPath = new NumericalPath<>(newVertices, newTotalWeight);
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
        for (TNode sourceVertex : graph.getVertices()) {
          for (Edge<TNode, TWeight> edge : graph.getNeighbors(sourceVertex)) {
            TNode destinationVertex = edge.getDestination();
            TWeight weight = edge.getWeight();

            relax(sourceVertex, destinationVertex, weight);
          }
        }
        
      }
      //check negative cycle
      //not implemented yet


    return shortestPaths;
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
    Graph<String> graph = new Graph<>();
    graph.addEdge("A", "B", new TWeight(2));
    graph.addEdge("A", "C", new TWeight(4));
    graph.addEdge("B", "C", new TWeight(1));
    graph.addEdge("B", "D", new TWeight(7));
    graph.addEdge("C", "E", new TWeight(3));
    graph.addEdge("D", "E", new TWeight(2));

    BellmanFord<String> bellmanFord = new BellmanFord<>();
    Map<String, Path<String, TWeight>> shortestPaths = bellmanFord.findShortestPaths(graph, "A",
        new NumericalPath<>("A"));

    for (Map.Entry<String, Path<String, TWeight>> entry : shortestPaths.entrySet()) {
      System.out.println("Shortest paths to " + entry.getKey() + ":");
      Path<String, TWeight> path = entry.getValue();
      System.out.println(path);
      
    }

  }
}