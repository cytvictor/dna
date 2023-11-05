package org.ants.generator.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ants.generator.algo.DijkstraMultiPath;
import org.ants.generator.algo.Graph;
import org.ants.generator.algo.Path;
import org.ants.generator.routing.path.BGPPath;
import org.ants.generator.routing.weight.ComparableBGPWeight;
import org.ants.parser.datamodel.Prefix;
import org.ants.parser.relation.*;
import org.ants.parser.relation.neighbor.*;

public class Network {

  // private DiGraph<Node> oldGraph;
  private Graph<Node, ComparableBGPWeight> graph;
  private ArrayList<BgpNetwork> networksPendingPropagate;

  public Network() {
    // oldGraph = new DiGraph<>();
    graph = new Graph<>();
    networksPendingPropagate = new ArrayList<>();
  }

  public void constructGraphFromUpdates(Map<String, List<Relation>> updates) {

    // 1. add nodes
    for (String updateType : updates.keySet()) {
      for (Relation rel : updates.get(updateType)) {
        if (rel instanceof BgpNetwork) {
          networksPendingPropagate.add((BgpNetwork) rel);
        }
      }
    }

    // 2. Now we process BGP routing. We recognize BGP neighbors (instead of links)
    // as neighbors
    for (String updateType : updates.keySet()) {
      for (Relation rel : updates.get(updateType)) {
        if (rel instanceof EBgpNeighbor) {
          EBgpNeighbor bgpNeighbor = (EBgpNeighbor) rel;
          Node node1 = bgpNeighbor.node1;
          Node node2 = bgpNeighbor.node2;
          // oldGraph.addEdge(node1, node2);
          // oldGraph.addEdge(node2, node1);
          graph.addEdge(node1, node2, new ComparableBGPWeight(false));
          graph.addEdge(node2, node1, new ComparableBGPWeight(false));
        }
      }
    }

    // print graph
    // System.out.println(graph);
    // System.out.println(networksPendingPropagate);

  }

  public void propagateRouteAdvertisementSSSP() {
    for (BgpNetwork bgpNetwork : networksPendingPropagate) {
      System.out.println("Propagating " + bgpNetwork);

      // 1. Find SPT for this bgpNetwork
      DijkstraMultiPath<Node, ComparableBGPWeight> dijkstra = new DijkstraMultiPath<>();
      Map<Node, List<Path<Node, ComparableBGPWeight>>> shortestPaths = dijkstra.findShortestPaths(graph,
          bgpNetwork.node, new BGPPath<Node, ComparableBGPWeight>(bgpNetwork.node, bgpNetwork));

      // 2. Derive RIBs from SPT
      Map<Node, Set<Prefix>> nodeRibs = new HashMap<>();
      for (Map.Entry<Node, List<Path<Node, ComparableBGPWeight>>> entry : shortestPaths.entrySet()) {
        System.out.println("Shortest paths to " + entry.getKey().node + ":");
        for (Path<Node, ComparableBGPWeight> path : entry.getValue()) {
          System.out.println("  " + path);
          for (Node visitedNode : path.getVertices()) {
            Set<Prefix> ribs = nodeRibs.get(visitedNode);
            if (ribs == null) {
              ribs = new HashSet<>();
              nodeRibs.put(visitedNode, ribs);
            }
            ribs.add(((BGPPath<Node, ComparableBGPWeight>) path).getBgpNetwork().prefix);
          }
        }
      }

      this.printNodeRIBs(nodeRibs);
    }
  }

  public void printNodeRIBs(Map<Node, Set<Prefix>> nodeRibs) {
    for (Node node : nodeRibs.keySet()) {
      System.out.println("RIBs of " + node.node + ": ");
      for (Prefix rib : nodeRibs.get(node)) {
        System.out.println("  " + rib);
      }
      System.out.println();
    }
  }
}
