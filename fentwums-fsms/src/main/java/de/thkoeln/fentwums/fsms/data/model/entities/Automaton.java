package de.thkoeln.fentwums.fsms.data.model.entities;

import java.util.ArrayList;
import java.util.stream.Collectors;
import de.thkoeln.fentwums.fsms.data.model.enums.Direction;

/**
 * The state machine.
 *
 * @author Linus Schoendorf and Marvin Jolk
 */
public class Automaton {

    private ArrayList<State> states;
    private ArrayList<Variable> variables;
    private ArrayList<Signal> signals;
    private State currentState;
    private String name;
    private State startState;
    private StartNode startNode;
    private String type;

    public Automaton() {
        this.states = new ArrayList<State>();
        this.variables = new ArrayList<Variable>();
        this.signals = new ArrayList<Signal>();
        this.name = "";
    }

    public ArrayList<Signal> getSignals() {
        return signals;
    }

    public void setSignals(ArrayList<Signal> signals) {
        this.signals = signals;
    }

    public State getStartState() {
        return startState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<Variable> variables) {
        this.variables = variables;
    }

    public void setStartState(State state) {
        startState = state;
    }

    public StartNode getStartNode() {
        return startNode;
    }

    public void setStartNode(StartNode startNode) {
        this.startNode = startNode;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    /**
     * Find a signal by name.
     *
     * @param sigName Name of the signal
     * @return The searched signal
     * @author Linus Schoendorf and Marvin Jolk
     */
    public Signal findSignalByName(String sigName) {
        return signals.stream().filter(s -> sigName.equals(s.getName())).findFirst().orElse(null);
    }

    /**
     * Find a state by name.
     *
     * @param stateName name of the State
     * @return The searched state
     * @author Linus Schoendorf and Marvin Jolk
     */
    public State findStateByName(String stateName) {
        return states.stream().filter(s -> stateName.equals(s.getName())).findFirst().orElse(null);
    }

    public ArrayList<Signal> getInputSignals() {
        return new ArrayList<>(signals.stream().filter(s -> s.getDir() == Direction.IN || s.getDir() == Direction.INOUT).collect(Collectors.toList()));
    }

    public ArrayList<Signal> getOutputSignals() {
        return new ArrayList<>(signals.stream().filter(s -> s.getDir() == Direction.OUT || s.getDir() == Direction.INOUT).collect(Collectors.toList()));
    }

    /**
     * Resets the state machine to the beginning before execution.
     *
     * @author Linus Schoendorf and Marvin Jolk
     */
    public void reset() {
        for (Variable variable : variables) {
            variable.setValue(0);
        }
        currentState = startState;
    }
}
