package de.thkoeln.fentwums.fsms.data.datalogic.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import de.thkoeln.fentwums.fsms.data.datalogic.services.IInputVector;
import de.thkoeln.fentwums.fsms.data.model.entities.SignalVectorList;
import de.thkoeln.fentwums.fsms.logHandling.LogHandler;

/**
 * This class is for parsing the input vectors from an XML file.
 *
 * @author Marvin Jolk
 */
public class InputVectorParser implements IInputVector {

    LogHandler errorHandler = LogHandler.getInstance();

    /**
     * Parse the input vector from an xml file and build the java object.
     *
     * @param xmlPath Path of the xml file
     * @return The input Vectors
     * @author Marvin Jolk
     */
    @Override
    public SignalVectorList getInputVector(String xmlPath) {
        SignalVectorList signalVector;
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

            ArrayList<Hashtable<String, Integer>> signalsAndVectors = new ArrayList<Hashtable<String, Integer>>();

            NodeList vectorNodesList = doc.getElementsByTagName("vector");

            for (int i = 0; i < vectorNodesList.getLength(); i++) {
                Node nodeVector = vectorNodesList.item(i);
                Element vectorElement = (Element) nodeVector;
                Hashtable vector = new Hashtable();

                for (int j = 0; j < vectorElement.getElementsByTagName("signal").getLength(); j++) {
                    Node nodeSignal = vectorElement.getElementsByTagName("signal").item(j);
                    Element signalElement = (Element) nodeSignal;
                    try {
                        vector.put(signalElement.getAttribute("name"), Integer.parseInt(signalElement.getAttribute("value")));
                    } catch (Exception e) {
                        errorHandler.addError("Value of Signal " + j + " is not an Integer");
                        return null;
                    }

                }
                signalsAndVectors.add(vector);

            }

            signalVector = new SignalVectorList();
            signalVector.setSignalsVectorList(signalsAndVectors);
        } catch (Exception e) {
            errorHandler.addError("Something went wrong while parsing your InputVector XML-File: " + e);
            return null;

        }
        return signalVector;
    }

}
