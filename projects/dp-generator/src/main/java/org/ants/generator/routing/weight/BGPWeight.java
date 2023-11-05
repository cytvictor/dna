package org.ants.generator.routing.weight;

import org.ants.parser.relation.Node;

public class BGPWeight {
  private Node from;
  private Node to;
  private BGPAdjRib adjRib;
  public boolean inf;
  public boolean isLocal;

  public BGPWeight(Node from, Node to, BGPAdjRib adjRib, boolean inf, boolean isLocal) {
    this.from = from;
    this.to = to;
    this.adjRib = adjRib;
    this.inf = inf;
    this.isLocal = isLocal;
  }

  // add
  public BGPWeight plus(BGPWeight _other) {
    if (this.inf) {
      return this;
    }
    if (_other.inf)
      return _other;

    BGPWeight newWeight = new BGPWeight(this.from, this.to, null, false, false);
    BGPAdjRib newAdjRib = new BGPAdjRib(this.adjRib.bgpNetwork, this.adjRib.asPath, this.adjRib.localPref);
    newWeight.adjRib = newAdjRib;

    newWeight.adjRib.asPath.add(_other.from.as);
    newWeight.from = this.from;
    newWeight.to = _other.to;

    return newWeight;
  }

  public boolean lowerThan(BGPWeight _other) {
    if (this.inf && _other.inf) {
      return false;
    }
    if (this.inf)
      return false;
    if (_other.inf) {
      return true;
    }

    // compare as_path, if as_path is not the same, shorter as_path is better
    if (!this.adjRib.asPath.equals(_other.adjRib.asPath)) {
      return this.adjRib.asPath.size() > _other.adjRib.asPath.size();
    }

    // compare local_preference, higher local_preference is better, if not the same
    if (this.adjRib.localPref != _other.adjRib.localPref) {
      return this.adjRib.localPref < _other.adjRib.localPref;
    }

    // compare lexical order
    return false;
  }

  public String toString() {
    return "BGPWeight(" + this.from + "," + this.to + "," + this.adjRib + ",inf=" + this.inf + ",isLocal="
        + this.isLocal + ")";
  }
}
