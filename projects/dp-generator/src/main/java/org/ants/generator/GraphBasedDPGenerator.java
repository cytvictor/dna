package org.ants.generator;

import java.util.*;

import org.ants.generator.routing.Network;
import org.ants.parser.relation.Relation;

public class GraphBasedDPGenerator {

  public GraphBasedDPGenerator() {
  }

  private Network network = new Network();

  public ArrayList<String> generateFibUpdates(Map<String, List<Relation>> updates) {
    network.constructGraphFromUpdates(updates);
    // network.propagateRouteAdvertisementSSSP();
    return null;
  }

  public ArrayList<String> getFib() {
    return null;
  }
}
