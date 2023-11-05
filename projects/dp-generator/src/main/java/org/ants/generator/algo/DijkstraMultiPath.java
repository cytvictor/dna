package org.ants.generator.algo;

import java.util.*;

public class DijkstraMultiPath<T, W extends Comparable<W>> {

  private Map<T, List<Path<T, W>>> shortestPaths;

  public DijkstraMultiPath() {
    shortestPaths = new HashMap<>();
  }

  public Map<T, List<Path<T, W>>> findShortestPaths(Graph<T, W> graph, T source) {
    shortestPaths.clear();

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
          Path<T, W> newPath = currentPath.extend(neighborVertex, weight);
          minHeap.offer(newPath);
        }
      }
    }

    return shortestPaths;
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

    DijkstraMultiPath<String, Integer> dijkstra = new DijkstraMultiPath<>();
    Map<String, List<Path<String, Integer>>> shortestPaths = dijkstra.findShortestPaths(graph, "A");

    for (Map.Entry<String, List<Path<String, Integer>>> entry : shortestPaths.entrySet()) {
      System.out.println("Shortest paths to " + entry.getKey() + ":");
      for (Path<String, Integer> path : entry.getValue()) {
        System.out.println(path);
      }
    }
  }
}
