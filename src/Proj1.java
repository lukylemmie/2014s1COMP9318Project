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
//            System.out.println("i = " + i + " : " + address);
//            if(i == 0){
//                logger.setLevel(Level.ALL);
//            } else {
//                logger.setLevel(Level.OFF);
//            }
            i++;
            SAP current = new SAP(states);
            ArrayList<SAP> topK = getTopK(address, current, k);
            for(SAP sap : topK){
                sap.print();
            }
        }
    }


    private ArrayList<SAP> getTopK(ArrayList<Integer> address, SAP currentSAP, Integer k){
        return useArrays(address, k);
    }


    private ArrayList<SAP> useArrays(ArrayList<Integer> address, Integer k){
        ArrayList<SAP> topK = new ArrayList<SAP>();
        ArrayList<Integer> topProb = new ArrayList<Integer>();
        Double lnProbabilities[][][] = new Double[k][states.getNumStates()][address.size() + 3];
        Integer previousStates[][][] = new Integer[k][states.getNumStates()][address.size() + 3];


        for(int i = 0; i < states.getNumStates(); i++) {
            lnProbabilities[0][i][0] = 0d;
            previousStates[0][i][0] = i;
        }

        for(int j = 0; j < address.size(); j++) {
            for (int i = 0; i < states.getNumStates(); i++) {
                for (int h = 0; h < states.getNumStates(); h++) {
                    Double lnTransitionChance = states.lnProbability(h, i);
                    Double lnEmissionChance = symbols.lnProbability(i, address.get(j), states);
                    Double lnProbability = lnTransitionChance + lnEmissionChance + lnProbabilities[0][h][j];
                    if (lnProbabilities[0][i][j + 1] == null) {
                        lnProbabilities[0][i][j + 1] = lnProbability;
                        previousStates[0][i][j + 1] = h;
                    } else if (lnProbabilities[0][i][j + 1] < lnProbability) {
                        lnProbabilities[0][i][j + 1] = lnProbability;
                        previousStates[0][i][j + 1] = h;

                    }
                }
            }
        }

        for (int i = 0; i < states.getNumStates(); i++) {
            for (int h = 0; h < states.getNumStates(); h++) {
                Double lnTransitionChance = states.lnProbability(h, states.getStateID("END"));
                Double lnProbability = lnTransitionChance + lnProbabilities[0][h][address.size()];
                if (lnProbabilities[0][i][address.size() + 1] == null) {
                    lnProbabilities[0][i][address.size() + 1] = lnProbability;
                    previousStates[0][i][address.size() + 1] = h;
                } else if (lnProbabilities[0][i][address.size() + 1] < lnProbability) {
                    lnProbabilities[0][i][address.size() + 1] = lnProbability;
                    previousStates[0][i][address.size() + 1] = h;

                }
            }
        }

        for (int i = 0; i < states.getNumStates(); i++) {
            lnProbabilities[0][i][address.size() + 2] = lnProbabilities[0][i][address.size() + 1];
            previousStates[0][i][address.size() + 2] = states.getStateID("END");
        }

        for (int i = 0; i < states.getNumStates(); i++) {
            int j;
            boolean added;
            if(topProb.isEmpty()){
                topProb.add(i);
            } else if(topProb.size() < k){
                j = 0;
                added = false;
                while(j < k && !added){
                    if(j >= topProb.size() || lnProbabilities[0][i][address.size() + 1] > lnProbabilities[0][topProb.get(j)][address.size() + 1]){
                        topProb.add(j, i);
                        added = true;
                    }
                    j++;
                }
            } else if(lnProbabilities[0][i][address.size() + 1] > lnProbabilities[0][topProb.get(k - 1)][address.size() + 1]){
                j = 0;
                added = false;
                while(j < k && !added){
                    if(lnProbabilities[0][i][address.size() + 1] > lnProbabilities[0][topProb.get(j)][address.size() + 1]){
                        topProb.add(j, i);
                        topProb.remove(k.intValue());
                        added = true;
                    }
                    j++;
                }
            }
        }

        for(int i = 0; i < topProb.size(); i++){
            ArrayList<Integer> sequence = new ArrayList<Integer>();
            Double lnProbability = lnProbabilities[0][topProb.get(i)][address.size() + 2];
            Integer previousState = previousStates[0][topProb.get(i)][address.size() + 2];
            sequence.add(0,previousState);
            previousState = previousStates[0][topProb.get(i)][address.size() + 1];
            sequence.add(0,previousState);
            for(int j = address.size(); j > 0; j--) {
                previousState = previousStates[0][previousState][j];
                sequence.add(0,previousState);
            }
            SAP sap = new SAP(sequence, lnProbability);
            topK.add(sap);
        }

        logger.info(toString2DArray(lnProbabilities, states.getNumStates(), address.size() + 3));
        logger.info(toString2DArray(previousStates, states.getNumStates(), address.size() + 3));

        return topK;
    }

    String toString2DArray(Double lnProbabilities[][][], int m, int n){
        String output = "";

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                output += lnProbabilities[0][i][j] + " ";
            }
            output += "\n";
        }

        return output;
    }

    String toString2DArray(Integer previousStates[][][], int m, int n){
        String output = "\n";

        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                output += previousStates[0][i][j] + " ";
            }
            output += "\n";
        }

        return output;
    }
}
