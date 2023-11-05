package org.ants.generator.algo;

import java.util.*;

public class FloydWarshall<T, W extends Comparable<W>> {

  private Map<T, Map<T, Path<T, W>>> shortestPaths;

  public FloydWarshall() {
    shortestPaths = new HashMap<>();
  }

  public Map<T, Map<T, Path<T, W>>> findAllShortestPaths(Graph<T, W> graph) {
    shortestPaths.clear();

    // Initialize shortestPaths with direct paths and weights
    for (T vertex : graph.getVertices()) {
      shortestPaths.put(vertex, new HashMap<>());
      for (Edge<T, W> edge : graph.getNeighbors(vertex)) {
        T destination = edge.getDestination();
        W weight = edge.getWeight();
        shortestPaths.get(vertex).put(destination, new Path<>(Arrays.asList(vertex, destination), weight));
      }
    }

    for (T k : graph.getVertices()) {
      for (T i : graph.getVertices()) {
        for (T j : graph.getVertices()) {
          Path<T, W> pathIK = shortestPaths.get(i).get(k);
          Path<T, W> pathKJ = shortestPaths.get(k).get(j);
          if (pathIK != null && pathKJ != null) {
            W newWeight = pathIK.getTotalWeight().compareTo(pathKJ.getTotalWeight()) < 0 ? pathKJ.getTotalWeight()
                : pathIK.getTotalWeight();
            if (!shortestPaths.get(i).containsKey(j) ||
                newWeight.compareTo(shortestPaths.get(i).get(j).getTotalWeight()) < 0) {
              List<T> vertices = new ArrayList<>(pathIK.getVertices());
              vertices.addAll(pathKJ.getVertices().subList(1, pathKJ.getVertices().size()));
              Path<T, W> newPath = new Path<>(vertices, newWeight);
              shortestPaths.get(i).put(j, newPath);
            }
          }
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

    FloydWarshall<String, Integer> floydWarshall = new FloydWarshall<>();
    Map<String, Map<String, Path<String, Integer>>> allShortestPaths = floydWarshall.findAllShortestPaths(graph);

    for (Map.Entry<String, Map<String, Path<String, Integer>>> entry : allShortestPaths.entrySet()) {
      System.out.println("Shortest paths from " + entry.getKey() + ":");
      for (Map.Entry<String, Path<String, Integer>> pathEntry : entry.getValue().entrySet()) {
        System.out.println(pathEntry.getKey() + ": " + pathEntry.getValue());
      }
    }

    // Update the weight of an edge (e.g., increase the weight from A to B)
    graph.updateEdge("A", "B", 5);

    // Re-run Floyd-Warshall algorithm to account for the updated weight
    allShortestPaths = floydWarshall.findAllShortestPaths(graph);

    for (Map.Entry<String, Map<String, Path<String, Integer>>> entry : allShortestPaths.entrySet()) {
      System.out.println("Updated shortest paths from " + entry.getKey() + ":");
      for (Map.Entry<String, Path<String, Integer>> pathEntry : entry.getValue().entrySet()) {
        System.out.println(pathEntry.getKey() + ": " + pathEntry.getValue());
      }
    }
  }
}
