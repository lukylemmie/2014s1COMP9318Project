import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Integer[][] emissions;

    public Symbols(String fileName){
        logger.setLevel(Proj1.LOGGING_LEVEL);
        Scanner scanner;
        File file = new File(fileName);
        int i,j,k;

        try {
            scanner = new Scanner(file);
            logger.info("1:" + toString());
            numSymbols = scanner.nextInt();
            scanner.nextLine();
            logger.info("2:" + toString());
            emissions = new Integer[numSymbols][numSymbols];

            for(i = 0; i < numSymbols; i++){
                symbolNames.add(scanner.nextLine());
            }
            logger.info("3:" + toString());

            while(scanner.hasNextInt()){
                i = scanner.nextInt();
                j = scanner.nextInt();
                k = scanner.nextInt();
                emissions[i][j] = k;
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
                ", emissions=" + emissionsData() +
                '}';
    }

    private String emissionsData(){
        String data = "";

        if(emissions != null){
            for(Integer[] iArray : emissions){
                data = "{" + Arrays.toString(iArray) + "}";
            }
        }

        return data;
    }
}
