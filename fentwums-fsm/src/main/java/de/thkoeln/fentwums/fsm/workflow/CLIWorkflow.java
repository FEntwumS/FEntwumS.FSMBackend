package de.thkoeln.fentwums.fsm.workflow;

import de.thkoeln.fentwums.fsm.generation.Generation;
import de.thkoeln.fentwums.fsm.graph.Graph;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CLIWorkflow {
    private final Graph graph;
    private final Generation generation;
    private String logString;

    public CLIWorkflow() {
        graph = new Graph();
        generation = new Generation();
    }

    public String verifyGraph() {
        return logString = generation.verifyGraphAndPartialGenerate(graph);
    }

    public String generateCode_C(File file_h, File file_c, File file_e) {
        try {
            return logString = generation.generateCode_C(file_h, file_c, file_e, graph);
        } catch (IOException exception) {
            return logString = "Generieren von C-Code fehlgeschlagen!";
        }
    }

    public String generateCode_VHDL(File file) {
        try {
            return logString = generation.generateCode_VHDL(file, graph, true);
        } catch (IOException exception) {
            return logString = "Generieren von VHDL-Code fehlgeschlagen!";
        }
    }

    public String exportAsSCXML(File file) {
        try {
            return logString = generation.exportAsSCXML(file, graph);
        } catch (IOException exception) {
            return logString = "Export als SCXML fehlgeschlagen!";
        }
    }

    public void loadFile(File file) throws IOException {
        int fileVersion;
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        fileVersion = dis.readInt();
        graph.loadGraph(dis, fileVersion);
    }

}
