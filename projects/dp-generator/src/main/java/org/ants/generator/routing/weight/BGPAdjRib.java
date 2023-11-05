package org.ants.generator.routing.weight;

import java.util.ArrayList;

import org.ants.parser.relation.BgpNetwork;

public class BGPAdjRib {
  public BgpNetwork bgpNetwork;
  public ArrayList<Long> asPath;
  public long localPref;

  public BGPAdjRib(BgpNetwork bgpNetwork, ArrayList<Long> asPath, long localPref) {
    this.bgpNetwork = bgpNetwork;
    this.asPath = asPath;
    this.localPref = localPref;
  }

  public String toString() {
    return "BGPAdjRib(" + bgpNetwork.prefix + ", " + asPath + ", " + localPref + ")";
  }
}
