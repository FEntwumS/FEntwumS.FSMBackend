package de.thkoeln.fentwums.fsm;

import de.thkoeln.fentwums.fsm.gui.GuiMain;
import picocli.CommandLine;

import javax.swing.*;

@CommandLine.Command(name = "STDE", mixinStandardHelpOptions = true, version = "STDE 1.0")
public class Main implements Runnable{

    @CommandLine.Option(names = {"-c", "--cli"}, description = "Run in CLI mode without GUI.")
    private boolean cliMode;

    /* Example CLI-specific options */
    @CommandLine.Option(names = {"-v", "--verify"}, description = "Verify the model immediately.")
    private boolean verify;

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    @Override
    public void run() {
        if (cliMode) {
            runCliLogic();
        } else {
            runGuiLogic();
        }
    }

    private void runGuiLogic() {
        System.out.println("Starting GUI mode...");
        SwingUtilities.invokeLater(() -> {
            boolean macOSX = false;
            String osname = System.getProperty("os.name");
            if(osname.contains("Mac")) {
                macOSX = true;
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("apple.awt.brushMetalLook", "true");
                System.setProperty("apple.awt.fileDialogForDirectories", "true");
            }
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) { System.out.println("error: setLookAndFeel(..) failed"); }

            new GuiMain(1000, 650, macOSX);
        });
    }

    private void runCliLogic() {
        System.out.println("Running in CLI mode...");
        if (verify) {
            System.out.println("Performing model verification...");
        }
    }
}
