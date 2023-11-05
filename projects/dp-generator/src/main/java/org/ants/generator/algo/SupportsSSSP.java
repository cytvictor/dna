package org.ants.generator.algo;

import java.util.*;

public interface SupportsSSSP<TNode, TWeight extends Comparable<TWeight>> {
  public Map<TNode, List<Path<TNode, TWeight>>> findShortestPaths(Graph<TNode, TWeight> graph, TNode source,
      Path<TNode, TWeight> initialPath);
}
