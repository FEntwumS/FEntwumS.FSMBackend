package de.thkoeln.fentwums.fsms.data.datalogic.services;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Interface for Expression.
 *
 * @author Marvin Jolk, Linus Schoendorf
 */
public interface IParseExpression {

    public boolean evaluate(ConcurrentHashMap<String, Integer> inputVector);
}
