package de.thkoeln.fentwums.fsms.data.model.entities;

import de.thkoeln.fentwums.fsms.data.model.enums.Direction;
import de.thkoeln.fentwums.fsms.data.model.enums.OperandType;

/**
 * Signal that goes in, out or in and out of an state machine.
 *
 * @author Linus Schoendorf and Marvin Jolk
 */
public class Signal extends Operand {

    private Direction dir;

    public Signal() {
        this.opType = OperandType.SIGNAL;
    }

    public Signal(String name, Direction dir) {
        this();
        this.name = name;
        this.dir = dir;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "\t Name: " + this.getName() + "\n \t \t Direction: " + dir + "\n \t \t Type: " + this.getType() + "\n \t \t Size: " + this.getSize();
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }
}
