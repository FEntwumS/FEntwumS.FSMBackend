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

    /* CLI-specific options */
    @CommandLine.Option(names = {"-v", "--verify"}, description = "Verify the model immediately.")
    private boolean verify;
    @CommandLine.Option(names = {"-C", "--target=c"}, description = "Generate C code immediately.")
    private boolean generateC;
    @CommandLine.Option(names = {"-V", "--target=vhdl"}, description = "Generate VHDL code immediately.")
    private boolean generateVHDL;
    @CommandLine.Option(names = {"-i", "--inputpath"}, description = "Path to the input file.")
    private java.io.File filePath;
    @CommandLine.Option(names = {"-o", "--outputpath"}, description = "Path to the output file.")
    private String outputDirectory;

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
        if (filePath == null) {
            System.out.println("Error: No input file provided.");
            return;
        }
        String path = filePath.getPath();
        cliWorkflow.loadFile(new java.io.File(path));
        int pos = filePath.getName().lastIndexOf(".");
        String justName = pos > 0 ? filePath.getName().substring(0, pos) : filePath.getName();
        if (verify) {
            System.out.println("Performing verification for "+ filePath.getName() + "...");
            System.out.println(cliWorkflow.verifyGraph());
        }
        if (generateC) {
            System.out.println("Performing C-Code generation...");
            if (outputDirectory == null || outputDirectory.isEmpty()) {
                System.out.println("Error: No export path provided for C code generation.");
                return;
            }
            System.out.println(cliWorkflow.generateCode_C(new java.io.File(outputDirectory + "/"+ justName+".h"), new java.io.File(outputDirectory + "/"+ justName+".c"), new java.io.File(outputDirectory + "/"+ justName+".e")));
        }
//        if (generateVerilog) {
//            System.out.println("Not yet implemented...");
//        }
        if (generateVHDL) {
            System.out.println("Performing VHDL-Code generation...");
            if (outputDirectory == null || outputDirectory.isEmpty()) {
                System.out.println("Error: No export path provided for VHDL code generation.");
                return;
            }
            System.out.println(cliWorkflow.generateCode_VHDL(new java.io.File(outputDirectory + "/"+ justName+".vhd")));
        }
    }
}
