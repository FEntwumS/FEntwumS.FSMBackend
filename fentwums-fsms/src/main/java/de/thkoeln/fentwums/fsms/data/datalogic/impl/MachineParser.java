package de.thkoeln.fentwums.fsms.data.datalogic.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Hashtable;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import de.thkoeln.fentwums.fsms.data.datalogic.services.IParseMachine;
import de.thkoeln.fentwums.fsms.data.model.entities.Automaton;
import de.thkoeln.fentwums.fsms.data.model.entities.Signal;
import de.thkoeln.fentwums.fsms.data.model.entities.StartNode;
import de.thkoeln.fentwums.fsms.data.model.entities.State;
import de.thkoeln.fentwums.fsms.data.model.entities.Transition;
import de.thkoeln.fentwums.fsms.data.model.entities.Variable;
import de.thkoeln.fentwums.fsms.data.model.enums.DataType;
import de.thkoeln.fentwums.fsms.data.model.enums.Direction;
import de.thkoeln.fentwums.fsms.logHandling.LogHandler;

/**
 * This class is for parsing the state machine from an XML file. It also builds
 * the automat.
 *
 * @author Marvin Jolk
 */
public class MachineParser implements IParseMachine {

    LogHandler errorHandler = LogHandler.getInstance();

    /**
     * Loads the state machine from an XML file. Then builds and return it.
     *
     * @param xmlPath the location of the state machine XML file
     * @return the state machine
     * @author Marvin Jolk
     */
    @Override
    public Automaton parseXMLFile(String xmlPath) {
        Automaton automat;
        try {
            //Vorbereitung zum einlesen der XML-Datei
            xmlPath = xmlPath.replace("\\", "/");

            if (new File(xmlPath) == null) {
                errorHandler.addError("File path is not correct");
                return null;
            }
            File inputFile = new File(xmlPath);

            DocumentBuilderFactory dbFactory;
            DocumentBuilder dBuilder;
            try {
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                errorHandler.addError("Parser couldn't be configurated");
                return null;
            }
            Document doc;
            try {
                doc = dBuilder.parse(inputFile);
                doc.getDocumentElement().normalize();
            } catch (IOException | SAXException e) {
                errorHandler.addError("File can't be parsed. Not a valid xml File");
                return null;
            }

            //Automatons Namen und Startzustand setzten
            automat = new Automaton();
            automat.setName(doc.getDocumentElement().getAttribute("name"));
            String startState = doc.getDocumentElement().getAttribute("initial");
            try {
                automat.setType(doc.getDocumentElement().getAttribute("graph_type"));
            } catch (Exception e) {
                errorHandler.addWarning("Graph Type not set");
            }

            //Signale einlesen und zu Automaton hinzufügen
            NodeList signalNodesList = doc.getElementsByTagName("signal");
            ArrayList<Signal> signals = new ArrayList<>();

            for (int i = 0; i < signalNodesList.getLength(); i++) {
                Node nodeSignal = signalNodesList.item(i);
                Element signalElement = (Element) nodeSignal;
                Signal signal = new Signal();

                signal.setName(signalElement.getAttribute("name"));

                if (signalElement.getAttribute("dir") != null && !signalElement.getAttribute("dir").equals("")) {
                    try {
                        signal.setDir(Direction.valueOf(signalElement.getAttribute("dir").toUpperCase()));
                    } catch (Exception e) {
                        errorHandler.addError("Direction of Signal " + i + " does not match the defined values");
                        return null;
                    }
                }
                if (signalElement.getAttribute("type") != null && !signalElement.getAttribute("type").equals("")) {
                    try {
                        signal.setType(DataType.valueOf(signalElement.getAttribute("type").toUpperCase()));
                    } catch (Exception e) {
                        errorHandler.addError("Type of Signal " + i + " does not match the defined values");
                        return null;
                    }
                }

                if (signalElement.getAttribute("size") != null && !signalElement.getAttribute("size").equals("")) {
                    if (!signalElement.getAttribute("size").equals("1")) {
                        try {
                            signal.setSize(Integer.parseInt(signalElement.getAttribute("size")));
                        } catch (Exception e) {
                            errorHandler.addError("Size of Signal " + i + " does not match the defined values");
                            return null;
                        }
                    }
                }

                signals.add(signal);
            }

            automat.setSignals(signals);

            //Variablen einlesen und zu Automaton hinzufügen
            NodeList variableNodesList = doc.getElementsByTagName("var");
            ArrayList<Variable> variables = new ArrayList<>();

            for (int i = 0; i < variableNodesList.getLength(); i++) {
                Node nodeVar = variableNodesList.item(i);
                Element varElement = (Element) nodeVar;
                Variable var = new Variable();

                var.setName(varElement.getAttribute("name"));

                if (varElement.getAttribute("type") != null && !varElement.getAttribute("type").equals("")) {
                    try {
                        var.setType(DataType.valueOf(varElement.getAttribute("type").toUpperCase()));
                    } catch (Exception e) {
                        errorHandler.addError("Type of Variable " + i + " does not match the defined values");
                        return null;
                    }
                }

                if (varElement.getAttribute("size") != null && !varElement.getAttribute("size").equals("")) {
                    if (!varElement.getAttribute("size").equals("1")) {
                        try {
                            var.setSize(Integer.parseInt(varElement.getAttribute("size")));
                        } catch (Exception e) {
                            errorHandler.addError("Size of Variable " + i + " does not match the defined values");
                            return null;
                        }
                    }
                }

                variables.add(var);
            }

            automat.setVariables(variables);

            //Zustaende einlesen und zu Automaton hinzufügen
            NodeList stateNodesList = doc.getElementsByTagName("state");
            ArrayList<State> states = new ArrayList<>();

            for (int i = 0; i < stateNodesList.getLength(); i++) {
                Node nodeState = stateNodesList.item(i);
                Element stateElement = (Element) nodeState;
                State state = new State();

                state.setId(stateElement.getAttribute("id"));

                String xStatePos = stateElement.getElementsByTagName("position").item(0).getAttributes().getNamedItem("x").getTextContent();
                String yStatePos = stateElement.getElementsByTagName("position").item(0).getAttributes().getNamedItem("y").getTextContent();
                try {
                    state.setPosition(new Point(Integer.parseInt(xStatePos), Integer.parseInt(yStatePos)));
                } catch (NumberFormatException e) {
                    errorHandler.addError("The x and/or y Position of State " + i + " is not an Integer");
                    return null;
                }

                try {
                    state.setWidth(Integer.parseInt(stateElement.getElementsByTagName("size").item(0).getAttributes().getNamedItem("width").getTextContent()));
                    state.setHeight(Integer.parseInt(stateElement.getElementsByTagName("size").item(0).getAttributes().getNamedItem("height").getTextContent()));
                } catch (NumberFormatException e) {
                    errorHandler.addError("The width and/or height of State " + i + " is not an Integer");
                    return null;
                }

                //Transitions einlesen und dem state zuweisen
                ArrayList<Transition> transitions = new ArrayList<>();

                for (int j = 0; j < stateElement.getElementsByTagName("transition").getLength(); j++) {
                    Node nodeTransitions = stateElement.getElementsByTagName("transition").item(j);
                    Element transitionElement = (Element) nodeTransitions;
                    Transition transition = new Transition();

                    String condString = transitionElement.getAttribute("cond");
                    Expression expression = new Expression(condString);

                    transition.setExpression(expression);

                    transition.setTarget(transitionElement.getAttribute("target"));
                    String xTran = stateElement.getElementsByTagName("conditionPosition").item(j).getAttributes().getNamedItem("x").getTextContent();
                    String yTran = stateElement.getElementsByTagName("conditionPosition").item(j).getAttributes().getNamedItem("y").getTextContent();
                    try {
                        transition.setConditionPosition(new Point(Integer.parseInt(xTran), Integer.parseInt(yTran)));
                    } catch (NumberFormatException e) {
                        errorHandler.addError("The x and/or y Position of transition " + j + " is not an Integer");
                        return null;
                    }

                    String xStart = stateElement.getElementsByTagName("startPoint").item(j).getAttributes().getNamedItem("x").getTextContent();
                    String yStart = stateElement.getElementsByTagName("startPoint").item(j).getAttributes().getNamedItem("y").getTextContent();
                    try {
                        transition.setStartPoint(new Point(Integer.parseInt(xStart), Integer.parseInt(yStart)));
                    } catch (NumberFormatException e) {
                        errorHandler.addError("The x and/or y Position of transition " + j + " is not an Integer");
                        return null;
                    }

                    String xEnd = stateElement.getElementsByTagName("endPoint").item(j).getAttributes().getNamedItem("x").getTextContent();
                    String yEnd = stateElement.getElementsByTagName("endPoint").item(j).getAttributes().getNamedItem("y").getTextContent();
                    try {
                        transition.setEndPoint(new Point(Integer.parseInt(xEnd), Integer.parseInt(yEnd)));
                    } catch (NumberFormatException e) {
                        errorHandler.addError("The x and/or y Position of transition " + j + " is not an Integer");
                        return null;
                    }

                    //Controlpoints einlesen und der Transition hinzufuegen
                    ArrayList<Point> ctrlPoints = new ArrayList<>();

                    for (int k = 0; k < transitionElement.getElementsByTagName("ctrlPoint").getLength(); k++) {
                        Node nodeCtrlPoints = transitionElement.getElementsByTagName("ctrlPoint").item(k);
                        Element ctrlPointElement = (Element) nodeCtrlPoints;
                        Point ctrlPoint = new Point();
                        int xPos;
                        int yPos;
                        try {
                            xPos = Integer.parseInt(ctrlPointElement.getAttribute("x"));
                            yPos = Integer.parseInt(ctrlPointElement.getAttribute("y"));
                        } catch (NumberFormatException e) {
                            errorHandler.addError("The x and/or y Position of controlpoint " + k + " in transition " + j + " in State " + i + " is not an Integer");
                            return null;
                        }

                        ctrlPoint.setLocation(xPos, yPos);

                        ctrlPoints.add(ctrlPoint);
                    }
                    transition.setCtrlPoints(ctrlPoints);

                    //Fuer Mealy
                    try {
                        ArrayList<Signal> mealySignalsInTransition = new ArrayList<>();
                        ArrayList<Integer> outputVectorsMealy = new ArrayList<>();
                        for (int k = 0; k < transitionElement.getElementsByTagName("assign").getLength(); k++) {
                            Node assignMealyCtrlPoints = transitionElement.getElementsByTagName("assign").item(k);
                            Element assignMealyElement = (Element) assignMealyCtrlPoints;

                            try {
                                mealySignalsInTransition.add(automat.findSignalByName(assignMealyElement.getAttribute("signal")));
                            } catch (Exception e) {
                                errorHandler.addError("The signal in Transition " + j + " in state " + i + "can not be found");
                                return null;
                            }

                            int expr;
                            try {
                                expr = Integer.parseInt(assignMealyElement.getAttribute("expr"));
                            } catch (Exception e) {
                                errorHandler.addError("The Expression of the assigned Signal in Transition " + j + " in State " + i + " is not an Integer");
                                return null;
                            }
                            outputVectorsMealy.add(expr);
                        }
                        transition.setMealyOutputSignals(buildSignalTable(outputVectorsMealy, mealySignalsInTransition));
                    } catch (Exception e) {

                    }

                    transitions.add(transition);

                }
                state.setTransitions(transitions);

                //On Entry einlesen und zuweisen
                ArrayList<String> onEntry = new ArrayList<>();
                for (int j = 0; j < stateElement.getElementsByTagName("onentry").getLength(); j++) {
                    Node nodeOnEntry = stateElement.getElementsByTagName("onentry").item(j);
                    Element onEntryElement = (Element) nodeOnEntry;

                    for (int k = 0; k < onEntryElement.getElementsByTagName("assign").getLength(); k++) {
                        Node nodeAssignOnEntry = onEntryElement.getElementsByTagName("assign").item(k);
                        Element onEntryAssignElement = (Element) nodeAssignOnEntry;

                        String variable = onEntryAssignElement.getAttribute("variable");
                        String expression = onEntryAssignElement.getAttribute("expr");

                        onEntry.add(variable + " = " + expression);
                    }
                }
                state.setOnEntry(onEntry);

                //Signale zuweisen
                ArrayList<Integer> outputVectors = new ArrayList<>();
                ArrayList<Signal> signalsInState = new ArrayList<>();
                for (int l = 0; l < stateElement.getElementsByTagName("during").getLength(); l++) {
                    Node nodeDuring = stateElement.getElementsByTagName("during").item(l);
                    Element duringElement = (Element) nodeDuring;

                    for (int m = 0; m < duringElement.getElementsByTagName("assign").getLength(); m++) {
                        Node nodeAssignDuring = duringElement.getElementsByTagName("assign").item(m);
                        Element duringAssignDuring = (Element) nodeAssignDuring;
                        try {
                            signalsInState.add(automat.findSignalByName(duringAssignDuring.getAttribute("signal")));
                        } catch (Exception e) {
                            errorHandler.addError("The signal in state " + i + "can not be found");
                            return null;
                        }

                        int expr;
                        try {
                            expr = Integer.parseInt(duringAssignDuring.getAttribute("expr"));
                        } catch (Exception e) {
                            errorHandler.addError("The Expression of the assigned Signal in State " + i + " is not an Integer");
                            return null;
                        }
                        outputVectors.add(expr);
                    }

                }

                state.setOutputVector(buildSignalTable(outputVectors, signalsInState));
                states.add(state);
            }
            automat.setStates(states);
            if (automat.findStateByName(startState) == null) {
                errorHandler.addError("Startstate set in xml Header (initial), does not exist");
                return null;
            }
            automat.setStartState(automat.findStateByName(startState));

            //StartNode einlesen und zuweisen
            NodeList startNodeNodeList = doc.getElementsByTagName("startNode");
            if (startNodeNodeList.getLength() > 1) {
                errorHandler.addError("More then one StartNode. This is not valid");
                return null;
            }
            Node nodeStartNode = startNodeNodeList.item(0);
            Element startNodeElement = (Element) nodeStartNode;
            StartNode startNode = new StartNode();

            if (automat.findStateByName(startNodeElement.getAttribute("target")) != null) {
                startNode.setTarget(automat.findStateByName(startNodeElement.getAttribute("target")));
            } else {
                errorHandler.addError("Target (State) in StartNode does not exist" + startNodeElement.getAttribute("target"));
                return null;
            }
            try {
                if (Integer.parseInt(startNodeElement.getAttribute("condition")) != 1) {
                    errorHandler.addError("StartNode condition is not 1");
                    return null;
                }
                startNode.setCondition(Integer.parseInt(startNodeElement.getAttribute("condition")));
            } catch (Exception e) {
                errorHandler.addError("StartNode condition can not be parsed. It need to be 1");
                return null;
            }
            Node nodeConditionPosition = startNodeElement.getElementsByTagName("conditionPosition").item(0);
            Element conditionPositionElement = (Element) nodeConditionPosition;
            try {
                int xPos = Integer.parseInt(conditionPositionElement.getAttribute("x"));
                int yPos = Integer.parseInt(conditionPositionElement.getAttribute("y"));
                startNode.setConditionPosition(new Point(xPos, yPos));
            } catch (NumberFormatException e) {
                errorHandler.addError("The x and/or y ConditionPosition of StartNode is not an Integer");
                return null;
            }
            Node nodePosition = startNodeElement.getElementsByTagName("position").item(0);
            Element positionElement = (Element) nodePosition;
            try {
                int xPos = Integer.parseInt(positionElement.getAttribute("x"));
                int yPos = Integer.parseInt(positionElement.getAttribute("y"));
                startNode.setPosition(new Point(xPos, yPos));
            } catch (NumberFormatException e) {
                errorHandler.addError("The x and/or y Position of StartNode is not an Integer");
                return null;
            }
            Node nodeTargetPosition = startNodeElement.getElementsByTagName("targetPosition").item(0);
            Element targetPositionElement = (Element) nodeTargetPosition;
            try {
                int xPos = Integer.parseInt(targetPositionElement.getAttribute("x"));
                int yPos = Integer.parseInt(targetPositionElement.getAttribute("y"));
                startNode.setTargetPosition(new Point(xPos, yPos));
            } catch (NumberFormatException e) {
                errorHandler.addError("The x and/or y targetPosition of StartNode is not an Integer");
                return null;
            }
            automat.setStartNode(startNode);

        } catch (DOMException e) {
            errorHandler.addError("A DOMException. Something went wrong while parsing your XML-File: " + e);
            return null;
        } catch (NumberFormatException e) {
            errorHandler.addError("NumberFormatException. Something went wrong while parsing your XML-File: " + e);
            return null;
        } catch (Exception e) {
            errorHandler.addError("Something went wrong while parsing your XML-File: " + e);
            return null;
        }
        return automat;
    }

    /**
     * Builds a Hashtable which contains the output vectors and signals, and
     * their reference.
     *
     * @param outputVector List of Output vectors
     * @param signals List of Signals
     * @return Hashtable, which contains output vectors and signals.
     * @author Marvin Jolk
     */
    private Hashtable<Signal, Integer> buildSignalTable(List<Integer> outputVector, List<Signal> signals) {
        Hashtable h = new Hashtable();
        for (int i = 0; i < outputVector.size(); i++) {
            h.put(signals.get(i), outputVector.get(i));
        }
        return h;
    }
}
