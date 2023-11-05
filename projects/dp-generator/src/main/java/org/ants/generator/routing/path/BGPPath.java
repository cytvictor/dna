package org.ants.generator.routing.path;

import java.util.ArrayList;
import java.util.List;

import org.ants.generator.algo.Path;
import org.ants.parser.relation.BgpNetwork;
import org.ants.parser.relation.Node;

public class BGPPath<TNode, TWeight extends Comparable<TWeight>> extends Path<TNode, TWeight> {
  private BgpNetwork bgpNetwork;
  public ArrayList<Long> asPath;
  public long localPref;
  // TODO: implement and other properties that propagates with BGP message (e.g.
  // community).

  public BGPPath(TNode source, BgpNetwork network) {
    super(source);
    this.bgpNetwork = network;
  }

  public BGPPath(List<TNode> vertices, TWeight totalWeightight, BgpNetwork network) {
    super(vertices, totalWeightight);
    this.bgpNetwork = network;
  }

  @Override
  public Path<TNode, TWeight> extend(TNode destination, TWeight weight) {
    List<TNode> newVertices = new ArrayList<>(vertices);
    newVertices.add(destination);

    TWeight newTotalWeight = (totalWeight == null) ? weight : totalWeight;
    if (weight != null) {
      newTotalWeight = newTotalWeight.compareTo(weight) > 0 ? weight : newTotalWeight;
    }

    return new BGPPath<>(newVertices, newTotalWeight, bgpNetwork);
  }

  public BgpNetwork getBgpNetwork() {
    return bgpNetwork;
  }

  @Override
  public String toString() {
    String path = "Path{";
    for (int i = 0; i < vertices.size() - 1; i++) {
      path += ((Node) vertices.get(i)).node + "->";
    }
    path += ((Node) vertices.get(vertices.size() - 1)).node;

    return path + "}";
  }
}