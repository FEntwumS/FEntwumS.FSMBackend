package de.thkoeln.fentwums.fsms.data.model.entities;

import java.util.ArrayList;
import java.util.Hashtable;
import de.thkoeln.fentwums.fsms.logHandling.LogHandler;

/**
 * A List of Vectors and the matching signals.
 *
 * @author Marvin Jolk, Linus Schoendorf
 */
public class SignalVectorList {

    private ArrayList<Hashtable<String, Integer>> signalsVectorList;
    private ArrayList<Signal> signals;
    private int signalIndex;
    LogHandler errorHandler = LogHandler.getInstance();

    public SignalVectorList() {
        signalsVectorList = new ArrayList<Hashtable<String, Integer>>();
        signalIndex = 0;
    }

    public ArrayList<Hashtable<String, Integer>> getSignalsVectorList() {
        return signalsVectorList;
    }

    public void setSignalsVectorList(ArrayList<Hashtable<String, Integer>> signalsVectorList) {
        this.signalsVectorList = signalsVectorList;
        signalIndex = 0;
    }

    /**
     * Inserts a vector at a given location.
     *
     * @param index location to insert the vector into
     * @return success report
     * @author Linus Schoendorf, Marvin Jolk
     */
    public boolean addVectorAt(int index) {
        if (index < 0 || index > signalsVectorList.size()) {
            errorHandler.addError("No vector selected");
            return false;
        }
        Hashtable<String, Integer> h = new Hashtable<>();
        for (int i = 0; i < signals.size(); i++) {
            h.put(signals.get(i).getName(), 0);
        }
        signalsVectorList.add(index, h);
        if (index < signalIndex) {
            signalIndex++;
        }
        return true;
    }

    /**
     * Modifies a vector at a given position.
     *
     * @param index index of the vector that shall be modified
     * @param vector new vector
     * @return success report
     * @author Marvin Jolk, Linus Schoendorf
     */
    public boolean changeVectorAt(int index, Hashtable<String, Integer> vector) {
        if (index < 0 || index >= signalsVectorList.size()) {
            errorHandler.addError("Input vector index out of bounds");
            return false;
        }
        if (vector == null) {
            errorHandler.addError("Cant change vector to null");
            return false;
        }
        signalsVectorList.set(index, vector);
        return true;
    }

    /**
     * Returns a vector at a given position.
     *
     * @param index index of the vector that shall be returned
     * @return the wanted vector.
     * @author Linus Schoendorf, Marvin Jolk
     */
    public Hashtable<String, Integer> getVectorAt(int index) {
        if (index >= signalsVectorList.size()) {
            errorHandler.addError("Can not get error at index " + index + ". Out of bounds.");
            return null;
        }
        return signalsVectorList.get(index);
    }

    public ArrayList<Signal> getSignals() {
        return signals;
    }

    public void setSignals(ArrayList<Signal> signals) {
        this.signals = signals;
    }

    /**
     * Goes one step further in the signal vector list.
     *
     * @return success report
     * @author Marvin Jolk, Linus Schoendorf
     */
    public boolean nextStep() {
        if (signalIndex >= signalsVectorList.size()) {
            return false;
        }
        Hashtable<String, Integer> hash = signalsVectorList.get(signalIndex++);
        Integer result;
        if (signals.isEmpty()) {
            errorHandler.addWarning("Signal List is empty");
            return true;
        }
        for (Signal signal : signals) {
            result = hash.get(signal.getName());
            if (result == null) {
                errorHandler.addError("Signal " + signal.getName() + " is not contained in signalVectorList");
                return false;
            }
            signal.setValue(result);
        }

        return true;
    }

    /**
     * Resets the index.
     *
     * @author Marvin Jolk, Linus Schoendorf
     */
    public void resetIterator() {
        signalIndex = 0;
    }

    /**
     * Removes a vector at a given position.
     *
     * @param index index of the vector that shall be removed
     * @return success report
     * @author Marvin Jolk, Linus Schoendorf
     */
    public boolean removeVectorAt(int index) {
        if (index < 0 || index >= signalsVectorList.size()) {
            errorHandler.addError("No vector selected");
            return false;
        }
        signalsVectorList.remove(index);
        if (index < signalIndex) {
            signalIndex--;
        }
        return true;
    }
}
