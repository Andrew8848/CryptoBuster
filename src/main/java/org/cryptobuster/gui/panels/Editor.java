package org.cryptobuster.gui.panels;

import lombok.Data;
import org.cryptobuster.gui.component.Split;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

@Data
public class Editor extends JPanel{
    private static final String EDITOR_STR = "INPUT";
    private static final String RESULT_STR = "OUTPUT";

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;

    private Path path;

    public Editor(Path path) {
        this.path = path;
        init();
    }


    public long getInputCharsSize(){
        return this.inputTextArea.getText().chars().count();
    }

    public long getOutputCharsSize(){
        return this.outputTextArea.getText().chars().count();
    }

    public long getCharsSizeFromSelectedEditor(){
        return this.inputTextArea.getText().chars().count();
    }

    private void init() {
        this.inputTextArea = new JTextArea();
        this.outputTextArea = new JTextArea();
        this.outputTextArea.setEditable(false);


        Split splitPane = new Split(JSplitPane.VERTICAL_SPLIT, setPane(this.inputTextArea, EDITOR_STR), setPane(this.outputTextArea, RESULT_STR));
        splitPane.setResizeWeight(0.5d);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    private Component setPane(JTextArea editor, String name) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(name), BorderLayout.NORTH);
        panel.add(setInScrollPane(editor), BorderLayout.CENTER);
        return panel;
    }

    private Component setInScrollPane(JTextArea editor) {
        JScrollPane scrollPane = new JScrollPane(editor);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMinimumSize(new Dimension(0, 20));
        return scrollPane;
    }
}
