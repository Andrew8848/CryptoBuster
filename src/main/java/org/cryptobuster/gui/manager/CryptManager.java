package org.cryptobuster.gui.manager;

import org.cryptobuster.cryptography.AlphabetHandler;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class CryptManager {

    public static final String ENCRYPTED_DATA = "ENCRYPTED_DATA";
    public static final String DECRYPTED_DATA = "DECRYPTED_DATA";
    public static final String WRONG_KEY = "WRONG KEY";

    public static final String WRONG_INPUT_ARGUMENT = "WRONG INPUT ARGUMENT";

    public static final Charset encoder = StandardCharsets.UTF_8;


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
        menu.add(new MenuItem("Caesar EN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crypto = new Caesar(AlphabetHandler.EN);
                informationPanel.setCryptoSystemName("Caesar EN");
            }
        }));
        menu.add(new MenuItem("Caesar UA", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crypto = new Caesar(AlphabetHandler.UA);
                informationPanel.setCryptoSystemName("Caesar UAr");
            }
        }));
        menu.addSeparator();
        menu.add(new MenuItem("Trithemius EN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crypto = new Trithemius(AlphabetHandler.EN);
                informationPanel.setCryptoSystemName("Trithemius EN");
            }
        }));
        menu.add(new MenuItem("Trithemius UA", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crypto = new Trithemius(AlphabetHandler.UA);
                informationPanel.setCryptoSystemName("Trithemius UA");
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
                    try {
                        byte[] data = crypto.decrypt(editor.getInputData(), keyField.getText().getBytes(encoder));
//                        String text = listToString(crypto.decrypt(editor.getInputData(), keyField.getText().getBytes(encoder)));
//                        editor.getOutputTextArea().setText(text);
                        editor.setOutputData(data);
                        toInform(DECRYPTED_DATA, editor);
                    } catch (BadPaddingException ex) {
                        editor.getOutputTextArea().setText(wrongKey(editor));
                    } catch (IllegalArgumentException ex){
                        toInformTheWrongArgument(editor);
                    }

                }
            }
        });

        this.encrypt.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(keyAndTabIsExist()){
                    Editor editor = tabbedEditor.getSelectedEditor();
                    try {
//                        String text = listToString(crypto.encrypt(editor.getInputData(), keyField.getText().getBytes(encoder)));
//                        editor.getOutputTextArea().setText(text);
                        byte[] data = crypto.encrypt(editor.getInputData(), keyField.getText().getBytes(encoder));
                        editor.setOutputData(data);
                        toInform(ENCRYPTED_DATA, editor);
                    } catch (BadPaddingException ex) {
                        editor.getOutputTextArea().setText(wrongKey(editor));
                    } catch (IllegalArgumentException ex){
                        toInformTheWrongArgument(editor);
                    }

                }
            }
        });
    }

    private void toInformTheWrongArgument(Editor editor) {
        log.toLog(WRONG_INPUT_ARGUMENT, informationPanel.getCryptoSystemName() + "\t" + informationPanel.getCharsInfo(), editor.getPath().toString());
    }

    private void toInform(String decryptedData, Editor editor) {
        informationPanel.setCharsInfo(editor.getInputCharsSize(), editor.getOutputCharsSize());
        log.toLog(DECRYPTED_DATA, informationPanel.getCryptoSystemName() + "\t" + informationPanel.getCharsInfo(), editor.getPath().toString());
    }

    private String wrongKey(Editor editor) {
        String text = "";
        log.toLog(WRONG_KEY, informationPanel.getCryptoSystemName() + "\t" + informationPanel.getCharsInfo(), editor.getPath().toString());
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

    private String listToString(byte[] chars){
        return new String(chars, encoder);
    }

}
