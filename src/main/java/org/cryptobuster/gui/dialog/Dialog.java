package org.cryptobuster.gui.dialog;

import javax.swing.*;
import java.awt.*;

public class Dialog extends JDialog {
    public Dialog(Frame owner, String title) {
        super(owner, title);
    }

    public void setCenter(){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
    }
}
