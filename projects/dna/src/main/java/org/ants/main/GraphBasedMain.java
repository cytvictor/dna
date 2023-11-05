package org.ants.main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.ants.generator.GraphBasedDPGenerator;
import org.ants.parser.ConfigParser;
import org.ants.parser.relation.Relation;
import org.ants.verifier.DPVerifier;

public class GraphBasedMain {
  private static final String currentPath = System.getProperty("user.dir");
  public static String workingPath;
  public static String testcase;

  public static ConfigParser parser;
  public static GraphBasedDPGenerator generator;
  public static DPVerifier verifier;

  public static void main(String[] args) throws IOException {
    // init("networks/example-network/base");
    // init("../Dynamic-APSP-Dataplane-Verification/networks/withdrawals/update");
    init("../Dynamic-APSP-Dataplane-Verification/networks/single-source-multipath/base");
    init("../Dynamic-APSP-Dataplane-Verification/networks/fattree/bgp/bgp_fattree04");

    // update("../Dynamic-APSP-Dataplane-Verification/networks/withdrawals/update");

  }

  public static void init(String configPath) throws IOException {
    configPath = Paths.get(configPath).toRealPath().toString();
    testcase = Paths.get(configPath).toRealPath().getFileName().toString();
    workingPath = Paths.get(currentPath, "results", testcase).toString();

    parser = new ConfigParser(testcase, configPath, workingPath);
    Map<String, List<Relation>> configUpdates = parser.getControlPlaneDiff(null);
    // HashSet<String> topos = parser.getTopology();
    // HashSet<String> edgePorts = parser.getEdgePorts();
    // Map<String, Map<String, Object>> dpDevices = parser.getDataPlaneModels();

    generator = new GraphBasedDPGenerator();
    ArrayList<String> fibUpdates = generator.generateFibUpdates(configUpdates);

    // verifier = new DPVerifier(testcase, new ArrayList<>(topos), new
    // ArrayList<>(edgePorts), dpDevices);
    // ArrayList<String> dpChanges = verifier.run(fibUpdates, null);
  }

  public static void update(String configPath) throws IOException {
    configPath = Paths.get(configPath).toRealPath().toString();
    parser.updateConfig(configPath);
    Map<String, List<Relation>> configUpdates = parser.getControlPlaneDiff(null);
    ArrayList<String> fibUpdates = generator.generateFibUpdates(configUpdates);
    // ArrayList<String> dpChanges = verifier.run(fibUpdates, null);
  }

}
