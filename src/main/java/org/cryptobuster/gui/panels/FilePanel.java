package org.cryptobuster.gui.panels;

import lombok.Getter;
import org.cryptobuster.gui.component.Tree;

import javax.swing.*;
import java.awt.*;

@Getter
public class FilePanel extends JPanel {
    private static FilePanel instance;

    public static FilePanel getInstance(){
        return instance == null ? new FilePanel() : instance;
    }

    private JTextField textField;
    private Tree tree;

    private FilePanel() {
        this.textField = new JTextField();
        this.tree = new Tree();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(170, 0));

        add(this.textField, BorderLayout.NORTH);
        add(getScrollableTree(), BorderLayout.CENTER);

        setBackground(Color.GRAY);
    }

    private JScrollPane getScrollableTree() {
        JScrollPane scrollPane = new JScrollPane(this.tree);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

}
