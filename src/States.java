import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Logger logger = Logger.getLogger(States.class.getName());
    private Integer numStates;
    private ArrayList<String> stateNames = new ArrayList<String>();
    private HashMap<Integer,HashMap<Integer,Integer>> transitions = new HashMap<Integer, HashMap<Integer, Integer>>();


    public States(String fileName){
        logger.setLevel(Proj1.LOGGING_LEVEL);
        Scanner scanner;
        File file = new File(fileName);
        int i,j,k;
        HashMap<Integer, Integer> map;

        try {
            scanner = new Scanner(file);
            logger.info("1:" + toString());
            numStates = scanner.nextInt();
            scanner.nextLine();
            logger.info("2:" + toString());

            for(i = 0; i < numStates; i++){
                stateNames.add(scanner.nextLine());
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
            }
            logger.info("4:" + toString());

        } catch (FileNotFoundException e) {
            System.out.println("State file not found...");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public String toString() {
        return "States{" +
                "numStates=" + numStates +
                ", stateNames=" + stateNames +
                ", transitions=" + transitions +
                '}';
    }

    //    @Override
//    public String toString() {
//        return "States{" +
//                "numStates=" + numStates +
//                ", stateNames=" + stateNames +
//                ", transitions=" + transitionsData() +
//                '}';
//    }
//
//    private String transitionsData(){
//        String data = "\n";
//
//        if(transitions != null){
//            for(int[] iArray : transitions){
//                data += "{" + Arrays.toString(iArray) + "}\n";
//            }
//        }
//
//        return data;
//    }
}
