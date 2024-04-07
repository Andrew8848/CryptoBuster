package org.cryptobuster.gui.dialog.alert;

import org.cryptobuster.gui.MainFrame;
import org.cryptobuster.gui.dialog.Dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AlertDialog {

    private static final Font FONT = new Font("Calibri", Font.PLAIN, 16);

    private JFrame owner;

    public AlertDialog(JFrame owner) {
        this.owner = owner;
    }

    public void open(String title, String label, AbstractAction action) {
        Dialog dialog = new Dialog(this.owner, title);
        dialog.setResizable(false);
        dialog.setContentPane(getPanel(dialog, label, action));
        dialog.pack();
        dialog.setCenter();
        dialog.setVisible(true);
    }

    public void open(String title, String label) {
        open(title, label, null);
    }

    private JPanel getPanel(JDialog own, String label, AbstractAction action) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridy = 0;
        constraints.insets = new Insets(30 ,30,20,30);
        panel.add(getLabel(label), constraints);
        constraints.gridy = 1;

        constraints.insets = new Insets(0,0,20,0);
        if(action != null) {
            panel.add(getPanelButtons(own, action), constraints);
        } else {
            panel.add(getButton(own, "OK"), constraints);
        }
        return panel;
    }

    private JPanel getPanelButtons(JDialog own, AbstractAction action) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(0, 10, 0, 10);
        constraints.gridx = 0;
        panel.add(getButton(own, "Cancel"), constraints);
        constraints.gridx = 1;
        panel.add(getButton(own,"Confirm", action), constraints);

        return panel;
    }

    private JLabel getLabel(String title) {
        JLabel label = new JLabel(title);
        label.setFont(FONT);
        return label;
    }

    private JButton getButton(JDialog own, String name) {
        JButton button = new JButton(name);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                own.setVisible(false);
            }
        });
        return button;
    }

    private JButton getButton(JDialog own, String name, AbstractAction action) {
        JButton button = getButton(own, name);
        if(action != null) button.addActionListener(action);
        return button;
    }
}
