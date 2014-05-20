import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew
 * Date: 17/05/14
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class States {
    private final Logger logger = Logger.getLogger(States.class.getName());
    private Integer numStates;
    private final ArrayList<String> stateNames = new ArrayList<String>();
    private final HashMap<String, Integer> stateIDs = new HashMap<String, Integer>();
    private final HashMap<Integer, HashMap<Integer,Integer>> transitions = new HashMap<Integer, HashMap<Integer, Integer>>();
    private final HashMap<Integer, Integer> transitionsCount = new HashMap<Integer, Integer>();

    public States(String fileName){
        logger.setLevel(Proj1.LOGGING_LEVEL);
        Scanner scanner;
        File file = new File(fileName);
        int i,j,k;
        HashMap<Integer, Integer> map;
        int count = 0;
        String state;
        Integer tCount;

        try {
            scanner = new Scanner(file);
            logger.info("1:" + toString());
            numStates = scanner.nextInt();
            scanner.nextLine();
            logger.info("2:" + toString());

            for(i = 0; i < numStates; i++){
                state = scanner.nextLine();
                stateNames.add(state);
                stateIDs.put(state, count);
                count++;
            }
            logger.info("3:" + toString());

            while(scanner.hasNextLine()){
                i = scanner.nextInt();
                j = scanner.nextInt();
                k = scanner.nextInt();
                if(transitions.containsKey(i)){
                    map = transitions.get(i);
                    if(map.containsKey(j)){
                        logger.info("Duplicate transition?");
                    }
                    map.put(j,k);
                } else {
                    map = new HashMap<Integer, Integer>();
                    transitions.put(i, map);
                    map.put(j,k);
                }
                if(transitionsCount.containsKey(i)){
                    tCount = transitionsCount.get(i);
                    tCount += k;
                    transitionsCount.put(i, tCount);
                } else {
                    transitionsCount.put(i,k);
                }
            }
            logger.info("4:" + toString());

        } catch (FileNotFoundException e) {
            System.out.println("State file not found...");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    // returns the natural log of the probability of state1 transitioning to state2 after add-1 smoothing
    public Double lnProbability(Integer state1, Integer state2){
        Double chance = 0d;
        Double lnChance;

        logger.info("BEGIN = " + getStateID("BEGIN"));
        logger.info("END = " + getStateID("END"));
        logger.info("state2 = " + state2);
        if(!(state2.equals(getStateID("BEGIN")) || state1.equals(getStateID("END")))){
            logger.info("If statement was true");
            if(transitions.containsKey(state1) && transitions.get(state1).containsKey(state2)){
                chance = (transitions.get(state1).get(state2) + 1d) / (transitionsCount.get(state1) + numStates - 1);
            } else if(transitionsCount.containsKey(state1)) {
                chance = 1d / (transitionsCount.get(state1) + numStates - 1);
            } else {
                chance = 1d / (numStates - 1);
            }
        }
        lnChance = Math.log(chance);
        logger.info("lnChance = " + lnChance);
        return lnChance;
    }

    public Integer getStateID(String state){
        Integer stateID = stateIDs.get(state);
        if(stateID == null){
            stateID = Proj1.UNKNOWN;
        }
        return stateID;
    }

    @Override
    public String toString() {
        return "States{" +
                "numStates=" + numStates +
                ", stateNames=" + stateNames +
                ", transitions=" + transitions +
                '}';
    }

    public Integer getNumStates() {
        return numStates;
    }
}
