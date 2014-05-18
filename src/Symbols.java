import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew
 * Date: 17/05/14
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Symbols {
    private Integer numSymbols;
    private ArrayList<String> symbolNames = new ArrayList<String>();
    private Integer[][] emissions;

    public Symbols(String fileName){
        Scanner scanner;
        File file = new File(fileName);
        int i,j,k;

        try {
            scanner = new Scanner(file);
            numSymbols = scanner.nextInt();
            emissions = new Integer[numSymbols][numSymbols];

            for(i = 0; i < numSymbols; i++){
                symbolNames.add(scanner.nextLine());
            }
            while(scanner.hasNextLine()){
                i = scanner.nextInt();
                j = scanner.nextInt();
                k = scanner.nextInt();
                emissions[i][j] = k;
            }

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
                ", emissions=" + Arrays.toString(emissions) +
                '}';
    }
}
