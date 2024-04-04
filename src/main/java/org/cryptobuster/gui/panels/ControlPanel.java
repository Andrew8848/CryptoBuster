package org.cryptobuster.gui.panels;

import lombok.Getter;
import org.cryptobuster.gui.component.ActionLog;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


@Getter
public class ControlPanel extends JPanel {

    private static ControlPanel instance;

    public static ControlPanel getInstance(){
        return instance == null ? new ControlPanel() : instance;
    }

    private static final String NAME_OF_KEY_FIELD = "  KEY:";

    private JButton decrypt;
    private JButton encrypt;
    private JTextField keyField;
    private ActionLog log;

    private ControlPanel() {
        this.log = ActionLog.getInstance();
        this.decrypt = new JButton("DECRYPT");
        this.encrypt = new JButton("ENCRYPT");
        this.keyField = new JTextField();

        init();
    }

    private void init() {
        setMinimumSize(new Dimension(0, 140));
        setLayout(new BorderLayout());
        add(setCryptController(), BorderLayout.NORTH);
        add(setLogPane(this.log), BorderLayout.CENTER);
    }

    private JPanel setCryptController() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(setKeyTextField());
        panel.add(setButtons(this.decrypt, this.encrypt), BorderLayout.EAST);
        return panel;
    }


    private JPanel setKeyTextField() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(NAME_OF_KEY_FIELD), BorderLayout.WEST);
        panel.add(this.keyField, BorderLayout.CENTER);
        return panel;
    }


    private JPanel setButtons(JButton ...buttons) {
        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        Arrays.stream(buttons).forEach(panel::add);

        return panel;
    }
    private JScrollPane setLogPane(JTextArea log) {
        JScrollPane scroll = new JScrollPane(log);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scroll;
    }
}
