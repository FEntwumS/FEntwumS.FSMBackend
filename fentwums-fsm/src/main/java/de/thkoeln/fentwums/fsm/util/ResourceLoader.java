package de.thkoeln.fentwums.fsm.util;

import javax.swing.*;

import java.awt.*;
import java.net.URL;

public class ResourceLoader {


    public static ImageIcon loadImageIcon(String name){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(String.format("images/%s.png", name));
        assert url != null;
        return new ImageIcon(url);
    }

    public static Image loadImage(String name) {
        return loadImageIcon(name).getImage();
    }
}
