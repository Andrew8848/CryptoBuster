package org.cryptobuster.gui.childwindows;

import org.cryptobuster.cryptography.AlphabetHandler;
import org.cryptobuster.gui.MainFrame;
import org.cryptobuster.gui.component.Split;
import org.cryptobuster.gui.dialog.alert.AlertDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class TrithemiusKeyFinder extends JFrame {

    private static final Charset encoder = StandardCharsets.UTF_8;

    private JTextArea alphabetTextArea;
    private JTextArea plainText;
    private JTextArea cipherText;
    private final JTextArea keyOutput;


    private JButton findButton;

    private char[] alphabet;
    private AlertDialog alert;

    public TrithemiusKeyFinder(MainFrame owner) {
        this.alphabet = AlphabetHandler.getEnAlphabet();

        this.alphabetTextArea = new JTextArea();
        this.plainText = new JTextArea();
        this.cipherText = new JTextArea();
        this.keyOutput = new JTextArea();
        this.findButton = new JButton("FIND KEY");

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

        this.findButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyOutput.setText(extractKey(plainText.getText(), cipherText.getText(), new String(alphabet)));
            }
        });
    }

    private String extractKey(String plainText, String cipherText, String alphabet) {
        StringBuilder key = new StringBuilder();

        IntStream.range(0, plainText.length()).forEach(i -> {
            int plainIndex = alphabet.indexOf(plainText.charAt(i));
            int cipherIndex = alphabet.indexOf(cipherText.charAt(i));

            if (plainIndex != -1 && cipherIndex != -1) {
                int keyExtractor = (cipherIndex - plainIndex + alphabet.length()) % alphabet.length();
                key.append(keyExtractor).append(" ");
            } else {
                key.append("?");
            }
        });
        return key.toString();
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


        Split firstSplitPane = new Split(JSplitPane.VERTICAL_SPLIT, getTextAreaWithLabelPanel("ALPHABET", this.alphabetTextArea), getTextAreaWithLabelPanel( "PLAIN TEXT", this.plainText));
        Split secondSplitPane = new Split(JSplitPane.VERTICAL_SPLIT, firstSplitPane, getTextAreaWithLabelPanel( "CIPHER TEXT", this.cipherText));
        Split lastSplitPane = new Split(JSplitPane.VERTICAL_SPLIT, secondSplitPane, getTextAreaWithLabelPanel( "OUTPUT KEY", this.keyOutput));


        firstSplitPane.setResizeWeight(0.6D);
        secondSplitPane.setResizeWeight(0.7D);
        lastSplitPane.setResizeWeight(0.7D);


        panel.add(lastSplitPane);
        return panel;
    }

    private JPanel getTextAreaWithLabelPanel(String INPUT, JTextArea textArea) {
        JPanel panel = new JPanel();

        textArea.setWrapStyleWord(true);
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
        panel.setLayout(new BorderLayout());
        panel.add(this.findButton, BorderLayout.LINE_END);
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
