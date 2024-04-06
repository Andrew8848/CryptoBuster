package org.cryptobuster.gui.panels;

import lombok.Data;
import org.cryptobuster.gui.component.Split;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.charset.Charset;
import java.nio.file.Path;

@Data
public class Editor extends JPanel{
    private static final String INPUT_STR = "INPUT";
    private static final String OUTPUT_STR = "OUTPUT";

    private static final String WRITE_MODE = "WRITE MODE";
    private static final String READ_MODE = "READ MODE";

    private static final String EDITED = INPUT_STR + " [EDITED]";


    private JLabel inputLabel;
    private JLabel outputLabel;

    private JToggleButton allowEdit;

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;

    private byte[] inputData;
    private byte[] outputData;

    private boolean textAreaIsChanged;

    private Charset encoder;

    private Path path;

    public Editor(Path path, Charset encoder) {

        this.allowEdit = new JToggleButton(READ_MODE);

        this.inputLabel = new JLabel(INPUT_STR);
        this.outputLabel = new JLabel(OUTPUT_STR);

        this.inputTextArea = new JTextArea();
        this.outputTextArea = new JTextArea();

        this.inputTextArea.setEditable(false);
        this.outputTextArea.setEditable(false);

        this.textAreaIsChanged = false;

        this.encoder = encoder;

        this.path = path;

        init();
    }


    public long getInputCharsSize(){
        return this.inputTextArea.getText().chars().count();
    }

    public long getOutputCharsSize(){
        return this.outputTextArea.getText().chars().count();
    }

    public void setDataInInputTextArea(byte[] inputData){
        this.inputTextArea.setText(new String(inputData, this.encoder));
    }

    public void setDataInOutputTextArea(byte[] outputData){
        this.outputTextArea.setText(new String(outputData, this.encoder));
    }

    private void setDataFromInputTextArea() {
        this.inputData = this.inputTextArea.getText().getBytes(this.encoder);
    }

    private void setDataFromOutputTextArea() {
        this.outputData = this.outputTextArea.getText().getBytes(this.encoder);
    }

    private void init() {

        setMinimumSize(new Dimension(200,200));
        Split splitPane = new Split(JSplitPane.VERTICAL_SPLIT, setInputPane(this.inputTextArea, INPUT_STR), setOutputPane(this.outputTextArea, OUTPUT_STR));
        splitPane.setResizeWeight(0.5d);

        this.allowEdit.setPreferredSize(new Dimension(120, 15));

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);

        setInputTextAreaListener();
        setToggleEditableListener(this.allowEdit);
    }

    private void setToggleEditableListener(JToggleButton toggleButton) {
        toggleButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(toggleButton.isSelected()){
                    toggleButton.setText(WRITE_MODE);
                    inputTextArea.setEditable(true);
                } else{
                    toggleButton.setText(READ_MODE);
                    inputTextArea.setEditable(false);
                }
            }
        });
    }

    private void setInputTextAreaListener(){
            this.inputTextArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if(allowEdit.isSelected()) {
                        textAreaIsChanged = true;
                        if(!inputLabel.getText().equals(EDITED)) inputLabel.setText(EDITED);
                    }
                }
            });
    }

    private Component setInputPane(JTextArea editor, String name) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(getInputHead(name), BorderLayout.NORTH);
        panel.add(setInScrollPane(editor), BorderLayout.CENTER);
        return panel;
    }

    private Component setOutputPane(JTextArea editor, String name) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(this.outputLabel, BorderLayout.NORTH);
        panel.add(setInScrollPane(editor), BorderLayout.CENTER);
        return panel;
    }

    private JPanel getInputHead(String name) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(this.inputLabel, BorderLayout.LINE_START);
        panel.add(this.allowEdit, BorderLayout.LINE_END);
        return panel;
    }

    private Component setInScrollPane(JTextArea editor) {
        JScrollPane scrollPane = new JScrollPane(editor);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMinimumSize(new Dimension(0, 20));
        return scrollPane;
    }

    public byte[] getInputData() {
        if(textAreaIsChanged) setDataFromInputTextArea();
        return inputData;
    }

    public byte[] getOutputData() {
        if(textAreaIsChanged) setDataFromOutputTextArea();
        return outputData;
    }

    public void setInputData(byte[] inputData) {
        this.inputData = inputData;
        setDataInInputTextArea(inputData);
    }

    public void setOutputData(byte[] outputData) {
        this.outputData = outputData;
        setDataInOutputTextArea(outputData);
    }
}
