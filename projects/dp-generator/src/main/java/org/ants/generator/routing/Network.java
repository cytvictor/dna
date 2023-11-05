package org.ants.generator.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ants.generator.routing.weight.BGPAdjRib;
import org.ants.generator.routing.weight.BGPWeight;
import org.ants.parser.relation.*;
import org.ants.parser.relation.neighbor.*;

public class Network {

  private DiGraph<Node> graph;
  private ArrayList<BgpNetwork> networksPendingPropagate;

  public Network() {
    graph = new DiGraph<>();
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
          graph.addEdge(node1, node2);
          graph.addEdge(node2, node1);
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
      // build initial distances
      Map<Node, Map<Node, BGPWeight>> distances = new HashMap<>();
      for (Node node1 : graph.getVertices()) {
        if (!distances.containsKey(node1))
          distances.put(node1, new HashMap<>());

        for (Node node2 : graph.getVertices()) {
          if (node1 == node2) {
            distances.get(node1).put(node1, new BGPWeight(node1, node1, null, false, true));
          } else if (graph.hasEdge(node1, node2)) {
            ArrayList<Long> asPath = new ArrayList<>();
            asPath.add(node1.as);
            distances.get(node1).put(node2,
                new BGPWeight(node1, node2, new BGPAdjRib(bgpNetwork, asPath, 100), false, false));
          } else {
            distances.get(node1).put(node2, new BGPWeight(node1, node2, null, true, false));
          }
        }
      }

      // get updated distances and paths
      HashMap<Node, ArrayList<Node>> nodePaths = this.dijkstraForAdvertisement(bgpNetwork.node, distances);
      this.printNodePaths(nodePaths);
    }
  }

  public void printNodePaths(HashMap<Node, ArrayList<Node>> nodePaths) {
    for (Node node : nodePaths.keySet()) {
      System.out.print(node.node + ": ");
      for (Node p : nodePaths.get(node)) {
        System.out.print(p.node + ",");
      }
      System.out.println();
    }
  }

  public HashMap<Node, ArrayList<Node>> dijkstraForAdvertisement(Node startNode,
      Map<Node, Map<Node, BGPWeight>> initDistances) {
    Set<Node> visited = new HashSet<>();
    visited.add(startNode);

    Map<Node, BGPWeight> dist = initDistances.get(startNode);
    Set<Node> remains = new HashSet<>();
    for (Node node : this.graph.getVertices()) {
      if (startNode != node)
        remains.add(node);
    }

    // initialize paths
    HashMap<Node, ArrayList<Node>> nodePaths = new HashMap<>();
    for (Node node : initDistances.keySet()) {
      ArrayList<Node> paths = new ArrayList<>();
      if (!dist.get(node).inf)
        paths.add(node);
      nodePaths.put(node, paths);
    }

    // do dijkstra
    while (remains.size() > 0) {
      Node now = remains.iterator().next();

      for (Node other : remains) {
        if (dist.get(other).lowerThan(dist.get(now))) {
          now = other;
        }
      }

      remains.remove(now);
      visited.add(now);

      for (Node node : remains) {
        // System.out.println(startNode.node + " -> " + now.node + " -> " + node.node);
        // System.out.println(" " + startNode.node + "->" + now.node + ": " +
        // dist.get(now));
        // System.out.println(" " + now.node + "->" + node.node + ": " +
        // initDistances.get(now).get(node));
        BGPWeight d = dist.get(now).plus(initDistances.get(now).get(node));
        // System.out.println(" Check shorter=new: " + startNode.node + "->" + node.node
        // + ": " + d);
        // System.out.println(" Check shorter=old: " + startNode.node + "->" + node.node
        // + ": " + dist.get(node));

        if (d.lowerThan(dist.get(node))) {
          // System.out.println(" Updated to new " + nodePaths.get(node));
          dist.put(node, d);
          nodePaths.get(node).clear();
          nodePaths.get(node).addAll(nodePaths.get(now));
          nodePaths.get(node).add(node);
        }
      }
    }
    return nodePaths;
  }
}
