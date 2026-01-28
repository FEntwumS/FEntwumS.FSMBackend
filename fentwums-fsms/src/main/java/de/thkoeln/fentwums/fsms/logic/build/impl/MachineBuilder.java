package de.thkoeln.fentwums.fsms.logic.build.impl;

import java.util.List;
import de.thkoeln.fentwums.fsms.data.datalogic.impl.InputVectorParser;
import de.thkoeln.fentwums.fsms.data.datalogic.impl.MachineParser;
import de.thkoeln.fentwums.fsms.data.datalogic.impl.TransitionHistory;
import de.thkoeln.fentwums.fsms.data.model.entities.Automaton;
import de.thkoeln.fentwums.fsms.data.model.entities.SignalVectorList;
import de.thkoeln.fentwums.fsms.data.model.entities.State;
import de.thkoeln.fentwums.fsms.logHandling.LogHandler;
import de.thkoeln.fentwums.fsms.logic.build.services.ILoadMachine;
import de.thkoeln.fentwums.fsms.logic.simulation.impl.Simulator;

/**
 * Builds the automaton and set up everything needed to run the simulation.
 *
 * @author Marvin Jolk, Linus Schoendorf
 */
public class MachineBuilder implements ILoadMachine {

    private Automaton automaton;
    Simulator simulator;
    private SignalVectorList signalVectorsList;
    private String transitionHistoryPath;

    private LogHandler errorHandler = LogHandler.getInstance();

    @Override
    public String getTransitionHistoryPath() {
        return transitionHistoryPath;
    }

    @Override
    public void setTransitionHistoryPath(String transitionHistoryPath) {
        this.transitionHistoryPath = transitionHistoryPath;
    }

    /**
     * Set the Automaton by Path.
     *
     * @param path Path of the automaton
     * @return success report
     * @author Marvin Jolk, Linus Schoendorf
     */
    @Override
    public boolean setAutomatonByPath(String path) {
        MachineParser machineParser = new MachineParser();
        if (path == null) {
            errorHandler.addError("Inputvectorpath is null");
            return false;
        }
        if ((automaton = machineParser.parseXMLFile(path)) == null) {
            errorHandler.addError(path + "is not a valid Path for Automaton");
            return false;
        }
        automaton.reset();
        return true;
    }

    /**
     * Set the Inputvector by Path.
     *
     * @param path Path of the inputvector
     * @return success report
     * @author Marvin Jolk, Linus Schoendorf
     */
    @Override
    public boolean setInputVectorByPath(String path) {
        InputVectorParser inputVectorParser = new InputVectorParser();
        if (path == null) {
            errorHandler.addError("Inputvectorpath is null");
            return false;
        }
        if (inputVectorParser.getInputVector(path) == null) {
            errorHandler.addError(path + "is not a valid Path for Inputvector");
            return false;
        }
        signalVectorsList = inputVectorParser.getInputVector(path);
        return true;
    }

    /**
     * Runs the simulation until there are no further InputVectors defined
     *
     * @return true if the run was successful and the simulation history was
     * saved in a file
     * @author Marvin Jolk, Linus Schoendorf
     */
    @Override
    public boolean runFullSimulation() {
        if (simulator == null) {
            errorHandler.addError("Simulator is null");
            return false;
        }
        if (simulator.getAutomaton() == null) {
            errorHandler.addError("Simulator does not have an automaton");
            return false;
        }
        signalVectorsList.resetIterator();
        simulator.getAutomaton().reset();
        if (!simulator.run(signalVectorsList)) {
            errorHandler.addError("Simulator can not run");
            return false;
        }
        if (transitionHistoryPath == null || transitionHistoryPath.equals("")) {
            return true;
        }
        return true;

    }

    /**
     * Runs a single step on the automaton.
     *
     * @return false when automaton was not set or there where no further
     * InputVectors defined, true otherwise
     * @author Marvin Jolk, Linus Schoendorf
     */
    @Override
    public boolean runSingleStep() {
        if (simulator == null) {
            errorHandler.addError("Simulator is not set");
            return false;
        }
        if (simulator.getAutomaton() == null) {
            errorHandler.addError("Simulator does not have an automaton");
            return false;
        }
        if (!signalVectorsList.nextStep()) {
            errorHandler.addWarning("No further input vectors defined");
            return false;
        }
        if (!simulator.runSingleStep()) {
            errorHandler.addError("Simulator can not run SingleStep");
        }

        return true;
    }

    /**
     * Creates a new simulator from the automaton and signalVectorList
     * attributes
     *
     * @return status of success, true if successful, false if not
     * @author Marvin Jolk, Linus Schoendorf
     */
    @Override
    public boolean setupSimulation() {
        simulator = new Simulator();
        if (automaton == null) {
            errorHandler.addError("Automaton is not set");
            return false;
        }
        simulator.setAutomaton(automaton);
        if (automaton.getInputSignals() == null) {
            errorHandler.addError("Automaton doesn't have Input Signals");
        }
        if (signalVectorsList == null) {
            errorHandler.addWarning("No Input Vectors are set");
            signalVectorsList = new SignalVectorList();
        }
        signalVectorsList.setSignals(automaton.getInputSignals());

        return true;
    }

    /**
     * Resets the simulator and the signalVectorList
     *
     * @author Marvin Jolk, Linus Schoendorf
     */
    @Override
    public void resetSimulator() {
        if (signalVectorsList != null) {
            signalVectorsList.resetIterator();
        } else {
            errorHandler.addWarning("SignalVector List is null. Could not reset it");
        }
        if (simulator != null) {
            simulator.reset();
        } else {
            errorHandler.addWarning("Simulator is null. Could not reset it");
        }
        TransitionHistory.resetHistory();
    }

    public List<State> getCurrentSaves() {
        return TransitionHistory.getCurrentSaves();
    }

    public Simulator getSimulator() {
        return simulator;
    }

    public SignalVectorList getInputVectors() {
        return signalVectorsList;
    }

    public void setInputVectors(SignalVectorList s) {
        signalVectorsList = s;
    }
}
