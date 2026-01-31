/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thkoeln.fentwums.fsms.data.datalogic.impl;

import de.thkoeln.fentwums.fsms.data.datalogic.impl.MachineParser;
import de.thkoeln.fentwums.fsms.data.datalogic.impl.Expression;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import de.thkoeln.fentwums.fsms.data.model.entities.Automaton;
import de.thkoeln.fentwums.fsms.data.model.entities.Operand;
import de.thkoeln.fentwums.fsms.data.model.entities.Signal;
import de.thkoeln.fentwums.fsms.data.model.entities.StartNode;
import de.thkoeln.fentwums.fsms.data.model.entities.State;
import de.thkoeln.fentwums.fsms.data.model.entities.Transition;
import de.thkoeln.fentwums.fsms.data.model.entities.Variable;
import de.thkoeln.fentwums.fsms.data.model.enums.CondOperator;
import de.thkoeln.fentwums.fsms.data.model.enums.DataType;
import static de.thkoeln.fentwums.fsms.data.model.enums.DataType.BIT;
import static de.thkoeln.fentwums.fsms.data.model.enums.DataType.INTEGER;
import de.thkoeln.fentwums.fsms.data.model.enums.Direction;

/**
 * Testclass for the MachineParser
 *
 * @author Marvin Jolk
 */
public class MachineParserTest {

    MachineParser instance;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = new MachineParser();
    }

    @After
    public void tearDown() {
    }

    /**
     * Tests if the signals are parsed correctly.
     */
    @Test
    public void testMultipleSignals0() {
        System.out.println("runMultipleSignals0");
        Automaton testAutomaton = createAutomaton("src/test/java/de/thkoeln/fentwums/fsms/data/datalogic/impl/xmlFilesForTest/mooreCompleteMachine.xml");

        ArrayList<Signal> signals = createSignalsForCompleteMoore();

        for (int i = 0; i < signals.size(); i++) {
            assertEquals(signals.get(i).getName(), testAutomaton.getSignals().get(i).getName());
            assertEquals(signals.get(i).getDir(), testAutomaton.getSignals().get(i).getDir());
            assertEquals(signals.get(i).getType(), testAutomaton.getSignals().get(i).getType());
        }
    }

    /**
     * Tests if the variables are parsed correctly.
     */
    @Test
    public void testMultipleVariables0() {
        System.out.println("runVariablesSignals0");
        Automaton testAutomaton = createAutomaton("src/test/java/de/thkoeln/fentwums/fsms/data/datalogic/impl/xmlFilesForTest/mooreCompleteMachine.xml");
        ArrayList<Variable> variables = new ArrayList<Variable>();
        Variable variable1 = new Variable();
        Variable variable2 = new Variable();

        variable1.setName("varname1");
        variable1.setDataType(INTEGER);
        variable1.setSize(16);
        variable2.setName("varnam2");
        variable2.setType(BIT);

        variables.add(variable1);
        variables.add(variable2);
        for (int i = 0; i < variables.size(); i++) {
            assertEquals(testAutomaton.getVariables().get(i).getName(), variables.get(i).getName());
            assertEquals(testAutomaton.getVariables().get(i).getDataType(), variables.get(i).getDataType());
            assertEquals(testAutomaton.getVariables().get(i).getSize(), variables.get(i).getSize());
        }
    }

    /**
     * Test if the states are parsed correctly.
     */
    @Test
    public void testMultipleStates0() {
        System.out.println("runMultipleStates0");
        Automaton testAutomaton = createAutomaton("src/test/java/de/thkoeln/fentwums/fsms/data/datalogic/impl/xmlFilesForTest/mooreCompleteMachine.xml");
        ArrayList<State> states = createStatesForCompleteMoore();

        assertEquals(states.size(), testAutomaton.getStates().size());
        for (int i = 0; i < states.size(); i++) {
            assertEquals(testAutomaton.getStates().get(i).getName(), states.get(i).getName());
            assertEquals(testAutomaton.getStates().get(i).getPosition(), states.get(i).getPosition());
            assertEquals(testAutomaton.getStates().get(i).getWidth(), states.get(i).getWidth());
            assertEquals(testAutomaton.getStates().get(i).getHeight(), states.get(i).getHeight());
        }
    }

    /**
     * Tests if the transitions from state 1 are parsed correctly.
     */
    @Test
    public void testMultipleTransitionsOneState0() {
        System.out.println("runMultipleTransitionsState0");
        Automaton testAutomaton = createAutomaton("src/test/java/de/thkoeln/fentwums/fsms/data/datalogic/impl/xmlFilesForTest/mooreCompleteMachine.xml");
        ArrayList<State> states = createStatesForCompleteMoore();

        for (int i = 0; i < states.get(0).getTransitions().size(); i++) {
            assertEquals(testAutomaton.getStates().get(0).getTransitions().get(i).getExpression().toString(), states.get(0).getTransitions().get(i).getExpression().toString());
            assertEquals(testAutomaton.getStates().get(0).getTransitions().get(i).getTarget(), states.get(0).getTransitions().get(i).getTarget());
            assertEquals(testAutomaton.getStates().get(0).getTransitions().get(i).getConditionPosition(), states.get(0).getTransitions().get(i).getConditionPosition());
            assertEquals(testAutomaton.getStates().get(0).getTransitions().get(i).getStartPoint(), states.get(0).getTransitions().get(i).getStartPoint());
            assertEquals(testAutomaton.getStates().get(0).getTransitions().get(i).getEndPoint(), states.get(0).getTransitions().get(i).getEndPoint());

            if (states.get(0).getTransitions().get(i).getCtrlPoints() != null) {
                for (int j = 0; j < states.get(0).getTransitions().get(i).getCtrlPoints().size(); j++) {
                    assertEquals(testAutomaton.getStates().get(0).getTransitions().get(i).getCtrlPoints().get(j), states.get(0).getTransitions().get(i).getCtrlPoints().get(j));
                }
            }

        }
    }

    /**
     * Tests if StartNode is parsed correctly
     */
    @Test
    public void testStartNode0() {
        System.out.println("runStartNode0");
        StartNode startNode = createStartNode();

        Automaton testAutomaton = createAutomaton("src/test/java/de/thkoeln/fentwums/fsms/data/datalogic/impl/xmlFilesForTest/mooreCompleteMachine.xml");
        StartNode underTestStartNode = testAutomaton.getStartNode();

        assertEquals(startNode.getTarget().getName(), underTestStartNode.getTarget().getName());
        assertEquals(startNode.getCondition(), underTestStartNode.getCondition());
        assertEquals(startNode.getConditionPosition(), underTestStartNode.getConditionPosition());
        assertEquals(startNode.getPosition(), underTestStartNode.getPosition());
        assertEquals(startNode.getTargetPosition(), underTestStartNode.getTargetPosition());
    }

    /**
     * Tests if OnEntry is parsed correctly
     */
    @Test
    public void testOnEntry0() {
        System.out.println("runOnEntry0");
        Automaton underTest = createAutomaton("src/test/java/de/thkoeln/fentwums/fsms/data/datalogic/impl/xmlFilesForTest/testOnEntry.xml");

        ArrayList<String> referenceTable1 = new ArrayList<>();
        referenceTable1.add("var1 = var4 + var1");
        ArrayList<String> referenceTable2 = new ArrayList<>();
        referenceTable2.add("var2 = var2 + 1");
        referenceTable2.add("var2 = var2 + 1");
        assertTrue(referenceTable1.equals(underTest.getStates().get(0).getOnEntry()));
        assertTrue(referenceTable2.equals(underTest.getStates().get(1).getOnEntry()));

    }

    /**
     * Tests if the signals are parsed correctly
     */
    @Test
    public void testSignals0() {
        System.out.println("runSignals0");
        Automaton underTest = createAutomaton("src/test/java/de/thkoeln/fentwums/fsms/data/datalogic/impl/xmlFilesForTest/mooreCompleteMachine.xml");

        List<Signal> signals = underTest.getSignals();
        String name = "signal2";
        Signal referenzSignal = signals.stream().filter(s -> name.equals(s.getName())).findFirst().orElse(null);

        for (int i = 0; i < 3; i++) {
            Hashtable<Signal, Integer> underTestTable = underTest.getStates().get(i).getOutputVector();
            assertFalse(underTestTable.isEmpty());
            assertTrue(underTestTable.containsKey(referenzSignal));
        }

    }

    /**
     * Help to making the tests easier.
     *
     * @param xmlPath
     * @return Automaton which is tested.
     */
    private Automaton createAutomaton(String xmlPath) {
        return instance.parseXMLFile(xmlPath);
    }

    /**
     *
     * Create all the Signals with the values of the xml File
     * mooreCompleteMachine
     *
     * @return Signals of Signals
     */
    private ArrayList<Signal> createSignalsForCompleteMoore() {
        ArrayList<Signal> signals = new ArrayList<Signal>();
        Signal sig1 = new Signal("signalIn1", Direction.IN);
        sig1.setType(DataType.INTEGER);
        sig1.setSize(16);
        Signal sig2 = new Signal("signalIn2", Direction.IN);
        sig2.setType(DataType.INTEGER);
        sig2.setSize(16);
        Signal sig3 = new Signal("signalIn3", Direction.IN);
        sig3.setType(DataType.INTEGER);
        sig3.setSize(16);
        Signal sig4 = new Signal("signal2", Direction.OUT);
        sig4.setType(DataType.BIT);

        signals.add(sig1);
        signals.add(sig2);
        signals.add(sig3);
        signals.add(sig4);

        return signals;
    }

    /**
     * Create all the States with the values of the xml File
     * mooreCompleteMachine
     *
     * @return List of States
     */
    private ArrayList<State> createStatesForCompleteMoore() {
        ArrayList<State> states = new ArrayList<State>();
        State state1 = new State();
        State state2 = new State();
        State state3 = new State();

        state1.setId("namestate1");
        state1.setPosition(new Point(224, 224));
        state1.setWidth(144);
        state1.setHeight(64);
        state2.setId("namestate2");
        state2.setPosition(new Point(192, 384));
        state2.setWidth(144);
        state2.setHeight(64);
        state3.setId("namestate3");
        state3.setPosition(new Point(496, 432));
        state3.setWidth(144);
        state3.setHeight(64);

        //Transitions
        //Transitions for state1 "namestate1"
        ArrayList<Transition> transitions1 = new ArrayList<Transition>();

        Transition trans11 = new Transition();
        ArrayList<Point> ctrlPoints11 = new ArrayList<Point>();
        //trans11.setCond(new Condition("1"));
        ArrayList<Operand> operands = new ArrayList<>();
        operands.add(new Signal("signalIn1", Direction.IN));
        operands.add(new Signal("signalIn2", Direction.IN));

        ArrayList<CondOperator> condOperators = new ArrayList<CondOperator>();
        condOperators.add(CondOperator.AND);

        trans11.setExpression(new Expression("signalIn1 and signalIn2"));
        trans11.setTarget("namestate2");
        trans11.setConditionPosition(new Point(56, 279));
        trans11.setStartPoint(new Point(157, 349));
        trans11.setEndPoint(new Point(156, 348));
        ctrlPoints11.add(new Point(116, 246));
        ctrlPoints11.add(new Point(68, 321));
        trans11.setCtrlPoints(ctrlPoints11);

        Transition trans12 = new Transition();

        operands = new ArrayList<>();
        operands.add(new Signal("signalIn1", Direction.IN));
        operands.add(new Signal("signalIn2", Direction.IN));

        condOperators = new ArrayList<CondOperator>();
        condOperators.add(CondOperator.OR);

        trans12.setExpression(new Expression("signalIn1 or signalIn2"));

        trans12.setTarget("namestate1");
        trans12.setConditionPosition(new Point(346, 222));
        trans12.setStartPoint(new Point(286, 286));
        trans12.setEndPoint(new Point(286, 286));

        Transition trans13 = new Transition();
        ArrayList<Point> ctrlPoints13 = new ArrayList<Point>();

        operands = new ArrayList<>();
        operands.add(new Signal("signalIn1", Direction.IN));
        operands.add(new Signal("signalIn2", Direction.IN));

        condOperators = new ArrayList<CondOperator>();
        condOperators.add(CondOperator.EQUAL);


        trans13.setExpression(new Expression("signalIn1 == signalIn2"));

        trans13.setTarget("namestate3");
        trans13.setConditionPosition(new Point(391, 326));
        trans13.setStartPoint(new Point(532, 468));
        trans13.setEndPoint(new Point(434, 370));
        ctrlPoints13.add(new Point(411, 317));
        trans13.setCtrlPoints(ctrlPoints13);

        transitions1.add(trans11);
        transitions1.add(trans12);
        transitions1.add(trans13);

        state1.setTransitions(transitions1);

        Transition trans21 = new Transition();
        ArrayList<Point> ctrlPoints21 = new ArrayList<Point>();

        operands = new ArrayList<>();
        operands.add(new Signal("signalIn1", Direction.IN));
        operands.add(new Signal("signalIn2", Direction.IN));
        operands.add(new Signal("signalIn3", Direction.IN));

        condOperators = new ArrayList<CondOperator>();
        condOperators.add(CondOperator.OR);
        condOperators.add(CondOperator.AND);

        trans21.setExpression(new Expression("signalIn1 or signalIn2 and signalIn3"));

        trans21.setTarget("namestate3");
        trans21.setConditionPosition(new Point(325, 462));
        trans21.setStartPoint(new Point(532, 468));
        trans21.setEndPoint(new Point(434, 370));
        trans21.setCtrlPoints(new ArrayList<>());

        ArrayList<Transition> transitions2 = new ArrayList<Transition>();
        transitions2.add(trans21);
        state2.setTransitions(transitions2);

        states.add(state1);
        states.add(state2);
        states.add(state3);

        return states;
    }

    /**
     * Create the startNode with the values of the xml File mooreCompleteMachine
     *
     * @return StartNode
     */
    private StartNode createStartNode() {
        StartNode startNode = new StartNode();
        State state1 = new State();
        state1.setId("namestate1");
        startNode.setTarget(state1);
        startNode.setCondition(1);
        startNode.setConditionPosition(new Point(149, 131));
        startNode.setPosition(new Point(132, 104));
        startNode.setTargetPosition(new Point(188, 188));
        return startNode;
    }

}
