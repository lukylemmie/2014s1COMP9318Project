import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew
 * Date: 17/05/14
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Proj1 {
    private final Logger logger = Logger.getLogger(Proj1.class.getName());
    public static final Level LOGGING_LEVEL = Level.OFF;
    static final Double EPSILON = 1.0E-4;
    static final Integer UNKNOWN = -1;
    States states;
    Symbols symbols;
    Queries queries;

    public static void main(String[] args){
        Proj1 proj1 = new Proj1(args[0], args[1], args[2]);

        Integer k = new Integer(args[3]);

        proj1.calculateOutput(k);
    }

    public Proj1(String statesFile, String symbolsFile, String queriesFile){
        logger.setLevel(Proj1.LOGGING_LEVEL);
        states = new States(statesFile);
        symbols = new Symbols(symbolsFile);
        queries = new Queries(queriesFile);
        queries.convert2IDs(symbols);
    }

    public void calculateOutput(Integer k){
        ArrayList<ArrayList<Integer>> addresses = queries.getTokenIDs();

        int i = 0;
        for(ArrayList<Integer> address : addresses){
            System.out.println("i = " + i);
            i++;
            SAP current = new SAP(states);
            ArrayList<SAP> extraK = getTopK(address, current, states, symbols, k);
            MySAPList topK = new MySAPList(k);
            topK.addAll(extraK);
            for(SAP sap : topK.getTopKList()){
                sap.print();
            }
        }
    }

//            HashMap<Integer, SAP> table = new HashMap<Integer, SAP>();
//            HashMap<Integer, SAP> table2;
//            for(int i = 0; i < states.getNumStates(); i++){
//                SAP starting = new SAP(states);
//                table.put(i, starting);
//            }
//            if(address.equals(addresses.get(0))) {
//                for (int i = 0; i < states.getNumStates(); i++) {
//                    table.get(i).print();
//                }
//                System.out.println("#############################################################");
//            }
//            for(Integer token : address){
//                table2 = new HashMap<Integer, SAP>();
//                for(int i = 0; i < states.getNumStates(); i++){
//                    SAP bestChance = null;
//                    for(int j = 0; j < states.getNumStates(); j++){
//                        SAP current = new SAP(table.get(j));
//                        current.addState(i, token, states, symbols);
//                        if (bestChance == null) {
//                            bestChance = current;
//                        } else {
//                            if (current.getLnProbability() > bestChance.getLnProbability()) {
//                                bestChance = current;
//                            }
//                        }
//                    }
//                    table2.put(i, bestChance);
//                }
//                table = table2;
//                if(address.equals(addresses.get(0))) {
//                    for (int i = 0; i < states.getNumStates(); i++) {
//                        table.get(i).print();
//                    }
//                    System.out.println("#############################################################");
//                }
//            }
//            for(int i = 0; i < states.getNumStates(); i++){
//                table.get(i).closeSAP(states);
//            }
//            if(address.equals(addresses.get(0))) {
//                for (int i = 0; i < states.getNumStates(); i++) {
//                    table.get(i).print();
//                }
//            }


    private ArrayList<SAP> getTopK(ArrayList<Integer> address, SAP currentSAP, States states, Symbols symbols, Integer k){
        return BFStopK(address, 0, currentSAP, states, symbols, k).getTopKList();
    }

    //TODO: incomplete
    private MySAPList CheckPrevious(ArrayList<Integer> address, States states, Symbols symbols, Integer k){
        MySAPList topK = new MySAPList(k);
        ArrayList<ArrayList<SAP>> SAPs = new ArrayList<ArrayList<SAP>>();
        ArrayList<SAP> SAPsI = new ArrayList<SAP>();
        ArrayList<SAP> SAPsI2;

        for(int i = 0; i < states.getNumStates(); i++){
            SAPsI.add(new SAP(states));
        }
        SAPs.add(SAPsI);

        for(int i = 0; i < address.size(); i++){
            SAPsI = new ArrayList<SAP>();
            SAPsI2 = SAPs.get(i);

            for(int j = 0; j < states.getNumStates(); j++){

            }

            SAPs.add(SAPsI);
        }

        topK.addAll(SAPs.get(SAPs.size() - 1));

        return topK;
    }

    private MySAPList BFStopK(ArrayList<Integer> address, int tokenIndex, SAP currentSAP, States states, Symbols symbols, Integer k){
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
                nextTopK.addAll(BFStopK(address, tokenIndex + 1, sap, states, symbols, k));
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

    private MySAPList DFStopK(ArrayList<Integer> address, SAP currentSAP, States states, Symbols symbols, Integer k){
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
                topK.addAll(DFStopK(copyAddress, current, states, symbols, k));
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
}
