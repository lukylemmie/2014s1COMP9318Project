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
 * Time: 9:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Queries {
    private Logger logger = Logger.getLogger(Queries.class.getName());
    private ArrayList<ArrayList<String>> tokens = new ArrayList<ArrayList<String>>();

    public Queries(String fileName){
        logger.setLevel(Proj1.LOGGING_LEVEL);
        Scanner scanner;
        Scanner lineScanner;
        File file = new File(fileName);
        String line;
        ArrayList<String> tokenList;

        try {
            scanner = new Scanner(file);
            logger.info("1:" + toString());

            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                lineScanner = new Scanner(line);
                tokenList = new ArrayList<String>();
                while(lineScanner.hasNext()){
                    tokenList.add(lineScanner.next());
                }
                tokens.add(tokenList);
            }
            logger.info("2:" + toString());

        } catch (FileNotFoundException e) {
            System.out.println("Query file not found...");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public String toString() {
        return "Queries{" +
                "tokens=" + tokens +
                '}';
    }
}
