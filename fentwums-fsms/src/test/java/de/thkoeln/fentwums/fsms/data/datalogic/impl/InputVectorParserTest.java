/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thkoeln.fentwums.fsms.data.datalogic.impl;

import de.thkoeln.fentwums.fsms.data.datalogic.impl.InputVectorParser;
import java.util.ArrayList;
import java.util.Hashtable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import de.thkoeln.fentwums.fsms.data.model.entities.SignalVectorList;

/**
 * To test the Inputvector Parser.
 *
 * @author Marvin Jolk
 */
public class InputVectorParserTest {

    InputVectorParser instance;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = new InputVectorParser();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test if the Inputvectors are parsed correctly.
     */
    @Test
    public void testMultipleVectors0() {
        System.out.println("runMultipleVectors0");
        SignalVectorList sigVec = instance.getInputVector("test/fsms/data/datalogic/impl/xmlFilesForTest/eingabevektor2times2.xml");
        SignalVectorList sigVecPruf = new SignalVectorList();
        sigVecPruf.setSignalsVectorList(createSignalVectorList1());

        assertTrue(sigVecPruf.getSignalsVectorList().equals(sigVec.getSignalsVectorList()));
        assertEquals(sigVecPruf.getSignalsVectorList().size(), sigVec.getSignalsVectorList().size());

    }

    /**
     * Test if the Signals from the Inputvectors are parsed correctly.
     */
    @Test
    public void testMultipleSignals() {
        System.out.println("runMultipleVectors0");
        SignalVectorList sigVec = instance.getInputVector("test/fsms/data/datalogic/impl/xmlFilesForTest/eingabevektor2times2.xml");
        SignalVectorList sigVecPruf = new SignalVectorList();
        sigVecPruf.setSignalsVectorList(createSignalVectorList1());

        for (int i = 0; i < sigVecPruf.getSignalsVectorList().size(); i++) {
            assertTrue(sigVecPruf.getSignalsVectorList().get(i).equals(sigVec.getSignalsVectorList().get(i)));
        }

    }

    /**
     * Creates the Vectors with his Signals from eingabevektor2times2.xml.
     *
     * @return the SignalVectorList
     */
    private ArrayList<Hashtable<String, Integer>> createSignalVectorList1() {
        SignalVectorList sigVec = new SignalVectorList();
        ArrayList<Hashtable<String, Integer>> signalsAndVectors = new ArrayList<Hashtable<String, Integer>>();

        Hashtable vector1 = new Hashtable();
        vector1.put("signal11", 1);
        vector1.put("signal12", 2);
        signalsAndVectors.add(vector1);

        Hashtable vector2 = new Hashtable();
        vector2.put("signal21", 3);
        vector2.put("signal22", 4);
        signalsAndVectors.add(vector2);

        return signalsAndVectors;
    }

}
