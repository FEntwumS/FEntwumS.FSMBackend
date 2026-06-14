/*
 * STDE - State Transition Diagram Editor
 *
 * 2011, 2012 Jan Montag, Andreas Schwenk
 *
 * Component:   Graph
 * Class:       SigVar
 * Created:     2011-10-28
 */

package de.thkoeln.fentwums.fsm.graph;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Abstract superclass for signals (Sig) and variables (Var).
 * Common information like name, description, type are stored in this class.
 * 
 * File-operations (loading, saving) for these common properties are provided.
 * 
 * Signal:   Signals describe the interface between the model and the world outside.
 * 
 * Variable: Variables allow states to be "countable". That means a state can be
 *           traversed more than once - for example for a serial transmitter / receiver.
 *           This repetitive behavior is controlled by variables.
 * 
 * @author Andreas Schwenk
 */
public abstract class SigVar
{
    // *** ENUMARATIONS ***
    public enum SIGVAR_TYPE {BIT, BIT_N, SIGNED, UNSIGNED,VECTOR}; // type
    
    // *** ATTRIBUTES ***
    protected String name;
    protected String description;
    protected SIGVAR_TYPE type;
    protected int bitLength=8;
    
    protected Graph parent; // owner

    // *** METHODS ***
    
    /**
     * gets the bit-length of the signal/variable
     * 
     * @return bit-length of the signal/variable
     * 
     * @author Andreas Schwenk
     */
    public int getBitLength() {
        return bitLength;
    }

    /**
     * sets the bit-length of the signal/variable
     * 
     * @param bitLength the new bit-length of the signal/variable
     * 
     * @author Andreas Schwenk
     */
    public void setbitLength(int bitLength) {
        this.bitLength = bitLength;
    }

    /**
     * gets the description of the signal/variable
     * 
     * @return description of the signal/variable
     * 
     * @author Andreas Schwenk
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description of the signal/variable
     * 
     * @param description the new description of the signal/variable
     * 
     * @author Andreas Schwenk
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * gets the name of the signal/variable
     * 
     * @return name of the signal/variable
     * 
     * @author Andreas Schwenk
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the signal/variable
     * 
     * @param name the name of the signal/variable
     * 
     * @author Andreas Schwenk
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the type of the signal/variable
     * 
     * @return type of the signal/variable
     * 
     * @author Andreas Schwenk
     */
    public SIGVAR_TYPE getType() {
        return type;
    }

    /**
     * sets the type of the signal/variable
     * 
     * @param type new type of the signal/variable
     * 
     * @author Andreas Schwenk
     */
    public void setType(SIGVAR_TYPE type) {
        this.type = type;
    }
    
    /**
     * Loads a SigVar from given file (data-input-stream).
     * Only common properties of this super-class are loaded. There is
     * necessity in loading all further parameters in derivation of abstract
     * method: "loadFromFile(..)".
     * 
     * @param in data-stream of the input-file
     * @param graph graph, e. g. needed to obtain the graph-type (Moore, Mealy)
     * @throws IOException 
     * 
     * @author Andreas Schwenk
     */
    public void loadSigVarFromFile(DataInputStream in, Graph graph) throws IOException
    {
        name = in.readUTF();
        description = in.readUTF();
        type = SIGVAR_TYPE.valueOf(in.readUTF());
        bitLength = in.readInt();
        parent = graph;
    }
    public void loadSigVarFromFile(String[] signal, Graph graph) throws IOException
    {
        name = signal[0];
        switch (signal[2].toUpperCase()) {
            case "BIT":
                type = SIGVAR_TYPE.BIT;
                bitLength = 1;
                break;
            case "NIBBLE":
                type = SIGVAR_TYPE.BIT_N;
                bitLength = 4;
                break;
            case "BYTE":
                type = SIGVAR_TYPE.BIT_N;
                bitLength = 8;
                break;
            case "INTEGER":
                type = SIGVAR_TYPE.SIGNED;
                bitLength = Integer.parseInt(signal[3]);
                break;
            case "UNSIGNED":
                type = SIGVAR_TYPE.UNSIGNED;
                bitLength = Integer.parseInt(signal[3]);
                break;
            case "VECTOR":
                type = SIGVAR_TYPE.VECTOR;
                bitLength = Integer.parseInt(signal[3]);
                break;
            default:
                type = SIGVAR_TYPE.valueOf(signal[2]);
        }
        parent = graph;
    }
    public void loadVarFromFile(String[] signal, Graph graph) throws IOException //TODO: CHECK IF IT WORKS
    {
        name = signal[0];
        switch (signal[1].toUpperCase()) {
            case "BIT":
                type = SIGVAR_TYPE.BIT;
                bitLength = 1;
                break;
            case "NIBBLE":
                type = SIGVAR_TYPE.BIT_N;
                bitLength = 4;
                break;
            case "BYTE":
                type = SIGVAR_TYPE.BIT_N;
                bitLength = 8;
                break;
            case "INTEGER":
                type = SIGVAR_TYPE.SIGNED;
                bitLength = Integer.parseInt(signal[2]);
                break;
            case "UNSIGNED":
                type = SIGVAR_TYPE.UNSIGNED;
                bitLength = Integer.parseInt(signal[2]);
                break;
            case "VECTOR":
                type = SIGVAR_TYPE.BIT_N;
                bitLength = Integer.parseInt(signal[2]);
                break;
            default:
                type = SIGVAR_TYPE.valueOf(signal[1]);
        }
        parent = graph;
    }
    
    /**
     * Abstract method. Derived methods load signals/variables data from file.
     * Common data is loaded from method: "loadComponentFromFile" in this class.
     * 
     * @param in data-stream of the input-file
     * @param graph graph, e. g. needed to obtain the graph-type (Moore, Mealy)
     * @throws IOException 
     * 
     * @author Andreas Schwenk
     */
    public abstract void loadFromFile(DataInputStream in, Graph graph) throws IOException;
    
    /**
     * saves properties which are common to all signals/variables, e. g. name
     * 
     * @param out data-stream of the output-file
     * @throws IOException 
     * 
     * @author Andreas Schwenk
     */
    public void saveToFile(DataOutputStream out) throws IOException
    {
        out.writeUTF(name);
        out.writeUTF(description);
        out.writeUTF(type.name());
        out.writeInt(bitLength);
    }
}
