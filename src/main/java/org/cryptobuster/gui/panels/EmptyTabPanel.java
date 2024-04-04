package org.cryptobuster.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class EmptyTabPanel extends JPanel {
    private static EmptyTabPanel instance;

    public static EmptyTabPanel getInstance(){
        return instance == null ? new EmptyTabPanel() : instance;
    }

    private static final String DESCRIPTION = "Possible action: ";

    private static final Font ARTICLE = new Font("Calibri", Font.BOLD, 14);
    private static final Font FUNCTIONAL_PARAGRAPH = new Font("Calibri", Font.BOLD, 14);

    GridBagConstraints constraints;

    private JLabel title;


    private EmptyTabPanel() {
        init();
    }

    public void addAction(String title, MouseAdapter mouseAdapter){
        JLabel label = new JLabel(title);
        label.setFont(FUNCTIONAL_PARAGRAPH);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setForeground(Color.BLUE);
        label.addMouseListener(mouseAdapter);
        this.constraints.insets = new Insets(10,0,0,0);
        this.constraints.gridy++;
        this.add(label, constraints);
    }

    private void init() {
        constraints = new GridBagConstraints();
        setBackground(Color.LIGHT_GRAY);

        this.title = new JLabel(DESCRIPTION);
        this.title.setFont(ARTICLE);


        setLayout(new GridBagLayout());

        this.constraints.gridx = 0;
        this.constraints.gridy = 0;
        this.constraints.insets = new Insets(0,0,20,0);
        add(this.title, constraints);

    }



}
