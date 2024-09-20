package org.ants.exp;

import org.ants.parser.relation.Interface;
import org.ants.parser.relation.Relation;
import org.ants.parser.relation.neighbor.EBgpNeighbor;
import org.ants.main.DNA;

import java.io.IOException;
import java.util.*;

/*
  Run link failure evaluation
 */
public class EvalLinkFailure {
    public ExpHelper helper;

    public EvalLinkFailure(String configPath) throws IOException{
        helper = new ExpHelper(configPath, 1);
    }

    public void run1LinkFailureUpdate() throws IOException {
        List<Relation> intfRels = helper.baseRelations.get(Interface.class.getSimpleName());
        Map<String, Relation> intfRelMap = new HashMap<>();
        for (Relation intfRel : intfRels) {
            Interface intf = (Interface) intfRel;
            String relKey = String.format("%s:%s", intf.node.node, intf.intf);
            intfRelMap.put(relKey, intf);
        }
        Set<String> avoidRepeat = new HashSet<>();
        List<List<String>> links = new ArrayList<>();
        for (String link : DNA.parser.getTopology()) {
            String[] items = link.split(" ");
            String intf1 = String.format("%s:%s", items[0], items[1]);
            String intf2 = String.format("%s:%s", items[2], items[3]);
            if (avoidRepeat.contains(String.format("%s+%s", intf1, intf2)))
                continue;
            links.add(Arrays.asList(intf1, intf2));
            avoidRepeat.add(String.format("%s+%s", intf1, intf2));
            avoidRepeat.add(String.format("%s+%s", intf2, intf1));
        }

        List<ExpRecord> res = new ArrayList<>();
        List<Relation> oldList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            System.out.println(i);
            List<String> link = links.get(i);
            Interface intfRel1 = (Interface) intfRelMap.get(link.get(0));
            Interface intfRel2 = (Interface) intfRelMap.get(link.get(1));
            List<Relation> relList = new ArrayList<>();
            relList.add(intfRel1);
            relList.add(intfRel2);

            Map<String, List<Relation>> updates = new HashMap<>();
            if (i == 0) {
                updates.put("delete", relList);
            } else {
                updates.put("insert", oldList);
                updates.put("delete", relList);
            }
            res.add(helper.runTransaction(String.format("change_update_%d", i + 1), updates));
            oldList = new ArrayList<>(relList);
        }
        helper.saveResult(DNA.testcase + "_lf", res);
        helper.bookSumTime("LinkFailure1", res);
    }
    public static Random rand = new Random(System.currentTimeMillis());

    public void runNLinkFailureUpdate(int n) throws IOException {
        List<Relation> neighborsRels = helper.baseRelations.get(EBgpNeighbor.class.getSimpleName());

        List<ExpRecord> res = new ArrayList<>();
        // List<Relation> oldList = new ArrayList<>();
        for (int k = 0; k < 1; k++)
        for (int i = 0; i < neighborsRels.size() && i <= 50; i+=1) {
            System.out.println(k + "/" + i);
            
            List<Relation> relList = new ArrayList<>();
            for (int j = 0; j < i * 10; j++) {
                while (true) {
                    // EBgpNeighbor intfRel = (EBgpNeighbor) neighborsRels.get(rand.nextInt(neighborsRels.size()));
                    EBgpNeighbor intfRel = (EBgpNeighbor) neighborsRels.get(j);
                    if (!relList.contains(intfRel)) {
                        // System.out.println(intfRel);
                        relList.add(intfRel);
                        break;
                    }
                }
            }

            Map<String, List<Relation>> updates = new HashMap<>();
            Map<String, List<Relation>> updatesRestore = new HashMap<>();
            updates.put("delete", relList);
            updatesRestore.put("insert", relList);
            res.add(helper.runTransaction(String.format("change_update_%d_%d", i + 1, k), updates));
            res.add(helper.runTransaction(String.format("change_restore_%d_%d", i + 1, k), updatesRestore));
        }
        helper.saveResult(DNA.testcase + "_n_lf", res);
        helper.bookSumTime("NLinkFailure", res);
    }

    public static void main(String[] args) throws IOException {
        String configPath = args[0];
        // String configPath = "../networks/fattree/bgp/bgp_fattree04";

        EvalLinkFailure evalLinkFailure = new EvalLinkFailure(configPath);
        // evalLinkFailure.run1LinkFailureUpdate();
        evalLinkFailure.runNLinkFailureUpdate(5);
        evalLinkFailure.helper.summaryTime();
    }
}
