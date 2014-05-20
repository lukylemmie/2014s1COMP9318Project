import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew
 * Date: 17/05/14
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Proj1 {
    public static final Level LOGGING_LEVEL = Level.OFF;
    static final Double EPSILON = 1.0E-4;
    static final Integer UNKNOWN = -1;
    States states;
    Symbols symbols;
    Queries queries;

    public static void main(String[] args){
        Proj1 proj1 = new Proj1(args[0], args[1], args[2]);

        Integer k = Integer.getInteger(args[3]);

        proj1.calculateOutput(k);
    }

    public Proj1(String statesFile, String symbolsFile, String queriesFile){
        states = new States(statesFile);
        symbols = new Symbols(symbolsFile);
        queries = new Queries(queriesFile);
        queries.convert2IDs(symbols);
    }

    public void calculateOutput(Integer k){
        ArrayList<ArrayList<Integer>> tokenIDs = queries.getTokenIDs();

        for(ArrayList<Integer> address : tokenIDs){
            HashMap<Integer, SAP[]> table = new HashMap<Integer, SAP[]>();
            HashMap<Integer, SAP[]> table2;
            for(int i = 0; i < states.getNumStates(); i++){
                SAP[] startings = new SAP[k];
                for(int j = 0; j < k; j++) {
                    SAP starting = new SAP(states);
                    startings[j] = starting;
                }
                table.put(i, startings);
            }
            if(address.equals(tokenIDs.get(0))) {
                for (int i = 0; i < states.getNumStates(); i++) {
                    table.get(i).print();
                }
                System.out.println("#############################################################");
            }
            for(Integer token : address){
                table2 = new HashMap<Integer, SAP[]>();
                for(int i = 0; i < states.getNumStates(); i++){
                    SAP[] bestChance = new SAP[k];
                    for(int j = 0; j < states.getNumStates(); j++){
                        SAP current = new SAP(table.get(j));
                        current.addState(i, token, states, symbols);
                        int a = 0;
                        boolean notFound = false;
                        while(a < k && !notFound) {
                            if (bestChance == null) {
                                bestChance = current;
                            } else {
                                if (current.getLnProbability() > bestChance.getLnProbability()) {
                                    bestChance = current;
                                }
                            }
                        }
                    }
                    table2.put(i, bestChance);
                }
                table = table2;
                if(address.equals(tokenIDs.get(0))) {
                    for (int i = 0; i < states.getNumStates(); i++) {
                        table.get(i).print();
                    }
                    System.out.println("#############################################################");
                }
            }
            for(int i = 0; i < states.getNumStates(); i++){
                table.get(i).closeSAP(states);
            }
            if(address.equals(tokenIDs.get(0))) {
                for (int i = 0; i < states.getNumStates(); i++) {
                    table.get(i).print();
                }
            }
        }
    }
}
