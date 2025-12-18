package de.thkoeln.fentwums.fsm.util;

import javax.swing.*;

import java.awt.*;
import java.net.URL;

/**
 * Utility class for centralized loading of graphical resources.
 * This class encapsulates the ClassLoader access and standardizes resource
 * pathing and file extensions across the application.
 */
public class ResourceLoader {

    /**
     * Loads an {@link ImageIcon} from the "images" resource directory.
     * The method automatically appends the "images/" prefix and the ".png" extension.
     *
     * @param name The base name of the image file (e.g., "saveIcon" for "images/saveIcon.png")
     * @return A new ImageIcon instance containing the loaded resource
     * @throws AssertionError if the resource cannot be found at the constructed URL
     */
    public static ImageIcon loadImageIcon(String name) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(String.format("images/%s.png", name));

        assert url != null : "Resource not found: images/" + name + ".png";

        return new ImageIcon(url);
    }

    /**
     * Loads a raw {@link Image} object from the resources.
     * This is a convenience wrapper for {@link #loadImageIcon(String)}.
     *
     * @param name The base name of the image file
     * @return The Image object extracted from the ImageIcon
     */
    public static Image loadImage(String name) {
        return loadImageIcon(name).getImage();
    }
}
