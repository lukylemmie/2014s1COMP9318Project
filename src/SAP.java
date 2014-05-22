import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Andrew on 20/05/2014.
 * SAP is acronym for "Sequence And Probability"
 */
public class SAP {
    private final Logger logger = Logger.getLogger(SAP.class.getName());
    private ArrayList<Integer> sequence;
    private Double lnProbability;
    private Integer lastState;

    public SAP(States states){
        logger.setLevel(Proj1.LOGGING_LEVEL);
        sequence = new ArrayList<Integer>();
        Integer startID = states.getStateID("BEGIN");
        sequence.add(startID);
        lastState = startID;
        lnProbability = 0d;
    }

    public SAP(ArrayList<Integer> sequence, Double lnProbability){
        this.sequence = sequence;
        this.lnProbability = lnProbability;
    }

    public SAP(SAP base){
        this.sequence = new ArrayList<Integer>(base.sequence);
        this.lnProbability = base.lnProbability;
        this.lastState = base.lastState;
    }

    // adds a state with the current token and updates the lnProbability
    public void addState(Integer stateID, Integer tokenID, States states, Symbols symbols){
        sequence.add(stateID);
        Double lnTransitionChance = states.lnProbability(lastState, stateID);
        Double lnEmissionChance = symbols.lnProbability(stateID, tokenID, states);
        lnProbability += lnTransitionChance + lnEmissionChance;
        lastState = stateID;
    }

    public ArrayList<Integer> getSequence() {
        return sequence;
    }

    public Double getLnProbability() {
        return lnProbability;
    }

    public void closeSAP(States states){
        Integer endID = states.getStateID("END");
        Double lnTransitionChance = states.lnProbability(lastState, endID);
        sequence.add(endID);
        lnProbability += lnTransitionChance;
        lastState = endID;
    }

    public void print(){
        String output = sequence.toString();
        output = output.replace("[","");
        output = output.replace("]","");
        output = output.replace(" ","");
        output += " " + lnProbability;

        System.out.println(output);
    }

    @Override
    public String toString() {
        return "SAP{" +
                "sequence=" + sequence +
                ", lnProbability=" + lnProbability +
                ", lastState=" + lastState +
                '}';
    }
}
