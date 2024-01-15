package org.ants.generator.algo;

import java.util.*;



  public static void main(String[] args) {
    // Example usage
    Graph<String> graph = new Graph<>();
    graph.addEdge("A", "B", new TWeight(2));
    // graph.addEdge("A", "C", 4);
    graph.addEdge("A", "C", new TWeight(4));
    // graph.addEdge("B", "C", 1);
    graph.addEdge("B", "C", new TWeight(1));
    // graph.addEdge("B", "D", 7);
    graph.addEdge("B", "D", new TWeight(7));
    // graph.addEdge("C", "E", 3);
    graph.addEdge("C", "E", new TWeight(3));
    // graph.addEdge("D", "E", 2);
    graph.addEdge("D", "E", new TWeight(2));

    DynamicDijkstra<String> dynamicDijkstra = new DynamicDijkstra<>();
    Map<String, Path<String, TWeight>> shortestPaths = dynamicDijkstra.findShortestPaths(graph, "A",
        new NumericalPath<>("A"), false);

    for (Map.Entry<String, Path<String, TWeight>> entry : shortestPaths.entrySet()) {
      System.out.println("Shortest paths to " + entry.getKey() + ":");
      Path<String, TWeight> path = entry.getValue();
      System.out.println(path);
      //print cost of path
      int cost = (path.getTotalWeight() == null) ? Integer.MAX_VALUE : path.getTotalWeight().getCost();
      System.out.println(cost);
      
    }



    // Update the weight of an edge (e.g., increase the weight from A to B)
    graph.updateEdge("C", "E", new TWeight(9));

    // Re-run Dijkstra's algorithm to account for the updated weight
    shortestPaths = dynamicDijkstra.findShortestPaths(graph, "A", new NumericalPath<>("A"), false);

    for (Map.Entry<String, Path<String, TWeight>> entry : shortestPaths.entrySet()) {
      System.out.println("Updated shortest paths to " + entry.getKey() + ":");
      Path<String, TWeight> path = entry.getValue();
      System.out.println(path);
      //print cost of path
      int cost = (path.getTotalWeight() == null) ? Integer.MAX_VALUE : path.getTotalWeight().getCost();
      System.out.println(cost);
      
    }
  }
}