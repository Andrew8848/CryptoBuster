package org.cryptobuster.gui.panels;

import javax.swing.*;
import java.awt.*;

public class InformationPanel extends JPanel {
    private static final String CRYPTOSYSTEM_STR = "CryptoSystem: ";
    private static final String CHAR_STR = "Chars: ";
    private static InformationPanel instance;

    public static InformationPanel getInstance(){
        return instance == null ? new InformationPanel() : instance;
    }

    private JLabel cryptosystem;
    private JLabel chars;

    private InformationPanel() {
        init();
    }

    private void init() {
        setBackground(Color.white);
        setLayout(new BorderLayout());
        this.cryptosystem = new JLabel(CRYPTOSYSTEM_STR);
        this.chars = new JLabel(CHAR_STR);

        add(this.cryptosystem, BorderLayout.LINE_START);
        add(this.chars, BorderLayout.LINE_END);
    }

    public String getCharsInfo() {
        return this.chars.getText();
    }

    public void setCharsInfo(long chars) {
            this.chars.setText(CHAR_STR + chars);
    }


    public String getCipherName() {
        return this.cryptosystem.getText();
    }

    public void setCipherName(String cryptosystem) {
        this.cryptosystem.setText(CRYPTOSYSTEM_STR + cryptosystem);
    }

}
