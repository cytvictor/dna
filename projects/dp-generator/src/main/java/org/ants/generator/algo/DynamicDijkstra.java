package org.ants.generator.algo;

import java.util.*;

public class DynamicDijkstra<TNode, TWeight extends Comparable<TWeight>> {

  private Map<TNode, List<Path<TNode, TWeight>>> shortestPaths;
  private Map<TNode, Map<TNode, TWeight>> currentWeights; // Map to store current weights of edges

  public DynamicDijkstra() {
    shortestPaths = new HashMap<>();
    currentWeights = new HashMap<>();
  }

  public Map<TNode, List<Path<TNode, TWeight>>> findShortestPaths(Graph<TNode, TWeight> graph, TNode source) {
    shortestPaths.clear();
    currentWeights.clear();

    PriorityQueue<Path<TNode, TWeight>> minHeap = new PriorityQueue<>();
    Set<TNode> visited = new HashSet<>();

    Path<TNode, TWeight> initialPath = new Path<>(source);
    minHeap.offer(initialPath);

    while (!minHeap.isEmpty()) {
      Path<TNode, TWeight> currentPath = minHeap.poll();
      TNode currentVertex = currentPath.getDestination();

      if (visited.contains(currentVertex)) {
        continue;
      }

      visited.add(currentVertex);

      if (!shortestPaths.containsKey(currentVertex)) {
        shortestPaths.put(currentVertex, new ArrayList<>());
      }

      shortestPaths.get(currentVertex).add(currentPath);

      List<Edge<TNode, TWeight>> neighbors = graph.getNeighbors(currentVertex);

      for (Edge<TNode, TWeight> neighbor : neighbors) {
        TNode neighborVertex = neighbor.getDestination();
        TWeight weight = neighbor.getWeight();

        if (!visited.contains(neighborVertex)) {
          TWeight currentWeight = getCurrentWeight(currentVertex, neighborVertex);
          if (currentWeight == null || weight.compareTo(currentWeight) < 0) {
            updateCurrentWeight(currentVertex, neighborVertex, weight);
            Path<TNode, TWeight> newPath = currentPath.extend(neighborVertex, weight);
            minHeap.offer(newPath);
          }
        }
      }
    }

    return shortestPaths;
  }

  // Function to get the current weight of an edge
  private TWeight getCurrentWeight(TNode source, TNode destination) {
    return currentWeights.getOrDefault(source, new HashMap<>()).get(destination);
  }

  // Function to update the current weight of an edge
  private void updateCurrentWeight(TNode source, TNode destination, TWeight newWeight) {
    currentWeights.computeIfAbsent(source, k -> new HashMap<>()).put(destination, newWeight);
  }

  public static void main(String[] args) {
    // Example usage
    Graph<String, Integer> graph = new Graph<>();
    graph.addEdge("A", "B", 2);
    graph.addEdge("A", "C", 4);
    graph.addEdge("B", "C", 1);
    graph.addEdge("B", "D", 7);
    graph.addEdge("C", "E", 3);
    graph.addEdge("D", "E", 2);

    DynamicDijkstra<String, Integer> dynamicDijkstra = new DynamicDijkstra<>();
    Map<String, List<Path<String, Integer>>> shortestPaths = dynamicDijkstra.findShortestPaths(graph, "A");

    for (Map.Entry<String, List<Path<String, Integer>>> entry : shortestPaths.entrySet()) {
      System.out.println("Shortest paths to " + entry.getKey() + ":");
      for (Path<String, Integer> path : entry.getValue()) {
        System.out.println(path);
      }
    }

    // Update the weight of an edge (e.g., increase the weight from A to B)
    graph.updateEdge("A", "B", 5);

    // Re-run Dijkstra's algorithm to account for the updated weight
    shortestPaths = dynamicDijkstra.findShortestPaths(graph, "A");

    for (Map.Entry<String, List<Path<String, Integer>>> entry : shortestPaths.entrySet()) {
      System.out.println("Updated shortest paths to " + entry.getKey() + ":");
      for (Path<String, Integer> path : entry.getValue()) {
        System.out.println(path);
      }
    }
  }
}