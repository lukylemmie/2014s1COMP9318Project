import java.io.File;
import java.io.FileNotFoundException;
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

    public Symbols(String fileName){
        Scanner scanner;
        File file = new File(fileName);
        int i,j,k;

        try {
            scanner = new Scanner(file);

        } catch (FileNotFoundException e) {
            System.out.println("Symbol file not found...");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
