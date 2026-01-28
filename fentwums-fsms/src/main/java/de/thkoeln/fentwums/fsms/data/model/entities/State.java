package de.thkoeln.fentwums.fsms.data.model.entities;

import java.util.ArrayList;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The states of an state machine.
 *
 * @author Linus Schoendorf and Marvin Jolk
 */
public class State {

    private String name;
    private int width;
    private int height;

    private Point position;
    private Hashtable<Signal, Integer> outputVector;
    private ArrayList<Transition> transitions;

    private ArrayList<String> onEntry;

    public ArrayList<String> getOnEntry() {
        return onEntry;
    }

    public void setOnEntry(ArrayList<String> onEntry) {
        this.onEntry = onEntry;
    }

    public void addOnEntry(String variable, String expression) {
        onEntry.add(variable + " = " + expression);
    }

    public void addOnEntry(String onEntry) {
        this.onEntry.add(onEntry);
    }

    public Hashtable<Signal, Integer> getOutputVector() {
        return outputVector;
    }

    public void setOutputVector(Hashtable<Signal, Integer> outputVector) {
        this.outputVector = outputVector;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.name = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(ArrayList<Transition> transitions) {
        this.transitions = transitions;
    }

    /**
     * Creates a string containing the Output of a State.
     *
     * @return String which contains the Output
     */
    public String toOutputString() {
        String output = "State " + this.name;
        Enumeration e = outputVector.keys();
        while (e.hasMoreElements()) {
            Signal s = (Signal) e.nextElement();
            output += System.lineSeparator() + " \t Signal " + s.getName();
            int value = outputVector.get(s);
            output += " : " + value;
        }
        return output;
    }

    /**
     * Creates a string containing the Output of a State in XML.
     *
     * @return Output of an State as XML.
     * @author Marvin Jolk
     */
    public String toOutputXML() {
        String output = "\t\t\t<state id = \"" + this.name + "\">" + System.lineSeparator();
        Enumeration e = outputVector.keys();
        output += "\t\t\t\t<outputVectors>" + System.lineSeparator();
        while (e.hasMoreElements()) {
            Signal s = (Signal) e.nextElement();
            output += "\t\t\t\t\t<outputVector>" + System.lineSeparator();
            output += "\t\t\t\t\t\t<Signal name = \"" + s.getName() + "\"";
            int value = outputVector.get(s);
            output += " value = \"" + value + "\"/>" + System.lineSeparator();
            output += "\t\t\t\t\t</outputVector>" + System.lineSeparator();
        }
        output += "\t\t\t\t</outputVectors>" + System.lineSeparator();
        output += "\t\t\t</state>" + System.lineSeparator();
        return output;
    }
}
