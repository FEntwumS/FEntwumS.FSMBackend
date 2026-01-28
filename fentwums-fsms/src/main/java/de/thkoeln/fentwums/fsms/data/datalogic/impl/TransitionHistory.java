package de.thkoeln.fentwums.fsms.data.datalogic.impl;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import de.thkoeln.fentwums.fsms.data.model.entities.Automaton;
import de.thkoeln.fentwums.fsms.data.model.entities.State;
import de.thkoeln.fentwums.fsms.logHandling.LogHandler;

/**
 * Writes the transition history to output files.
 *
 * @author Linus Schoendorf, Markus de Koster und Marvin Jolk
 */
public class TransitionHistory {

    private TransitionHistory() {
    }

    static List<State> stateList = new ArrayList<State>();

    /**
     * Save the current step.
     *
     * @param automaton automaton in its current state
     * @return success report
     * @author Linus Schoendorf, Markus de Koster und Marvin Jolk
     */
    static public boolean saveCurrentStep(Automaton automaton) {
        State state = automaton.getCurrentState();
        if (state == null) {
            LogHandler errorHandler = LogHandler.getInstance();
            errorHandler.addError("The automaton doesn't have a current State. Could not save current Step");
            return false;
        }
        stateList.add(state);
        return true;
    }

    /**
     * Writes the saved steps to a text file.
     *
     * @param fileName name of the text file
     * @return success report
     * @author Marvin Jolk, Markus de Koster, Linus Schoendorf
     */
    static public boolean saveToTXT(String fileName) {
        BufferedWriter bw;
        LogHandler errorHandler = LogHandler.getInstance();
        try {
            bw = new BufferedWriter(new PrintWriter(fileName));
        } catch (FileNotFoundException ex1) {
            errorHandler.addError("File to save output not found");
            return false;
        }
        try {
            int stepCounter = 0;
            for (State state : stateList) {
                String output = "Step " + (stepCounter++ + 1) + ": " + state.toOutputString() + System.lineSeparator() + System.lineSeparator();
                bw.write(output);
            }
            bw.close();
        } catch (IOException ex1) {
            errorHandler.addError("Could not write history to txt");
            return false;
        }

        return true;
    }

    /**
     * Writes the saved steps to a XML file.
     *
     * @param fileName name of the xml file
     * @return success report
     * @author Marvin Jolk
     */
    static public boolean saveToXML(String fileName) {

        BufferedWriter bw;
        LogHandler errorHandler = LogHandler.getInstance();
        try {
            bw = new BufferedWriter(new PrintWriter(fileName));
        } catch (FileNotFoundException ex1) {
            errorHandler.addError("File to save output not found");
            return false;
        }
        try {
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + System.lineSeparator());
            int stepCounter = 0;
            bw.write("<history>" + System.lineSeparator());
            bw.write("\t<steps>" + System.lineSeparator());
            for (State state : stateList) {
                String output = "\t\t<step number = " + (stepCounter++ + 1) + " >" + System.lineSeparator();
                output += state.toOutputXML();
                output += "\t\t</step>" + System.lineSeparator();
                bw.write(output);
            }
            bw.write("\t</steps>" + System.lineSeparator());
            bw.write("</history>");
            bw.close();
        } catch (IOException ex1) {
            errorHandler.addError("Could not write history to txt");
            return false;
        }

        return true;
    }

    /**
     * Return all current saved states.
     *
     * @return List of saved states
     * @author Linus Schoendorf, Markus de Koster und Marvin Jolk
     */
    static public List<State> getCurrentSaves() {
        return stateList;
    }

    /**
     * Resets the history.
     *
     * @author Linus Schoendorf, Markus de Koster und Marvin Jolk
     */
    static public void resetHistory() {
        stateList = new ArrayList<>();
    }
}
