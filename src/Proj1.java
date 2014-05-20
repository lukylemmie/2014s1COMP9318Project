import java.util.Arrays;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew
 * Date: 17/05/14
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Proj1 {
    public static final Level LOGGING_LEVEL = Level.ALL;
    static final Double EPSILON = 1.0E-4;
    static final Integer UNKNOWN = -1;

    public static void main(String[] args){
        System.out.println("args=" + Arrays.toString(args));
        States states = new States(args[0]);
        Symbols symbols = new Symbols(args[1]);
        Queries queries = new Queries(args[2]);
    }
}
