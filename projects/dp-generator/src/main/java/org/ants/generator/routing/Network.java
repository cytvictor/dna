package org.ants.generator.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ants.generator.algo.DijkstraMultiPath;
import org.ants.generator.algo.DynamicDijkstra;
import org.ants.generator.algo.Graph;
import org.ants.generator.algo.Path;
import org.ants.generator.algo.TWeight;
import org.ants.generator.routing.path.BGPPath;
import org.ants.generator.routing.weight.ComparableBGPWeight;
import org.ants.parser.datamodel.Prefix;
import org.ants.parser.relation.*;
import org.ants.parser.relation.neighbor.*;

public class Network {

  // private DiGraph<Node> oldGraph;
  private Graph<Node> graph;
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
          graph.addEdge(node1, node2, new TWeight(0));
          graph.addEdge(node2, node1, new TWeight(0));
        }
      }
    }

    // print graph
    // System.out.println(graph);
    // System.out.println(networksPendingPropagate);

  }

  public void propagateRouteAdvertisementSSSP() {
    Map<Node, Set<BgpRib>> nodeRibs = new HashMap<>();

    for (BgpNetwork bgpNetwork : networksPendingPropagate) {
      System.out.println("Propagating " + bgpNetwork);

      // 1. Find SPT for this bgpNetwork
      DynamicDijkstra<Node> dijkstra = new DynamicDijkstra<>();
      Map<Node, Path<Node, TWeight>> shortestPaths = dijkstra.findShortestPaths(graph,
          bgpNetwork.node, new BGPPath<Node, TWeight>(bgpNetwork.node, bgpNetwork), false);

      // 2. Derive RIBs from SPT
      for (Map.Entry<Node, Path<Node, TWeight>> path : shortestPaths.entrySet()) {
        // System.out.println("Shortest paths to " + entry.getKey().node + ":");
        // for (Path<Node, TWeight> path : entry.getValue()) {
        System.out.println("  " + path);
        Node nextHop = null;
        for (Node visitedNode : path.getValue().getVertices()) {
          Set<BgpRib> ribs = nodeRibs.get(visitedNode);
          if (ribs == null) {
            ribs = new HashSet<>();
            nodeRibs.put(visitedNode, ribs);
          }
          if (visitedNode == bgpNetwork.node) {
            // 如果是本地节点，那么就是local rib, 下一跳是本地
            ribs.add(new BgpRib(((BGPPath<Node, TWeight>) path.getValue()).getBgpNetwork().prefix, bgpNetwork.node,
                true));
          } else {
            // 如果是其他节点，那么就是adj rib, 下一跳是 SPT 前一个节点
            ribs.add(new BgpRib(((BGPPath<Node, TWeight>) path.getValue()).getBgpNetwork().prefix, nextHop));
          }
          nextHop = visitedNode;
        }
        // }
      }
    }

    this.printNodeRIBs(nodeRibs);
  }

  public void printNodeRIBs(Map<Node, Set<BgpRib>> nodeRibs) {
    for (Node node : nodeRibs.keySet()) {
      System.out.println("RIBs of " + node.node + ": ");
      for (BgpRib rib : nodeRibs.get(node)) {
        System.out.println("  " + rib);
      }
      System.out.println();
    }
  }
}
