package org.cryptobuster.gui.manager;

import org.cryptobuster.cryptography.Crypto;
import org.cryptobuster.cryptography.caesar.*;
import org.cryptobuster.gui.MainFrame;
import org.cryptobuster.gui.component.ActionLog;
import org.cryptobuster.gui.component.MenuItem;
import org.cryptobuster.gui.dialog.alert.AlertDialog;
import org.cryptobuster.gui.panels.Editor;
import org.cryptobuster.gui.panels.InformationPanel;
import org.cryptobuster.gui.panels.TabbedEditors;

import javax.crypto.BadPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public class CryptManager {

    public static final String ENCRYPTED_DATA = "ENCRYPTED_DATA";
    public static final String DECRYPTED_DATA = "DECRYPTED_DATA";
    public static final String WRONG_KEY = "WRONG KEY";


    private static CryptManager instance;

    public static CryptManager getInstance(MainFrame own){
        return instance == null ? new CryptManager(own) : instance;
    }

    private JMenu menuCipher;

    private TabbedEditors tabbedEditor;

    private JTextField keyField;

    private JButton decrypt;

    private JButton encrypt;

    private ActionLog log;

    private InformationPanel informationPanel;

    private AlertDialog alertDialog;

    private Crypto crypto;


    private CryptManager(MainFrame own) {
        this.menuCipher = own.getMenuCipher();

        this.tabbedEditor = own.getCryptPanel().getFileManagementPanel().getTabEditorPanel().getTabbedEditors();
        this.keyField = own.getCryptPanel().getControlPanel().getKeyField();
        this.decrypt = own.getCryptPanel().getControlPanel().getDecrypt();
        this.encrypt = own.getCryptPanel().getControlPanel().getEncrypt();
        this.log = own.getCryptPanel().getControlPanel().getLog();
        this.informationPanel = own.getInformationPanel();
        this.alertDialog = new AlertDialog(own);
        log.setCaretColor(Color.BLUE);

        initMenuCipherItems(this.menuCipher);
        initListener();
    }

    private void initMenuCipherItems(JMenu menu) {
        menu.add(new MenuItem("Caesar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crypto = new Caesar();
                informationPanel.setCryptoSystemName("Caesar");
            }
        }));
        menu.addSeparator();
        menu.add(new MenuItem("Trithemius", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crypto = new Trithemius();
                informationPanel.setCryptoSystemName("Trithemius");
            }
        }));
        menu.addSeparator();
        menu.add(new MenuItem("Gamma", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crypto = new Gamma();
                informationPanel.setCryptoSystemName("Gamma");
            }
        }));
        menu.addSeparator();
        menu.add(new MenuItem("DES", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crypto = new DES();
                informationPanel.setCryptoSystemName("DES");
            }
        }));
        menu.addSeparator();
        menu.add(new MenuItem("Triple DES", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crypto = new TripleDES();
                informationPanel.setCryptoSystemName("Triple DES");
            }
        }));
        menu.addSeparator();
        menu.add(new MenuItem("AES", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crypto = new AES();
                informationPanel.setCryptoSystemName("AES");
            }
        }));
    }

    private void initListener() {

        this.decrypt.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(keyAndTabIsExist()){
                    Editor editor = tabbedEditor.getSelectedEditor();
                    String text = new String();
                    try {
                        text = listToString(crypto.decrypt(toList(editor.getInputTextArea().getText()), toList(keyField.getText())));
                    } catch (BadPaddingException ex) {
                        text = wrongKey(editor);
                    }
                    editor.getOutputTextArea().setText(text);
                    toInform(DECRYPTED_DATA, editor);
                }
            }
        });

        this.encrypt.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(keyAndTabIsExist()){
                    Editor editor = tabbedEditor.getSelectedEditor();
                    String text = null;
                    try {
                        text = listToString(crypto.encrypt(toList(editor.getInputTextArea().getText()), toList(keyField.getText())));
                    } catch (BadPaddingException ex) {
                        text = wrongKey(editor);
                    }
                    editor.getOutputTextArea().setText(text);
                    toInform(ENCRYPTED_DATA, editor);
                }
            }
        });
    }

    private void toInform(String decryptedData, Editor editor) {
        informationPanel.setCharsInfo(editor.getInputCharsSize(), editor.getOutputCharsSize());
        log.toLog(DECRYPTED_DATA, informationPanel.getCipherName() + "\t" + informationPanel.getCharsInfo(), editor.getPath().toString());
    }

    private String wrongKey(Editor editor) {
        String text = "";
        log.toLog(WRONG_KEY, informationPanel.getCipherName() + "\t" + informationPanel.getCharsInfo(), editor.getPath().toString());
        return text;
    }

    private boolean keyAndTabIsExist() {
        String title = "WRONG INPUT";
        if(this.crypto == null){
            this.alertDialog.open(title, "Please select a cipher");
            return false;
        } else if(this.tabbedEditor.tabIsNull()){
            this.alertDialog.open(title, "The file was not opened");
            return false;
        }
        else if (this.keyField.getText().isEmpty()){
            this.alertDialog.open(title, "Please enter your key");
            return false;
        }
        return true;
    }

    private String listToString(List<Character> chars){
        return chars.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private static java.util.List<Character> toList(String str){
        return toList(str.toCharArray());
    }

    private static List<Character> toList(char[] chars){
        return IntStream.range(0, chars.length).mapToObj(i -> chars[i]).toList();
    }
}
