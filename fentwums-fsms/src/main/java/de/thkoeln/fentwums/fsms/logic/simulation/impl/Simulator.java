package de.thkoeln.fentwums.fsms.logic.simulation.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import de.thkoeln.fentwums.fsms.data.datalogic.impl.Expression;
import de.thkoeln.fentwums.fsms.data.datalogic.impl.TransitionHistory;
import de.thkoeln.fentwums.fsms.data.model.entities.Automaton;
import de.thkoeln.fentwums.fsms.data.model.entities.Operand;
import de.thkoeln.fentwums.fsms.data.model.entities.Signal;
import de.thkoeln.fentwums.fsms.data.model.entities.SignalVectorList;
import de.thkoeln.fentwums.fsms.data.model.entities.State;
import de.thkoeln.fentwums.fsms.data.model.entities.Transition;
import java.util.concurrent.ConcurrentHashMap;
import de.thkoeln.fentwums.fsms.data.datalogic.impl.Lexer;
import de.thkoeln.fentwums.fsms.data.datalogic.impl.Lexer.TOKEN;
import static de.thkoeln.fentwums.fsms.data.datalogic.impl.Lexer.TOKEN.ASSIGNMENT;
import static de.thkoeln.fentwums.fsms.data.datalogic.impl.Lexer.TOKEN.IDENTIFIER;
import de.thkoeln.fentwums.fsms.data.model.entities.Variable;
import de.thkoeln.fentwums.fsms.data.model.enums.Direction;
import de.thkoeln.fentwums.fsms.logHandling.LogHandler;
import de.thkoeln.fentwums.fsms.logic.simulation.services.ISimulation;

/**
 * Simulates the automaton
 *
 * @author Linus Schoendorf, Marvin Jolk, Markus de Koster
 */
public class Simulator implements ISimulation {

    private Automaton automaton;
    LogHandler errorHandler = LogHandler.getInstance();

    public Simulator() {
    }

    public Simulator(Automaton automaton) {
        this.automaton = automaton;
    }

    public Automaton getAutomaton() {
        return automaton;
    }

    public void setAutomaton(Automaton automaton) {
        this.automaton = automaton;
    }

    /**
     * Resets the automaton.
     *
     * @author Marvin Jolk, Linus Schoendorf
     */
    public void reset() {
        if (automaton != null) {
            automaton.reset();
        } else {
            errorHandler.addError("Can not reset. Automaton is null");
        }
    }

    /**
     * Runs a single step on the automaton.
     *
     * @return boolean value whether the simulation was successful or not
     * @author Linus Schoendorf, Marvin Jolk, Markus de Koster
     */
    @Override
    public boolean runSingleStep() {
        if (automaton == null) {
            errorHandler.addError("Can not run single step. Automaton is not set");
            return false;
        }
        try {
            State currentState = automaton.getCurrentState();
            ArrayList<State> states = automaton.getStates();
            ArrayList<Transition> transitions = currentState.getTransitions();

            ArrayList<Signal> inputSignals = automaton.getSignals();
            ArrayList<Variable> variables = automaton.getVariables();
            //remove all non input signals
            Iterator<Signal> it = inputSignals.iterator();
            while (it.hasNext()) {
                if (it.next().getDir() == Direction.OUT) {
                    it.remove();
                }
            }

            //find the correct transition of the current state
            String nextState = currentState.getName();
            // add all inputSignals to the inputVector

            ConcurrentHashMap<String, Integer> inputVector = new ConcurrentHashMap<>();
            for (Operand op : inputSignals) {
                inputVector.put(op.getName(), op.getValue());
            }
            // add all variables to the inputVector
            for (Operand op : variables) {
                inputVector.put(op.getName(), op.getValue());
            }

            // variable used to check for deterministic behaviour
            int conditionsMet = 0;

            for (Transition transition : transitions) {
                Expression expression = transition.getExpression();
                if (expression.evaluate(inputVector)) {
                    conditionsMet++;
                    //correct transition found
                    nextState = transition.getTarget();
                }
            }
            // error handling for non deterministic and non complete state machines
            if (conditionsMet == 0) {
                /* removed and implicit integrity assumed
                errorHandler.addWarning("No transition found for current set of input vectors."
                        + "\n state machine will remain in current state but is not complete!");
                 */

            } else if (conditionsMet > 1) {
                errorHandler.addError("Current set of input vectors results in transition to "
                        + conditionsMet + " states. State machine is non deterministic!");
            }
            State targetState;
            if (nextState == null || nextState == "") {

            }
            String nextStateName = nextState;
            try {
                targetState = states.stream().filter(x -> x.getName().equals(nextStateName)).findFirst().get();
            } catch (NoSuchElementException ex1) {
                errorHandler.addError("Could not find a state with the name \"" + nextStateName + "\".");
                return false;
            }
            if (targetState == null) {
                errorHandler.addError("Can not run single step. Target state " + nextStateName + " could not be found");
                return false;
            }
            //Execute Variable Operations
            if (!executeVarOps(targetState)) {
                errorHandler.addError("Could not execute Variable Operations of state: " + nextStateName);
                return false;
            }

            //Transition to new state
            automaton.setCurrentState(targetState);
            TransitionHistory.saveCurrentStep(automaton);
            return true;
        } catch (Exception e) {
            errorHandler.addError("Can not run single step. Something went wrong: " + e);
            return false;
        }
    }

    @Override
    /**
     * Runs Timed Finite State Machine Simulation until all input vectors have 
     * been used.
     *
     * @param signalVectorList mapping of signals to values
     * @return status of success of simulation. True if successful, false if not
     * @author Linus Schoendorf, Marvin Jolk
     */
    public boolean run(SignalVectorList signalVectorList) {
        if (automaton == null) {
            errorHandler.addError("Can not run. Automaton is not set");
            return false;
        }
        automaton.reset();
        while (signalVectorList.nextStep()) {
            if (!runSingleStep()) {
                errorHandler.addWarning("No further input vectors defined");
                return false;
            }
        }
        return true;
    }

    /**
     * Parses and executes Variable Operations such as "a = b + 3". 
     * Implementation for variable operations defined in STDE. Check user manual 
     * for allowed operations.
     *
     * @param state of which the variable operations shall be executed
     * @return status of execution. True if successful, false in error cases
     * @author Markus de Koster
     */
    private boolean executeVarOps(State state) {
        ArrayList<String> onEntry = state.getOnEntry();
        for (String varOp : onEntry) {
            Lexer lexer = new Lexer();
            lexer.setString((varOp + '\0').toCharArray()); //append limiter
            String targetVar; //first identifier e.g. 'a' for "a = 3"
            if (lexer.getToken() != IDENTIFIER) {
                errorHandler.addError("Variable Operation \"" + varOp + "\" could not be parsed."
                        + "\n Operation has to start with an Identifier");
                return false;
            }
            if ((targetVar = lexer.getIdentifier()) == null) {
                errorHandler.addError("Variable Operation \"" + varOp + "\" could not be parsed."
                        + "\n Left Variable not found");
                return false;
            }

            lexer.getNextToken(); // should be assignment sign '='
            TOKEN token = lexer.getToken();
            if (token != ASSIGNMENT) {
                errorHandler.addError("Variable Operation \"" + varOp + "\" could not be parsed."
                        + "\n " + token.toString() + " found after " + targetVar);
                return false;
            }
            lexer.getNextToken(); //either number, signal, variable or shift operation
            token = lexer.getToken();
            Operand nextOp;
            Variable var = null;
            for (Variable variable : automaton.getVariables()) {
                if (variable.getName().equals(targetVar)) {
                    var = variable;
                }
            }
            if (var == null) {
                errorHandler.addError("Variable \"" + targetVar + "\" not found.");
                return false;
            }
            int number;
            switch (token) {
                case NUMBER: //target Variable's value is changed to a number
                    number = lexer.getNumber();
                    var.assign(number);
                    break;
                case IDENTIFIER: //assign value of the variable / signal
                    //Name of variable or signal that is assigned
                    String identifier = lexer.getIdentifier();
                    int value = 0;
                    //check if there is an other token afterwards
                    lexer.getNextToken();
                    TOKEN nextToken = lexer.getToken();
                    switch (nextToken) {
                        case PLUS:
                            nextOp = automaton.getVariables().stream().filter(
                                    x -> x.getName().equals(identifier)).findFirst().get();
                            lexer.getNextToken();
                            number = lexer.getNumber();
                            number += nextOp.getValue();
                            var.assign(number);
                            break;
                        case MINUS:
                            nextOp = automaton.getVariables().stream().filter(
                                    x -> x.getName().equals(identifier)).findFirst().get();
                            lexer.getNextToken();
                            number = lexer.getNumber();
                            number = nextOp.getValue() - number;
                            var.assign(number);
                            break;
                        case END:
                            nextOp = automaton.getSignals().stream().filter(
                                    x -> x.getName().equals(identifier)).findFirst().get();
                            var.assign(nextOp.getValue());
                            break;
                        case NUMBER: //Lexer currently reads '-' signs as a number
                            // to fix this an identifying sign such as '#' should be used
                            // ahead of numbers that can be negative (signed int)
                            // --> change parsing to XML in STDE and Lexer
                            // this implementation is a temporary workaround
                            nextOp = automaton.getVariables().stream().filter(
                                    x -> x.getName().equals(identifier)).findFirst().get();
                            lexer.getNextToken();
                            number = lexer.getNumber();
                            number = nextOp.getValue() - number;
                            var.assign(number);
                            break;
                        default:
                            errorHandler.addError("Variable Operation \"" + varOp + "\" could not be parsed."
                                    + "\n " + nextToken.toString() + " found after " + token.toString());
                            return false;
                    }
                    break;
                case SHIFT_LEFT:
                    var.lshift(1);
                    break;
                case SHIFT_RIGHT:
                    var.rshift(1);
                    break;
                default:
                    errorHandler.addError("Variable Operation \"" + varOp + "\n could not be parsed."
                            + "\n " + token.toString() + " found after '='");
                    return false;
            }

        }
        return true;
    }

}
