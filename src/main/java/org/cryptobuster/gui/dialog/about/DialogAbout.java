package org.cryptobuster.gui.dialog.about;

import org.cryptobuster.gui.MainFrame;
import org.cryptobuster.gui.dialog.Dialog;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DialogAbout extends Dialog {

    private static final Font FONT = new Font("Calibri", Font.BOLD, 14);

    private static final InputStream PATH = DialogAbout.class.getClassLoader().getResourceAsStream("author.xml");

    private static final int WIDTH = 400, HEIGHT = 300;

    private static DialogAbout dialogAbout;

    private Author author;

    private JPanel panel;

    private JButton button;


    private DialogAbout(Frame owner, String title) {
        super(owner, title);
    }

    public static DialogAbout getInstance(Frame owner, String title){
        return dialogAbout == null ? new DialogAbout(owner, title) : dialogAbout;
    }

    public void open(){
        setVisible(true);
    }

    @Override
    protected void dialogInit() {
        super.dialogInit();
        this.author = getAuthorFromXml();
        this.button = new JButton("OK");
        this.button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        setResizable(false);
//        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        labelInit();

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridy = 0;
        constraints.insets = new Insets(30,70,20,70);
        add(this.panel, constraints);
        constraints.insets = new Insets(20,0,17,0);
        constraints.gridy = 1;
        add(this.button, constraints);
        pack();
        setCenter();
    }

    private void labelInit() {
        this.panel = new JPanel();

        List<JLabel> labelList = new ArrayList<>();
        labelList.add(new JLabel(this.author.getStudent().getAttributeName() + ":"));
        labelList.add(new JLabel("\t\t\t\t" + this.author.getStudent().getName() + " " + this.author.getStudent().getLastname() + " " + this.author.getStudent().getParentname()));
        labelList.add(new JLabel(this.author.getGroup().getAttributeName() + ": " + this.author.getGroup().getElementName()));
        labelList.add(new JLabel(this.author.getSubject().getAttributeName() + ": " + this.author.getSubject().getElementName()));

        labelList.forEach(label -> {
            label.setFont(FONT);
            label.setBorder(new CompoundBorder(label.getBorder(), new EmptyBorder(7,0,7,0)));
            panel.add(label);
        });
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }

    private static Author getAuthorFromXml() {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        AuthorHandler handler;
        try {
            SAXParser parser = parserFactory.newSAXParser();
            handler = new AuthorHandler();
            parser.parse(PATH, handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        return handler.getAuthor();
    }

}
