package org.cryptobuster.gui.panels;

import javax.swing.*;
import java.awt.*;

public class InformationPanel extends JPanel {
    private static final String CRYPTOSYSTEM_STR = "CryptoSystem: ";
    private static final String CHAR_INPUT_STR = "Chars Input: ";
    private static final String CHAR_OUTPUT_STR = "Chars Output: ";
    private static InformationPanel instance;

    public static InformationPanel getInstance(){
        return instance == null ? new InformationPanel() : instance;
    }

    private JLabel cryptoSystem;
    private JLabel chars;

    private InformationPanel() {
        init();
    }

    private void init() {
        setBackground(Color.white);
        setLayout(new BorderLayout());
        this.cryptoSystem = new JLabel(CRYPTOSYSTEM_STR);
        this.chars = new JLabel(CHAR_INPUT_STR);

        add(this.cryptoSystem, BorderLayout.LINE_START);
        add(this.chars, BorderLayout.LINE_END);
    }

    public String getCharsInfo() {
        return this.chars.getText();
    }

    public void setCharsInfo(long inputChars, long outputChars) {
            this.chars.setText(CHAR_INPUT_STR + inputChars + "      " + CHAR_OUTPUT_STR + outputChars + "    ");
    }

    public String getCipherName() {
        return this.cryptoSystem.getText();
    }

    public void setCryptoSystemName(String cryptoSystem) {
        this.cryptoSystem.setText( "        " + CRYPTOSYSTEM_STR + cryptoSystem);
    }

}
