package de.thkoeln.fentwums.fsms.gui;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Administrates the logo images
 *
 * @author Marvin Jolk
 */
public class ImageAdministration {

    /**
     * Sets all icons for the given frame
     *
     * @param frame frame to set the icons for
     * @author Marvin Jolk
     */
    public void setLogoImageList(JFrame frame) {
        List<Image> icons = createImageList();
        frame.setIconImages(icons);
    }

    /**
     * Sets all icons for the given ClearLog
     *
     * @param clearLogFrame frame to be set
     * @author Marvin Jolk
     */
    public void setLogoImageList(ClearLog clearLogFrame) {

        List<Image> icons = createImageList();
        clearLogFrame.setIconImages(icons);
    }

    /**
     * Sets Icon for the given frame
     *
     * @param frame frame to be set
     * @author Marvin Jolk
     */
    public void setIconList(JFrame frame) {
        List<Image> icons = createImageList();
        frame.setIconImages(icons);
    }

    /**
     * Create List of Images
     */
    private List<Image> createImageList() {
        // 1. Correct the path: remove 'src' and ensure leading/trailing slashes are handled
        // In a JAR, your package is the path.
        String path = "fsms/logo/";

        int[] sizes = {16, 32, 64, 128, 256};
        List<Image> icons = new ArrayList<>();

        for (int size : sizes) {
            String fullPath = path + "logo" + size + "_" + size + ".png";

            // 2. Use the ClassLoader to find the resource
            var resource = getClass().getClassLoader().getResource(fullPath);

            if (resource != null) {
                icons.add(new ImageIcon(resource).getImage());
            } else {
                // Log a warning or handle the missing icon gracefully
                System.err.println("Could not find resource: " + fullPath);
            }
        }
        return icons;
    }
}
