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
     *
     * @return
     * @author Marvin Jolk
     */
    private List<Image> createImageList() {
        String path = "fsms/res/logo/";
        ImageIcon img16_16 = new ImageIcon(getClass().getClassLoader().getResource(path+"logo16_16.png"));
        ImageIcon img32_32 = new ImageIcon(getClass().getClassLoader().getResource(path+"logo32_32.png"));
        ImageIcon img64_64 = new ImageIcon(getClass().getClassLoader().getResource(path+"logo64_64.png"));
        ImageIcon img128_128 = new ImageIcon(getClass().getClassLoader().getResource(path+"logo128_128.png"));
        ImageIcon img256_256 = new ImageIcon(getClass().getClassLoader().getResource(path+"logo256_256.png"));

        List<Image> icons = new ArrayList<Image>();
        icons.add(img16_16.getImage());
        icons.add(img32_32.getImage());
        icons.add(img64_64.getImage());
        icons.add(img128_128.getImage());
        icons.add(img256_256.getImage());
        return icons;
    }
}
