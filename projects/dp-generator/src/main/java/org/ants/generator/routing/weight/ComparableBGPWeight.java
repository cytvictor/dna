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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
  }

  @Override
  public String toString() {
    return "ComparableBGPWeight{" +
        "inf=" + inf +
        "}";
  }

}
