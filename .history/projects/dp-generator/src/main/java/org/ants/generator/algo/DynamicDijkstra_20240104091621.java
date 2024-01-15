package org.ants.generator.algo;

import java.util.*;


public class DynamicDijkstra<TNode> {

  private Map<TNode, Path<TNode, TWeight>> shortestPaths;
  // private Map<TNode, Map<TNode, TWeight>> currentWeights; // Map to store current weights of edges

  public DynamicDijkstra() {
    shortestPaths = new HashMap<>();
    // currentWeights = new HashMap<>();
  }

  public Map<TNode, Path<TNode, TWeight>> findShortestPaths(Graph<TNode> graph, TNode source,
    Map<TNode,Path<TNode,TWeight>> curShortestPaths, TNode update_source,
      TNode update_destination, TWeight update_weight) {
    
    //initialize heap
    PriorityQueue<Path<TNode, TWeight>> minHeap = new PriorityQueue<>();
    if (update_source != null && update_destination != null && update_weight != null){
      //check whether an in neighbor provides a shorter path
      TWeight currentPathWeight = new TWeight(Integer.MAX_VALUE);
      TNode cur_in_neighbor = null;

      for (TNode inNeighbor : graph.getInNeighbors(update_destination)) { 
        Path<TNode, TWeight> inNeighborShortestPath = shortestPaths.get(inNeighbor);
        TNode shortestPathInNeighbor = (inNeighborShortestPath == null) ? null : inNeighborShortestPath.getDestination();
        if (shortestPathInNeighbor == null) {
          continue;
        }
        TWeight inNeighborShortestPathWeight = (inNeighborShortestPath == null) 
        ? new TWeight(Integer.MAX_VALUE): inNeighborShortestPath.getTotalWeight();
        
        //get edge weight
        TWeight edgeWeight = graph.getWeight(inNeighbor, update_destination);

        TWeight newTotalWeight = (inNeighborShortestPathWeight == null)? edgeWeight: inNeighborShortestPathWeight.add(edgeWeight);

        //print current inNeighbor and current Vertex
        System.out.println("inNeighbor: " + inNeighbor);
        System.out.println("path weight from inNeighbor to currentVertex: " + newTotalWeight.getCost());
        System.out.println("currentVertex: " + update_destination);
        System.out.println("currentPathWeight: " + currentPathWeight.getCost());
        if (newTotalWeight.compareTo(currentPathWeight) < 0){
          currentPathWeight = newTotalWeight;
          cur_in_neighbor = inNeighbor;
        }
      }

      //add initialPath to heap if it's different from shortest path in curShortestPaths in in-neighbor and weight
      Path<TNode,TWeight> curShortestPath = curShortestPaths.get(update_destination);
      if (curShortestPath == null || !curShortestPath.getLastButOneVertex().equals(cur_in_neighbor) || !curShortestPath.getTotalWeight().equals(currentPathWeight)){
        //extend 
        List<TNode> newVertices =  new ArrayList<>(curShortestPaths.get(cur_in_neighbor).getVertices());
        newVertices.add(update_destination);

        NumericalPath<TNode,TWeight> newPath = new NumericalPath<>(newVertices, currentPathWeight);
        minHeap.offer(newPath);
      }
    }

    //static case
    else{
      Path<TNode,TWeight> initialPath = new NumericalPath<>(source);
      minHeap.offer(initialPath);
    }





    //iterative operations on heap
    shortestPaths = (curShortestPaths == null)? new HashMap<>(): curShortestPaths;
    Set<TNode> visited = new HashSet<>();


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
    //current time
    long startTime = System.nanoTime();
    Map<String, Path<String, TWeight>> shortestPaths = dynamicDijkstra.findShortestPaths(graph, "A",null,null,null,null);
    long endTime = System.nanoTime();
    long duration = (endTime - startTime);
    System.out.println("Dijkstra duration: " + duration);

    for (Map.Entry<String, Path<String, TWeight>> entry : shortestPaths.entrySet()) {
      System.out.println("Shortest paths to " + entry.getKey() + ":");
      Path<String, TWeight> path = entry.getValue();
      System.out.println(path);
      //print cost of path
      int cost = (path.getTotalWeight() == null) ? Integer.MAX_VALUE : path.getTotalWeight().getCost();
      System.out.println(cost);
      
    }





    // Re-run Dijkstra's algorithm to account for the updated weight

    // implement update
    graph.updateEdge("C", "E", new TWeight(9));

    //current time
    startTime = System.nanoTime();
    shortestPaths = dynamicDijkstra.findShortestPaths(graph, "A", shortestPaths,"C","E",new TWeight(9));
    endTime = System.nanoTime();
    duration = (endTime - startTime);
    System.out.println("Dynamic Dijkstra duration: " + duration);

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