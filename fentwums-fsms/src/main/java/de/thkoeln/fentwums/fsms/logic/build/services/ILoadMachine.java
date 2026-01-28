package de.thkoeln.fentwums.fsms.logic.build.services;


/**
 *
 * @author Linus Schoendorf
 */
public interface ILoadMachine {

    public String getTransitionHistoryPath();

    public void setTransitionHistoryPath(String transitionHistoryPath);

    public boolean setAutomatonByPath(String path);

    public boolean setInputVectorByPath(String path);

    public boolean runFullSimulation();
    
    public boolean runSingleStep();
    
    public boolean setupSimulation();
    
    public void resetSimulator();
}
