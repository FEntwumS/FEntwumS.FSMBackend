package de.thkoeln.fentwums.fsms.data.model.entities;

import de.thkoeln.fentwums.fsms.data.model.enums.DataType;
import de.thkoeln.fentwums.fsms.data.model.enums.OperandType;

/**
 * Abstract class for Signal and Variable.
 *
 * @author Linus Schoendorf and Marvin Jolk
 */
public abstract class Operand {

    protected String name;
    private DataType dataType;
    protected OperandType opType;
    protected int value;
    protected int size;

    abstract public int getValue();

    abstract public void setValue(int value);

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public OperandType getOpType() {
        return opType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return dataType;
    }

    public void setType(DataType dataType) {
        this.dataType = dataType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
