import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Andrew
 * Date: 17/05/14
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Proj1 {
    static final Double EPSILON = 1.0E-4;

    public static void main(String[] args){
        System.out.println("args=" + Arrays.toString(args));
        States states = new States(args[1]);
        Symbols symbols = new Symbols(args[2]);
        Queries queries = new Queries(args[3]);

        System.out.println(states.toString());
        System.out.println(symbols.toString());
        System.out.println(queries.toString());
    }
}
