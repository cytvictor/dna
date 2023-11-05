package org.ants.generator.algo;

import java.util.*;

public class DynamicDijkstra<T, W extends Comparable<W>> {

  private Map<T, List<Path<T, W>>> shortestPaths;
  private Map<T, Map<T, W>> currentWeights; // Map to store current weights of edges

  public DynamicDijkstra() {
    shortestPaths = new HashMap<>();
    currentWeights = new HashMap<>();
  }

  public Map<T, List<Path<T, W>>> findShortestPaths(Graph<T, W> graph, T source) {
    shortestPaths.clear();
    currentWeights.clear();

    PriorityQueue<Path<T, W>> minHeap = new PriorityQueue<>();
    Set<T> visited = new HashSet<>();

    Path<T, W> initialPath = new Path<>(source);
    minHeap.offer(initialPath);

    while (!minHeap.isEmpty()) {
      Path<T, W> currentPath = minHeap.poll();
      T currentVertex = currentPath.getDestination();

      if (visited.contains(currentVertex)) {
        continue;
      }

      visited.add(currentVertex);

      if (!shortestPaths.containsKey(currentVertex)) {
        shortestPaths.put(currentVertex, new ArrayList<>());
      }

      shortestPaths.get(currentVertex).add(currentPath);

      List<Edge<T, W>> neighbors = graph.getNeighbors(currentVertex);

      for (Edge<T, W> neighbor : neighbors) {
        T neighborVertex = neighbor.getDestination();
        W weight = neighbor.getWeight();

        if (!visited.contains(neighborVertex)) {
          W currentWeight = getCurrentWeight(currentVertex, neighborVertex);
          if (currentWeight == null || weight.compareTo(currentWeight) < 0) {
            updateCurrentWeight(currentVertex, neighborVertex, weight);
            Path<T, W> newPath = currentPath.extend(neighborVertex, weight);
            minHeap.offer(newPath);
          }
        }
      }
    }

    return shortestPaths;
  }

  // Function to get the current weight of an edge
  private W getCurrentWeight(T source, T destination) {
    return currentWeights.getOrDefault(source, new HashMap<>()).get(destination);
  }

  // Function to update the current weight of an edge
  private void updateCurrentWeight(T source, T destination, W newWeight) {
    currentWeights.computeIfAbsent(source, k -> new HashMap<>()).put(destination, newWeight);
    currentWeights.computeIfAbsent(destination, k -> new HashMap<>()).put(source, newWeight); // For undirected graph
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