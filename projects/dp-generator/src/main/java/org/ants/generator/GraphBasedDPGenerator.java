package org.ants.generator;

import java.util.*;

import org.ants.generator.routing.Network;
import org.ants.parser.relation.Relation;

public class GraphBasedDPGenerator {

  public GraphBasedDPGenerator() {
  }

  public ArrayList<String> generateFibUpdates(Map<String, List<Relation>> updates) {
    Network network = new Network();
    network.constructGraphFromUpdates(updates);
    network.propagateRouteAdvertisementSSSP();
    return null;
  }

  public ArrayList<String> getFib() {
    return null;
  }
}
