import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Andrew on 22/05/2014.
 */
public class OldSearches {
    private final Logger logger = Logger.getLogger(OldSearches.class.getName());

    private MySAPList BFStopK(ArrayList<Integer> address, States states, Symbols symbols, int tokenIndex, SAP currentSAP, Integer k){
        MySAPList topK = new MySAPList(k);
        MySAPList nextTopK;
        Integer token = address.get(tokenIndex);
        for(int i = 0; i < states.getNumStates(); i++){
            SAP current = new SAP(currentSAP);
            current.addState(i, token, states, symbols);
            topK.add(current);
        }
        logger.info(address.toString() + " -- " + tokenIndex);
        if(tokenIndex + 1 < address.size()) {
            nextTopK = new MySAPList(k);
            ArrayList<SAP> saps = topK.getTopKList();
            for(SAP sap : saps){
                nextTopK.addAll(BFStopK(address, null, null, tokenIndex + 1, sap, k));
            }
            topK = nextTopK;
        } else {
            ArrayList<SAP> saps = topK.getTopKList();
            for(SAP sap : saps){
                sap.closeSAP(states);
            }
        }

        return topK;
    }

    private MySAPList DFStopK(ArrayList<Integer> address, States states, Symbols symbols, SAP currentSAP, Integer k){
        logger.info(address.toString());
        MySAPList topK = new MySAPList(k);
        Integer token = address.remove(0);
        logger.info(address.toString());

        for(int i = 0; i < states.getNumStates(); i++){
            ArrayList<Integer> copyAddress = new ArrayList<Integer>();
            logger.info(i + " : " + address.toString());
            for(Integer j : address){
                copyAddress.add(j);
            }
            SAP current = new SAP(currentSAP);
            current.addState(i, token, states, symbols);
            if(!copyAddress.isEmpty()){
                topK.addAll(DFStopK(copyAddress, null, null, current, k));
            } else {
                current.closeSAP(states);
                topK.add(current);
            }
            logger.info(i + " : " + topK.getTopKList());
        }

        return topK;
    }

    private class MySAPList{
        private final Logger logger = Logger.getLogger(MySAPList.class.getName());
        ArrayList<SAP> topKList;
        Integer k;

        private MySAPList(Integer k){
            logger.setLevel(Proj1.LOGGING_LEVEL);
            logger.setLevel(Level.ALL);
            topKList = new ArrayList<SAP>();
            this.k = k;
        }

        private void add(SAP sap){
            if (topKList.size() < k) {
                topKList.add(sap);
            } else {
                if(sap.getLnProbability() > topKList.get(k-1).getLnProbability()) {
                    boolean added = false;
                    int i = 0;
                    while (i < k && !added) {
                        if (sap.getLnProbability() > topKList.get(i).getLnProbability()) {
                            topKList.add(i, sap);
                            topKList.remove(k.intValue());
                            added = true;
                        }
                        i++;
                    }
                }
            }
        }

        private void addAll(MySAPList list){
            ArrayList<SAP> sapList = list.getTopKList();
            for(SAP sap : sapList) {
                add(sap);
            }
        }

        private void addAll(ArrayList<SAP> sapList){
            for(SAP sap : sapList) {
                add(sap);
            }
        }

        public ArrayList<SAP> getTopKList() {
            return topKList;
        }
    }

    public void calculateOutput(Integer k, States states, Symbols symbols, Queries queries, ArrayList<Integer> address) {
        ArrayList<ArrayList<Integer>> addresses = queries.getTokenIDs();
        HashMap<Integer, SAP> table = new HashMap<Integer, SAP>();
        HashMap<Integer, SAP> table2;
        for (int i = 0; i < states.getNumStates(); i++) {
            SAP starting = new SAP(states);
            table.put(i, starting);
        }
        if (address.equals(addresses.get(0))) {
            for (int i = 0; i < states.getNumStates(); i++) {
                table.get(i).print();
            }
            System.out.println("#############################################################");
        }
        for (Integer token : address) {
            table2 = new HashMap<Integer, SAP>();
            for (int i = 0; i < states.getNumStates(); i++) {
                SAP bestChance = null;
                for (int j = 0; j < states.getNumStates(); j++) {
                    SAP current = new SAP(table.get(j));
                    current.addState(i, token, states, symbols);
                    if (bestChance == null) {
                        bestChance = current;
                    } else {
                        if (current.getLnProbability() > bestChance.getLnProbability()) {
                            bestChance = current;
                        }
                    }
                }
                table2.put(i, bestChance);
            }
            table = table2;
            if (address.equals(addresses.get(0))) {
                for (int i = 0; i < states.getNumStates(); i++) {
                    table.get(i).print();
                }
                System.out.println("#############################################################");
            }
        }
        for (int i = 0; i < states.getNumStates(); i++) {
            table.get(i).closeSAP(states);
        }
        if (address.equals(addresses.get(0))) {
            for (int i = 0; i < states.getNumStates(); i++) {
                table.get(i).print();
            }
        }
    }
}
