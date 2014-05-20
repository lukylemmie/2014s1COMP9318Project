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
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Symbols {
    private final Logger logger = Logger.getLogger(Symbols.class.getName());
    private Integer numSymbols;
    private final ArrayList<String> symbolNames = new ArrayList<String>();
    private final HashMap<String,Integer> symbolIDs = new HashMap<String, Integer>();
    private final HashMap<Integer,HashMap<Integer,Integer>> emissions = new HashMap<Integer, HashMap<Integer, Integer>>();
    private final HashMap<Integer, Integer> emissionsCount = new HashMap<Integer, Integer>();

    public Symbols(String fileName){
        logger.setLevel(Proj1.LOGGING_LEVEL);
        Scanner scanner;
        File file = new File(fileName);
        int i,j,k;
        HashMap<Integer, Integer> map;
        int count = 0;
        String symbol;
        Integer eCount;

        try {
            scanner = new Scanner(file);
            logger.info("1:" + toString());
            numSymbols = scanner.nextInt();
            scanner.nextLine();
            logger.info("2:" + toString());

            for(i = 0; i < numSymbols; i++){
                symbol = scanner.nextLine();
                symbolNames.add(symbol);
                symbolIDs.put(symbol, count);
                count++;
            }
            logger.info("3:" + toString());

            while(scanner.hasNextLine()){
                i = scanner.nextInt();
                j = scanner.nextInt();
                k = scanner.nextInt();
                if(emissions.containsKey(i)){
                    map = emissions.get(i);
                    if(map.containsKey(j)){
                        logger.info("Duplicate transition?");
                    }
                    map.put(j,k);
                } else {
                    map = new HashMap<Integer, Integer>();
                    emissions.put(i, map);
                    map.put(j,k);
                }
                if(emissionsCount.containsKey(i)){
                    eCount = emissionsCount.get(i);
                    eCount += k;
                    emissionsCount.put(i, eCount);
                } else {
                    emissionsCount.put(i,k);
                }
            }
            logger.info("4:" + toString());

        } catch (FileNotFoundException e) {
            System.out.println("Symbol file not found...");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    // returns the natural log of the probability of state emitting symbol after add-1 smoothing
    public Double lnProbability(Integer state, Integer symbol, States states){
        Double chance = 0d;
        Double lnChance;

        if(!(state.equals(states.getStateID("BEGIN")) || state.equals(states.getStateID("END")))){
            if(emissions.containsKey(state) && emissions.get(state).containsKey(symbol)){
                chance = (emissions.get(state).get(symbol) + 1d) / (emissionsCount.get(state) + numSymbols + 1);
            } else if (emissionsCount.containsKey(state)) {
                chance = 1d / (emissionsCount.get(state) + numSymbols + 1);
            } else {
                chance = 1d / (numSymbols + 1);
            }
        }
        lnChance = Math.log(chance);
        logger.info("lnChance = " + lnChance);

        return lnChance;
    }

    public Integer getSymbolID(String symbol){
        Integer symbolID = symbolIDs.get(symbol);
        if(symbolID == null){
            symbolID = Proj1.UNKNOWN;
        }
        return symbolID;
    }

    @Override
    public String toString() {
        return "Symbols{" +
                "numSymbols=" + numSymbols +
                ", symbolNames=" + symbolNames +
                ", emissions=" + emissions +
                '}';
    }

    public Integer getNumSymbols() {
        return numSymbols;
    }
}
