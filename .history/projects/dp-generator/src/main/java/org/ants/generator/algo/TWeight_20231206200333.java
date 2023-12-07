package org.ants.generator.algo;

import org.ants.generator.routing.weight.BGPAdjRib;
import org.ants.generator.routing.weight.BGPWeight;
import org.ants.parser.relation.Node;

public class TWeight implements Comparable<TWeight> {
    // constructor
    public TWeight(int cost) {
        this.cost = cost;
    }

    public TWeight(Node from, Node to, BGPAdjRib adjRib, boolean inf, boolean isLocal) {
        this.from = from;
        this.to = to;
        this.adjRib = adjRib;
        this.inf = inf;
        this.isLocal = isLocal;
    }

    public Node from;
    public Node to;
    public BGPAdjRib adjRib;
    public boolean inf;
    public boolean isLocal;

    // add method
    public TWeight add(TWeight _other) {
        if (this.inf) {
            return this;
        }
        if (_other.inf)
            return _other;

        TWeight newWeight = new TWeight(this.from, this.to, null, false, false);
        BGPAdjRib newAdjRib = new BGPAdjRib(this.adjRib.bgpNetwork, this.adjRib.asPath, this.adjRib.localPref);
        newWeight.adjRib = newAdjRib;

        newWeight.adjRib.asPath.add(_other.from.as);
        newWeight.from = this.from;
        newWeight.to = _other.to;

        return newWeight;
    }

    // //is infinity
    // public boolean isInfinity() {
    // return cost == Integer.MAX_VALUE;
    // }

    // cost attribute
    private int cost;

    // get cost
    public int getCost() {
        return cost;
    }

    // compareTo method
    public int compareTo(TWeight _other) {
        if (this.inf && _other.inf) {
            return 1;
        }
        if (this.inf)
            return 1;
        if (_other.inf) {
            return -1;
        }

        // compare as_path, if as_path is not the same, shorter as_path is better
        if (!this.adjRib.asPath.equals(_other.adjRib.asPath)) {
            return this.adjRib.asPath.size() > _other.adjRib.asPath.size() ? -1 : 1;
        }

        // compare local_preference, higher local_preference is better, if not the same
        if (this.adjRib.localPref != _other.adjRib.localPref) {
            return this.adjRib.localPref < _other.adjRib.localPref ? -1 : 1;
        }

        // compare lexical order
        return 1;
    }

    // subtract method
    public TWeight subtract(TWeight that) {
        return new TWeight(this.cost - that.cost);
    }

    public String toString() {
        return "TWeight(" + this.from + "," + this.to + "," + this.adjRib + ",inf=" + this.inf + ",isLocal="
                + this.isLocal + ")";
    }
}
