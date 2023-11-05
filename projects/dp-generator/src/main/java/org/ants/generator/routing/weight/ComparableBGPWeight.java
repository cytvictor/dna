package org.ants.generator.routing.weight;

public class ComparableBGPWeight implements Comparable<ComparableBGPWeight> {

  private boolean inf;

  public ComparableBGPWeight(boolean inf) {
    this.inf = inf;
  }

  public boolean isInf() {
    return inf;
  }

  @Override
  public int compareTo(ComparableBGPWeight o) {

    if (this.inf && o.inf) {
      return 0;
    }
    if (this.inf)
      return 1;
    // if (o.inf) {
    return -1;
    // }

    // The BGP decision Process
    // 1. Local preference
    // 2. AS path length
    // 3. Origin type
    // 4. MED
    // 5. eBGP over iBGP
    // 6. IGP cost to BGP next hop
    // 7. Router ID
    // return 0;
  }

  @Override
  public String toString() {
    return "ComparableBGPWeight{" +
        "inf=" + inf +
        "}";
  }

}
