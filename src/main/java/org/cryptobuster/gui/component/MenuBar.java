package org.cryptobuster.gui.component;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar {
    private static MenuBar instance;

    public static MenuBar getInstance(){
        return instance == null ? new MenuBar() : instance;
    }

    private MenuBar() {
        init();
    }

    private void init() {
        setBackground(Color.white);
    }


}
