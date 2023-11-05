package org.ants.generator.algo;

import java.util.*;

public interface SupportsAPSP<TNode, TWeight extends Comparable<TWeight>> {
  public Map<TNode, Map<TNode, Path<TNode, TWeight>>> findAllShortestPaths(Graph<TNode, TWeight> graph);
}
