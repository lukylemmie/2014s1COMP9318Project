import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew
 * Date: 17/05/14
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class States {
    private Integer numStates;
    private ArrayList<String> stateNames = new ArrayList<String>();
    private Integer[][] transitions;


    public States(String fileName){
        Scanner scanner;
        File file = new File(fileName);
        int i,j,k;


        try {
            scanner = new Scanner(file);
            numStates = scanner.nextInt();
            transitions = new Integer[numStates][numStates];

            for(i = 0; i < numStates; i++){
                stateNames.add(scanner.nextLine());
            }
            while(scanner.hasNextLine()){
                i = scanner.nextInt();
                j = scanner.nextInt();
                k = scanner.nextInt();
                transitions[i][j] = k;
                transitions[j][i] = k;
            }

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
                ", transitions=" + Arrays.toString(transitions) +
                '}';
    }
}
