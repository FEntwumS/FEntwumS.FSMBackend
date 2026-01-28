package de.thkoeln.fentwums.fsms.data.datalogic.services;

import de.thkoeln.fentwums.fsms.data.model.entities.SignalVectorList;

/**
 * Interface for InputVector.
 *
 * @author Marvin Jolk
 */
public interface IInputVector {

    public SignalVectorList getInputVector(String xmlPath);
}
