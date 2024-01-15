package org.ants.generator.algo;

import java.util.*;


public class MBallString<TNode> {

  private Map<TNode, Path<TNode, TWeight>> shortestPaths;
  private Map<TNode, Map<TNode, TWeight>> currentWeights; // Map to store current weights of edges

  public MBallString() {
    shortestPaths = new HashMap<>();
    currentWeights = new HashMap<>();
  }

  public Map<TNode, Path<TNode, TWeight>> findShortestPaths(Graph<TNode> graph, TNode source,
      Path<TNode, TWeight> initialPath, Boolean update) {
    if (!update){
      shortestPaths.clear();
      currentWeights.clear();
    }

    PriorityQueue<Path<TNode, TWeight>> minHeap = new PriorityQueue<>();
    Set<TNode> visited = new HashSet<>();

    minHeap.offer(initialPath);

    while (!minHeap.isEmpty()) {
      Path<TNode, TWeight> currentPath = minHeap.poll();
      TNode currentVertex = currentPath.getDestination();

      if (visited.contains(currentVertex)) {
        continue;
      }

      visited.add(currentVertex);

      // if (!shortestPaths.containsKey(currentVertex)) {
      //   shortestPaths.put(currentVertex, new Path<>(currentVertex));
      // }

      //check whether a in neighbor provides a shorter path
      for (TNode inNeighbor : graph.getInNeighbors(currentVertex)) { 
        Path<TNode, TWeight> inNeighborShortestPath = shortestPaths.get(inNeighbor);
        TNode shortestPathInNeighbor = (inNeighborShortestPath == null) ? null : inNeighborShortestPath.getDestination();
        if (shortestPathInNeighbor == null) {
          continue;
        }
        TWeight inNeighborShortestPathWeight = (inNeighborShortestPath == null) 
        ? new TWeight(null, null, null, true, false): inNeighborShortestPath.getTotalWeight();

        TWeight currentPathWeight = currentPath.getTotalWeight();
        //get edge weight
        TWeight edgeWeight = graph.getWeight(inNeighbor, currentVertex);

        TWeight newTotalWeight = (inNeighborShortestPathWeight == null)? edgeWeight: inNeighborShortestPathWeight.add(edgeWeight);

        //print current inNeighbor and current Vertex
        System.out.println("inNeighbor: " + inNeighbor);
        System.out.println("currentVertex: " + currentVertex);
        if (newTotalWeight.compareTo(currentPathWeight) < 0){
          //extend 
          List<TNode> newVertices =  new ArrayList<>(inNeighborShortestPath.getVertices());
          newVertices.add(currentVertex);

          NumericalPath<TNode,TWeight> newPath = new NumericalPath<>(newVertices, newTotalWeight);
          minHeap.offer(newPath);
        }
      }

      shortestPaths.put(currentVertex, currentPath);

      for (TNode outNeighbor : graph.getOutNeighbors(currentVertex)) {
        TWeight edgeWeight = graph.getWeight(currentVertex, outNeighbor);

        if (!visited.contains(outNeighbor)) {
          //TWeight currentWeight = getCurrentWeight(currentVertex, neighborVertex);
          // if (currentWeight == null || weight.compareTo(currentWeight) < 0) {
          //   updateCurrentWeight(currentVertex, neighborVertex, weight);
          //   Path<TNode, TWeight> newPath = currentPath.extend(neighborVertex, weight);
          //   minHeap.offer(newPath);
          // if (edgeWeight!=null){
          Path <TNode,TWeight> neighborShortestPath = shortestPaths.get(outNeighbor);


          //propagated path total weight
          TWeight currentPathWeight = currentPath.getTotalWeight();
          TWeight newTotalWeight = (currentPathWeight == null) ? edgeWeight : currentPathWeight.add(edgeWeight);
          
          TWeight neighborShortestPathWeight = (neighborShortestPath == null) 
          ? new TWeight(null, null, null, true, false): neighborShortestPath.getTotalWeight();

          if (newTotalWeight.compareTo(neighborShortestPathWeight) < 0){
            //extend 
            List<TNode> newVertices = new ArrayList<>(currentPath.getVertices());
            newVertices.add(outNeighbor);

            NumericalPath<TNode,TWeight> newPath = new NumericalPath<>(newVertices, newTotalWeight);
            minHeap.offer(newPath);
          }
          
        }
      }


    }
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

    MBallString<String> mballString = new MBallString<>();
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