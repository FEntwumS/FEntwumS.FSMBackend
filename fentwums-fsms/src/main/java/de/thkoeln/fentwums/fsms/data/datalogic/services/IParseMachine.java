package de.thkoeln.fentwums.fsms.data.datalogic.services;

import de.thkoeln.fentwums.fsms.data.model.entities.Automaton;

/**
 * Interface for MachineParser.
 *
 * @author Marvin Jolk
 */
public interface IParseMachine {

    public Automaton parseXMLFile(String xmlPath);
}
