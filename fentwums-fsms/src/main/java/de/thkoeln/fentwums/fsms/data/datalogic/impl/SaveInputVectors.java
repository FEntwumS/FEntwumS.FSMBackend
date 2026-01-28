/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thkoeln.fentwums.fsms.data.datalogic.impl;

import static de.thkoeln.fentwums.fsms.data.datalogic.impl.TransitionHistory.stateList;
import de.thkoeln.fentwums.fsms.data.model.entities.Signal;
import de.thkoeln.fentwums.fsms.data.model.entities.SignalVectorList;
import de.thkoeln.fentwums.fsms.data.model.entities.State;
import de.thkoeln.fentwums.fsms.logHandling.LogHandler;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This class saves the input vectors in an XML File.
 *
 * @author Marvin Jolk
 */
public class SaveInputVectors {

    static public boolean saveInputVectors(SignalVectorList inputVectors, String savePath) {
        BufferedWriter bw;
        LogHandler errorHandler = LogHandler.getInstance();
        try {
            bw = new BufferedWriter(new PrintWriter(savePath));
        } catch (FileNotFoundException ex1) {
            errorHandler.addError("File to save input vectors not found");
            return false;
        }
        try {
            int stepCounter = 0;
            String output;
            output = "<?xml version=\"1.0\"?>" + System.lineSeparator();
            output = output + "<inputvectors>" + System.lineSeparator();
            output = output + "\t<vectors>" + System.lineSeparator();
            bw.write(output);

            ArrayList<Hashtable<String, Integer>> signalsVectorList = inputVectors.getSignalsVectorList();
            for (int i = 0; i < signalsVectorList.size(); i++) {
                output = "\t\t<vector>" + System.lineSeparator();

                ArrayList<Signal> signals = inputVectors.getSignals();
                for (int j = 0; j < signals.size(); j++) {
                    output = output +"\t\t\t<signal name=\""+signals.get(j).getName()+"\" value=\""+signalsVectorList.get(i).get(signals.get(j).getName())+"\"/>"+ System.lineSeparator();
                }

                output = output + "\t\t</vector>" + System.lineSeparator();
                bw.write(output);
            }

            output = output + "\t</vectors>" + System.lineSeparator();
            output = output + "</inputvectors>" + System.lineSeparator();
            bw.write(output);
            bw.close();
        } catch (IOException ex1) {
            errorHandler.addError("Could not write input vectors to xml");
            return false;
        }
        return true;
    }
}
