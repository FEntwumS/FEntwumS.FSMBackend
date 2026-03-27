package de.thkoeln.fentwums.fsm;

import de.thkoeln.fentwums.fsm.gui.GuiMain;
import de.thkoeln.fentwums.fsm.workflow.CLIWorkflow;
import picocli.CommandLine;

import javax.swing.*;
import java.io.IOException;

@CommandLine.Command(name = "STDE", mixinStandardHelpOptions = true, version = "STDE 1.0")
public class Main implements Runnable {

    @CommandLine.Option(names = {"-c", "--cli"}, description = "Run in CLI mode without GUI.")
    private boolean cliMode;

    /* Example CLI-specific options */
    @CommandLine.Option(names = {"-v", "--verify"}, description = "Verify the model immediately.")
    private boolean verify;
    @CommandLine.Option(names = {"-C", "--target=c"}, description = "Generate C code immediately.")
    private boolean generateC;
    @CommandLine.Option(names = {"-VHDL", "--target=vhdl"}, description = "Generate VHDL code immediately.")
    private boolean generateVHDL;
    @CommandLine.Option(names = {"-Verilog", "--target=verilog"}, description = "Generate Verilog code immediately.")
    private boolean generateVerilog;
    @CommandLine.Option(names = {"-SCXML", "--target=scxml"}, description = "Generate SCXML code immediately.")
    private boolean generateSCXML;
    @CommandLine.Option(names = {"-input", "--inputpath"}, description = "Path to the input file.")
    private java.io.File inputPath;
    @CommandLine.Option(names = {"-output", "--outputpath"}, description = "Path to the output file.")
    private String outputPath;

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    CLIWorkflow cliWorkflow = new CLIWorkflow();

    @Override
    public void run() {
        if (cliMode) {
            try {
                runCliLogic();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            runGuiLogic();
        }
    }

    private void runGuiLogic() {
        System.out.println("Starting GUI mode...");
        SwingUtilities.invokeLater(() -> {
            boolean macOSX = false;
            String osname = System.getProperty("os.name");
            if (osname.contains("Mac")) {
                macOSX = true;
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("apple.awt.brushMetalLook", "true");
                System.setProperty("apple.awt.fileDialogForDirectories", "true");
            }
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("error: setLookAndFeel(..) failed");
            }

            new GuiMain(1000, 650, macOSX);
        });
    }

    private void runCliLogic() throws IOException {
        System.out.println("Running in CLI mode...");
        if (inputPath == null) {
            System.out.println("Error: No input file provided.");
            return;
        }
        String path = inputPath.getPath();
        cliWorkflow.loadFile(new java.io.File(path));

        if (verify) {
            System.out.println("Performing verification...");
            System.out.println(cliWorkflow.verifyGraph());
        }
        if (generateC) {
            System.out.println("Performing C-Code generation...");
            if (outputPath == null || outputPath.isEmpty()) {
                System.out.println("Error: No export path provided for C code generation.");
                return;
            }
            System.out.println(cliWorkflow.generateCode_C(new java.io.File(outputPath + "/testgraph.h"), new java.io.File(outputPath + "/testgraph.c"), new java.io.File(outputPath + "/testgraph.e")));
        }
        if (generateVerilog) {
            System.out.println("Not yet implemented...");
        }
        if (generateVHDL) {
            System.out.println("Performing VHDL-Code generation...");
            if (outputPath == null || outputPath.isEmpty()) {
                System.out.println("Error: No export path provided for VHDL code generation.");
                return;
            }
            System.out.println(cliWorkflow.generateCode_VHDL(new java.io.File(outputPath + "/testgraph.vhd")));
        }
        if (generateSCXML) {
            System.out.println("Performing SCXML export...");
            if (outputPath == null || outputPath.isEmpty()) {
                System.out.println("Error: No export path provided for SCXML code generation.");
                return;
            }
            System.out.println(cliWorkflow.exportAsSCXML(new java.io.File(outputPath + "/testgraph.xml")));
        }
    }
}
