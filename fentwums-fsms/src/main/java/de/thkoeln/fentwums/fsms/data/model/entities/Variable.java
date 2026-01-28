package de.thkoeln.fentwums.fsms.data.model.entities;

import de.thkoeln.fentwums.fsms.data.model.enums.OperandType;

/**
 * A variable. That is used to count or do other special operations, beside the
 * normal Moore and Mealy operations.
 *
 * @author Linus Schoendorf and Marvin Jolk
 */
public class Variable extends Operand {

    public Variable() {
        this.opType = OperandType.VARIABLE;
    }

    @Override
    public String toString() {
        return "Variable: " + this.getName() + "\nType: " + this.getType() + "\nSize: " + this.getSize();
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Increment the variable.
     *
     * @author Linus Schoendorf
     */
    public void increment() {
        value = (value + 1) & (~(~0 << size));
    }

    /**
     * Decrement the variable.
     *
     * @author Linus Schoendorf
     */
    public void decrement() {
        value = (value - 1) & (~(~0 << size));
    }

    /**
     * Add the given value to the variable.
     *
     * @param n value to be added to the variable
     * @author Linus Schoendorf
     */
    public void add(int n) {
        value = (value + n) & (~(~0 << size));
    }

    /**
     * Subtract the given value from the variable.
     *
     * @param n value to be subracted from the variable
     * @author Linus Schoendorf
     */
    public void subtract(int n) {
        value = (value - n) & (~(~0 << size));
    }

    /**
     * Shifts the variable to the left by the given value.
     *
     * @param n amount of left shifts
     * @author Linus Schoendorf
     */
    public void lshift(int n) {
        value = (value << n) & (~(~0 << size));
    }

    /**
     * Shifts the variable to the right by the given value.
     *
     * @param n amount of right shifts
     * @author Linus Schoendorf
     */
    public void rshift(int n) {
        value = (value >> n) & (~(~0 << size));
    }

    /**
     * Assign the given value to the variable.
     *
     * @param n value the variable shall be assigned to
     * @author Linus Schoendorf
     */
    public void assign(int n) {
        value = n;
    }
}
