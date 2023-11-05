package org.ants.generator.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ants.generator.algo.Path;
import org.ants.generator.algo.DijkstraMultiPath;
import org.ants.generator.algo.Graph;
import org.ants.generator.algo.NumericalPath;
import org.ants.generator.routing.weight.BGPAdjRib;
import org.ants.generator.routing.weight.BGPWeight;
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
        }
      }
    }

    // print graph
    System.out.println(graph);
    // System.out.println(networksPendingPropagate);

  }

  public void propagateRouteAdvertisementSSSP() {
    for (BgpNetwork bgpNetwork : networksPendingPropagate) {
      System.out.println("Propagating " + bgpNetwork);

      DijkstraMultiPath<Node, ComparableBGPWeight> dijkstra = new DijkstraMultiPath<>();
      Map<Node, List<Path<Node, ComparableBGPWeight>>> shortestPaths = dijkstra.findShortestPaths(graph,
          bgpNetwork.node);

      for (Map.Entry<Node, List<Path<Node, ComparableBGPWeight>>> entry : shortestPaths.entrySet()) {
        System.out.println("Shortest paths to " + entry.getKey() + ":");
        for (Path<Node, ComparableBGPWeight> path : entry.getValue()) {
          System.out.println(path);
        }
      }

      // build initial distances
      // Map<Node, Map<Node, BGPWeight>> distances = new HashMap<>();
      // for (Node node1 : oldGraph.getVertices()) {
      // if (!distances.containsKey(node1))
      // distances.put(node1, new HashMap<>());

      // for (Node node2 : oldGraph.getVertices()) {
      // if (node1 == node2) {
      // distances.get(node1).put(node1, new BGPWeight(node1, node1, null, false,
      // true));
      // } else if (oldGraph.hasEdge(node1, node2)) {
      // ArrayList<Long> asPath = new ArrayList<>();
      // asPath.add(node1.as);
      // distances.get(node1).put(node2,
      // new BGPWeight(node1, node2, new BGPAdjRib(bgpNetwork, asPath, 100), false,
      // false));
      // } else {
      // distances.get(node1).put(node2, new BGPWeight(node1, node2, null, true,
      // false));
      // }
      // }
      // }

      // 2. get updated distances and paths
      // HashMap<Node, ArrayList<Node>> nodePaths =
      // this.dijkstraForAdvertisement(bgpNetwork.node, distances);
      // this.printNodePaths(nodePaths);

      // 3. Derive RIBs from SPT
      // Map<Node, Set<Prefix>> nodeRibs = new HashMap<>();
      // for (Node node : nodePaths.keySet()) {
      // for (int i = 0; i < nodePaths.get(node).size(); i++) {
      // Node next = nodePaths.get(node).get(i);
      // Set<Prefix> ribs = nodeRibs.get(next);
      // if (ribs == null) {
      // ribs = new HashSet<>();
      // nodeRibs.put(next, ribs);
      // }
      // ribs.add(distances.get(bgpNetwork.node).get(next).adjRib.bgpNetwork.prefix);
      // }
      // }

      // this.printNodeRIBs(nodeRibs);
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

  public void printNodeRIBs(Map<Node, Set<Prefix>> nodeRibs) {
    for (Node node : nodeRibs.keySet()) {
      System.out.println("RIBs of " + node.node + ": ");
      for (Prefix rib : nodeRibs.get(node)) {
        System.out.println("  " + rib);
      }
      System.out.println();
    }
  }

  /**
   * Run dijkstra to update the distance from startNode to all other nodes, and
   * also record the available paths.
   * 
   * @param startNode     the node to start propagation
   * @param initDistances initial distances from all nodes to all other nodes
   * @return
   */
  // public HashMap<Node, ArrayList<Node>> dijkstraForAdvertisement(Node
  // startNode,
  // Map<Node, Map<Node, BGPWeight>> initDistances) {
  // Set<Node> visited = new HashSet<>();
  // visited.add(startNode);

  // Map<Node, BGPWeight> dist = initDistances.get(startNode);
  // Set<Node> remains = new HashSet<>();
  // for (Node node : this.oldGraph.getVertices()) {
  // if (startNode != node)
  // remains.add(node);
  // }

  // // initialize paths (used for multi path)
  // HashMap<Node, ArrayList<Node>> nodePaths = new HashMap<>();
  // for (Node node : initDistances.keySet()) {
  // ArrayList<Node> paths = new ArrayList<>();
  // if (!dist.get(node).inf && !dist.get(node).isLocal)
  // paths.add(node);
  // nodePaths.put(node, paths);
  // }

  // // do dijkstra
  // while (remains.size() > 0) {
  // Node now = remains.iterator().next();

  // for (Node other : remains) {
  // if (dist.get(other).lowerThan(dist.get(now))) {
  // now = other;
  // }
  // }

  // remains.remove(now);
  // visited.add(now);

  // for (Node node : remains) {
  // BGPWeight d = dist.get(now).plus(initDistances.get(now).get(node));

  // if (d.lowerThan(dist.get(node))) {
  // dist.put(node, d);
  // }
  // }
  // }
  // return nodePaths;
  // }
}
