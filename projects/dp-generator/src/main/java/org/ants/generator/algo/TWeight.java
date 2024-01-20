package org.ants.generator.algo;

import org.ants.generator.routing.weight.BGPAdjRib;
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

    public TWeight(TWeight w) {
        this.from = w.from;
        this.to = w.to;
        this.adjRib = w.adjRib;
        this.inf = w.inf;
        this.isLocal = w.isLocal;
        this.med = w.med;
        this.isEbgp = w.isEbgp;
    }

    public Node from;
    public Node to;
    public BGPAdjRib adjRib;
    public boolean inf;
    public boolean isLocal;
    public long med;
    public boolean isEbgp;

    // add method
    public TWeight add(TWeight _other) {
        // return new TWeight(this.getCost() + _other.getCost());
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
        //compare cost
        // if (this.cost != _other.cost) {
        //     return this.cost < _other.cost ? -1 : 1;
        // }
        // return 0;
        // System.out.println("Comparing from: " + this);
        // System.out.println("Comparing to: " + _other);
        // The BGP decision Process
        if (this.inf && _other.inf) {
            // System.out.println("    Result = from > to");
            return 1;
        }
        if (this.inf) {
            // System.out.println("    Result = from > to");
            return 1;
        }
        if (_other.inf) {
            // System.out.println("    Result = from < to");
            return -1;
        }

        // 1. Local preference
        // compare local_preference, higher local_preference is better, if not the same
        if (this.adjRib.localPref != _other.adjRib.localPref) {
            // System.out.println("    Result = " + (this.adjRib.localPref < _other.adjRib.localPref ? -1 : 1));
            return this.adjRib.localPref < _other.adjRib.localPref ? 1 : -1;
        }

        // 2. AS path length
        // compare as_path, if as_path is not the same, shorter as_path is better
        if (!this.adjRib.asPath.equals(_other.adjRib.asPath)) {
            // System.out.println("    Result = " + (this.adjRib.asPath.size() > _other.adjRib.asPath.size() ? -1 : 1));
            return this.adjRib.asPath.size() > _other.adjRib.asPath.size() ? 1 : -1;
        }

        // 3. Origin type: WIP
        // 4. MED
        // compare med, lower med is better
        if (this.med != _other.med) {
            // System.out.println("    Result = " + (this.med < _other.med ? -1 : 1));
            return this.med > _other.med ? 1 : -1;
        }

        // 5. eBGP over iBGP
        // compare isEbgp, eBGP is better
        if (this.isEbgp != _other.isEbgp) {
            // System.out.println("    Result = " + (this.isEbgp ? -1 : 1));
            return this.isEbgp ? -1 : 1;
        }

        // 6. IGP cost to BGP next hop: WIP
        // 7. Router ID
        // return 0;

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
