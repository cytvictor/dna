package org.ants.generator.algo;

import java.util.*;

public class DijkstraMultiPath<TNode, TWeight extends Comparable<TWeight>> {

  private Map<TNode, List<Path<TNode, TWeight>>> shortestPaths;

  public DijkstraMultiPath() {
    shortestPaths = new HashMap<>();
  }

  public Map<TNode, List<Path<TNode, TWeight>>> findShortestPaths(Graph<TNode, TWeight> graph, TNode source) {
    shortestPaths.clear();

    PriorityQueue<Path<TNode, TWeight>> minHeap = new PriorityQueue<>();
    Set<TNode> visited = new HashSet<>();

    Path<TNode, TWeight> initialPath = new NumericalPath<>(source);
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
          Path<TNode, TWeight> newPath = currentPath.extend(neighborVertex, weight);
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
