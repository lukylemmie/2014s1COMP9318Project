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
    private Logger logger = Logger.getLogger(Symbols.class.getName());
    private Integer numSymbols;
    private ArrayList<String> symbolNames = new ArrayList<String>();
    private HashMap<String,Integer> symbolIDs = new HashMap<String, Integer>();
    private HashMap<Integer,HashMap<Integer,Integer>> emissions = new HashMap<Integer, HashMap<Integer, Integer>>();

    public Symbols(String fileName){
        logger.setLevel(Proj1.LOGGING_LEVEL);
        Scanner scanner;
        File file = new File(fileName);
        int i,j,k;
        HashMap<Integer, Integer> map;
        int count = 0;
        String symbol;

        try {
            scanner = new Scanner(file);
            logger.info("1:" + toString());
            numSymbols = scanner.nextInt();
            scanner.nextLine();
            logger.info("2:" + toString());

            for(i = 0; i < numSymbols; i++){
                symbol = scanner.nextLine();
                symbolNames.add(symbol);
                symbolIDs.put(symbol, new Integer(count));
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
            }
            logger.info("4:" + toString());

        } catch (FileNotFoundException e) {
            System.out.println("Symbol file not found...");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public String toString() {
        return "Symbols{" +
                "numSymbols=" + numSymbols +
                ", symbolNames=" + symbolNames +
                ", emissions=" + emissions +
                '}';
    }



//    private String emissionsData(){
//        String data = "\n";
//
//        if(emissions != null){
//            for(int[] iArray : emissions){
//                data += "{" + Arrays.toString(iArray) + "}\n";
//            }
//        }
//
//        return data;
//    }
}
