package de.thkoeln.fentwums.fsms.logic.simulation.services;

import de.thkoeln.fentwums.fsms.data.model.entities.SignalVectorList;

/**
 *
 * @author Linus
 */
public interface ISimulation {

    public boolean runSingleStep();

    public boolean run(SignalVectorList signalVectorList);
}
