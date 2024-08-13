package org.ants.main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ants.generator.GraphBasedDPGenerator;
import org.ants.parser.ConfigParser;
import org.ants.parser.relation.Relation;
import org.ants.verifier.DPVerifier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GraphBasedMain {
  private static final String currentPath = System.getProperty("user.dir");
  public static String workingPath;
  public static String testcase;

  public static ConfigParser parser;
  public static GraphBasedDPGenerator generator;
  public static DPVerifier verifier;
  public static int configUpdateIdx;

  public static void main(String[] args) throws IOException {
    // init("networks/example-network/base");
    // init("../Dynamic-APSP-Dataplane-Verification/networks/withdrawals/update");
    // init("../Dynamic-APSP-Dataplane-Verification/networks/single-source-multipath/base", "../Dynamic-APSP-Dataplane-Verification/networks/single-source-multipath.json");
    // init("../Dynamic-APSP-Dataplane-Verification/networks/fattree/bgp/bgp_fattree04", "../Dynamic-APSP-Dataplane-Verification/networks/bgp_fattree04.json");
    // init("../Dynamic-APSP-Dataplane-Verification/networks/fattree/bgp/bgp_fattree08", "../Dynamic-APSP-Dataplane-Verification/networks/bgp_fattree08.json");
    // init("../Dynamic-APSP-Dataplane-Verification/networks/fattree/bgp/bgp_fattree20", "../Dynamic-APSP-Dataplane-Verification/networks/bgp_fattree20.json");
    // init("../Dynamic-APSP-Dataplane-Verification/networks/config2spec-networks/bics/bgp", "../Dynamic-APSP-Dataplane-Verification/networks/bgp_bics.json");
    // init("../Dynamic-APSP-Dataplane-Verification/networks/config2spec-networks/columbus/bgp", "../Dynamic-APSP-Dataplane-Verification/networks/bgp_columbus.json");
    // init("../Dynamic-APSP-Dataplane-Verification/networks/config2spec-networks/uscarrier/bgp", "../Dynamic-APSP-Dataplane-Verification/networks/bgp_uscarrier.json");
    configUpdateIdx = 0;

    init("../Dynamic-APSP-Dataplane-Verification/networks/withdrawals/base", "../Dynamic-APSP-Dataplane-Verification/networks/withdrawals-base.json");
    update("../Dynamic-APSP-Dataplane-Verification/networks/withdrawals/update", "../Dynamic-APSP-Dataplane-Verification/networks/withdrawals-update.json");
  }

  public static void exportUpdatesToJson(Map<String, List<Relation>> configUpdates, String saveJsonPath) {
    ArrayList<Map<String, Object>> jsonUpdates = new ArrayList<>();
    for (String updateType: configUpdates.keySet()) {
      for (Relation relation: configUpdates.get(updateType)) {
        Map<String, Object> relationMap = new LinkedHashMap<>();
        relationMap.put("UpdateType", updateType);
        relationMap.put("Relation", relation.getClass().getSimpleName());
        relationMap.put("Data", relation);
        jsonUpdates.add(relationMap);
      }
    }
    try {
      BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(saveJsonPath));
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      System.out.println("Writing configUpdates to file: " + saveJsonPath);
      writer.write(gson.toJson(jsonUpdates).toString());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void init(String configPath, String saveJsonPath) throws IOException {
    configPath = Paths.get(configPath).toRealPath().toString();
    testcase = Paths.get(configPath).toRealPath().getFileName().toString();
    workingPath = Paths.get(currentPath, "results", testcase).toString();

    parser = new ConfigParser(testcase, configPath, workingPath);
    Map<String, List<Relation>> configUpdates = parser.getControlPlaneDiff(null);

    exportUpdatesToJson(configUpdates, saveJsonPath);
    if (true) {
      return;
    }
    // export configUpdates to file end

    // HashSet<String> topos = parser.getTopology();
    // HashSet<String> edgePorts = parser.getEdgePorts();
    // Map<String, Map<String, Object>> dpDevices = parser.getDataPlaneModels();

    // start time
    long startTime = System.nanoTime();
    generator = new GraphBasedDPGenerator();
    ArrayList<String> fibUpdates = generator.generateFibUpdates(configUpdates);
    // end time
    long endTime = System.nanoTime();
    System.out.println("Time: " + (endTime - startTime) / 1000000 + "ms");

    // verifier = new DPVerifier(testcase, new ArrayList<>(topos), new
    // ArrayList<>(edgePorts), dpDevices);
    // ArrayList<String> dpChanges = verifier.run(fibUpdates, null);
  }

  public static void update(String configPath, String saveJsonPath) throws IOException {
    configPath = Paths.get(configPath).toRealPath().toString();
    parser.updateConfig(configPath);
    Map<String, List<Relation>> configUpdates = parser.getControlPlaneDiff(null);
    exportUpdatesToJson(configUpdates, saveJsonPath);
    ArrayList<String> fibUpdates = generator.generateFibUpdates(configUpdates);
    // ArrayList<String> dpChanges = verifier.run(fibUpdates, null);
  }

}
