package org.ants.generator.algo;

public class TWeight implements Comparable<TWeight> {
    //constructor
    public TWeight(int cost) {
        this.cost = cost;
    }

    // //is infinity
    // public boolean isInfinity() {
    //     return cost == Integer.MAX_VALUE;
    // }


    //cost attribute
    private int cost;

    //get cost
    public int getCost() {
        return cost;
    }

    //compareTo method
    public int compareTo(TWeight that) {
        if (this.cost < that.cost) {
            return -1;
        } else if (this.cost > that.cost) {
            return 1;
        } else {
            return 0;
        }
    }

    //add method
    public TWeight add(TWeight that) {
        return new TWeight(this.cost + that.cost);
    }

    //subtract method
    public TWeight subtract(TWeight that) {
        return new TWeight(this.cost - that.cost);
    }
}
