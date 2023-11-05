package org.ants.generator.routing;

import org.ants.parser.datamodel.Prefix;
import org.ants.parser.relation.Node;

public class BgpRib {
  private Prefix prefix;
  private Node nextHop;
  private boolean isLocal;

  public BgpRib(Prefix prefix, Node nextHop) {
    this.prefix = prefix;
    this.nextHop = nextHop;
  }

  public BgpRib(Prefix prefix, Node nextHop, boolean isLocal) {
    this.prefix = prefix;
    this.nextHop = nextHop;
    this.isLocal = isLocal;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BgpRib) {
      BgpRib other = (BgpRib) obj;
      return prefix.equals(other.prefix) && nextHop.equals(other.nextHop) && isLocal == other.isLocal;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return prefix.hashCode() + nextHop.hashCode() + (isLocal ? 1 : 0);
  }

  public Prefix getPrefix() {
    return prefix;
  }

  public Node getNextHop() {
    return nextHop;
  }

  public boolean isLocal() {
    return isLocal;
  }

  @Override
  public String toString() {
    if (isLocal) {
      return "BgpRib{" +
          "isLocal=" + isLocal +
          '}';
    }
    return "BgpRib{" +
        "prefix=" + prefix +
        ", nextHop=" + nextHop +
        '}';
  }
}
