package org.cryptobuster.gui.childwindows;

import org.cryptobuster.cryptography.AlphabetHandler;
import org.cryptobuster.cryptography.cipher.Caesar;
import org.cryptobuster.gui.MainFrame;
import org.cryptobuster.gui.component.Split;
import org.cryptobuster.gui.dialog.alert.AlertDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class StrongCombinationWindow extends JFrame {

    private static final Charset encoder = StandardCharsets.UTF_8;

    private JTextArea alphabetTextArea;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JSpinner spinner;

    private char[] alphabet;
    private char[] inputData;

    private AlertDialog alert;

    public StrongCombinationWindow(MainFrame owner) {
        this.alphabet = AlphabetHandler.getEnAlphabet();

        this.alphabetTextArea = new JTextArea();
        this.inputTextArea = new JTextArea();
        this.outputTextArea = new JTextArea();
        this.spinner = new JSpinner(new SpinnerNumberModel(0, 0, this.alphabet.length, 1));

        this.alert = new AlertDialog(this);

        init();
        initListeners();
    }

    private void initListeners() {
        this.alphabetTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char[] newAlphabet = alphabetTextArea.getText().toCharArray();
                boolean alerted = false;
                if (isUnique(newAlphabet)){
                    alphabet = newAlphabet;
                }   else {
                    if(!alerted) {
                        alert.open("WRONG INPUT", "alphabet's elements must be unique");
                        alerted = true;
                    }
                    alphabetTextArea.setText(new String(alphabet));
                }
            }
        });

        this.inputTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
              inputData = inputTextArea.getText().toCharArray();
            }
        });

        this.spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                rotate((Integer) (((JSpinner) e.getSource()).getValue()));
            }
        });
    }
    public void rotate(int key){
        try{
            this.outputTextArea.setText(new String(Caesar.rotate(this.inputData, key, alphabet)));
        }catch (NullPointerException e){
            alert.open("WRONG INPUT", "alphabets or input text cant by empty");
        }
    }

    private boolean isUnique(char[] chars){
        AtomicBoolean checker = new AtomicBoolean(true);
        List<Character> charsList = IntStream.range(0, chars.length)
                .mapToObj(i -> chars[i]).toList();
        charsList.stream().forEach(character -> {
            checker.set(Collections.frequency(charsList, character) == 1);
        });
        return checker.get();
    }

    private void init() {
        setSize(new Dimension(400, 400));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.alphabet = AlphabetHandler.getEnAlphabet();
        this.alphabetTextArea.setText(new String(alphabet));

        setContentPane(setContent());
        setCenter();
    }

    private JPanel setContent() {
        JPanel panel  = new JPanel();

        GroupLayout groupLayout = new GroupLayout(this);
        panel.setLayout(new BorderLayout());

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup());

        panel.add(getInputAndOutput(), BorderLayout.CENTER);

        panel.add(getControllerPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getInputAndOutput() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Split firstSplitPane = new Split(JSplitPane.VERTICAL_SPLIT, getTextAreaWithLabelPanel("ALPHABET", this.alphabetTextArea), getTextAreaWithLabelPanel( "INPUT", this.inputTextArea));
        Split secondSplitPane = new Split(JSplitPane.VERTICAL_SPLIT, firstSplitPane, getTextAreaWithLabelPanel( "OUTPUT", this.outputTextArea));

        firstSplitPane.setResizeWeight(0.5D);
        secondSplitPane.setResizeWeight(0.65D);


//        panel.add(firstSplitPane);
        panel.add(secondSplitPane);
        return panel;
    }

    private JPanel getTextAreaWithLabelPanel(String INPUT, JTextArea textArea) {
        JPanel panel = new JPanel();

        textArea.setWrapStyleWord(true);
//        textArea.setMinimumSize(new Dimension(100, 90));
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(INPUT), BorderLayout.NORTH);
        panel.add(getAsScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane getAsScrollPane(JTextArea alphabetTextArea) {
        JScrollPane scrollPane = new JScrollPane(alphabetTextArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel getControllerPanel() {
        JPanel panel = new JPanel();
        this.spinner.setPreferredSize(new Dimension(50,20));
        panel.setLayout(new BorderLayout());
        panel.add(this.spinner, BorderLayout.LINE_END);
        return panel;
    }

    public void open(){
        setVisible(true);
    }

    private void setCenter() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
    }


}
