package org.cryptobuster.gui.manager;

import org.cryptobuster.cryptography.AlphabetHandler;
import org.cryptobuster.cryptography.Crypto;
import org.cryptobuster.cryptography.cipher.*;
import org.cryptobuster.cryptography.util.SecretKeyGenerator;
import org.cryptobuster.gui.MainFrame;
import org.cryptobuster.gui.childwindows.StrongCombinationWindow;
import org.cryptobuster.gui.childwindows.TrithemiusKeyFinder;
import org.cryptobuster.gui.component.ActionLog;
import org.cryptobuster.gui.component.MenuItem;
import org.cryptobuster.gui.dialog.alert.AlertDialog;
import org.cryptobuster.gui.panels.Editor;
import org.cryptobuster.gui.panels.InformationPanel;
import org.cryptobuster.gui.panels.TabbedEditors;

import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;
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

    public static final String KEY_GENERATE = "GENERATED KEY";


    public static final Charset encoder = StandardCharsets.UTF_8;


    private static CryptManager instance;

    public static CryptManager getInstance(MainFrame own){
        return instance == null ? new CryptManager(own) : instance;
    }

    private JMenu menuCipher;

    private JMenu menuTools;

    private TabbedEditors tabbedEditor;

    private JTextField keyField;

    private JButton decrypt;

    private JButton encrypt;

    private ActionLog log;

    private InformationPanel informationPanel;

    private AlertDialog alertDialog;

    private StrongCombinationWindow strongCombinationWindow;

    private TrithemiusKeyFinder trithemiusKeyFinder;

    private Crypto crypto;


    private CryptManager(MainFrame owner) {
        this.menuCipher = owner.getMenuCipher();
        this.menuTools = owner.getMenuTools();

        this.tabbedEditor = owner.getCryptPanel().getFileManagementPanel().getTabEditorPanel().getTabbedEditors();
        this.keyField = owner.getCryptPanel().getControlPanel().getKeyField();
        this.decrypt = owner.getCryptPanel().getControlPanel().getDecrypt();
        this.encrypt = owner.getCryptPanel().getControlPanel().getEncrypt();
        this.log = owner.getCryptPanel().getControlPanel().getLog();
        this.informationPanel = owner.getInformationPanel();
        this.alertDialog = new AlertDialog(owner);
        this.strongCombinationWindow = new StrongCombinationWindow(owner);
        this.trithemiusKeyFinder = new TrithemiusKeyFinder(owner);

        log.setCaretColor(Color.BLUE);

        initMenuCipherItems(this.menuCipher);
        initToolsMenu(this.menuTools);
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
                informationPanel.setCryptoSystemName("Caesar UA");
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

    private void initToolsMenu(JMenu menu){
        JMenu attackerMenu = initAttackerMenu("Attacker Tools");
        JMenu keyGeneratorMenu = initKeyGeneratorMenu("Key Generator");
        menu.add(attackerMenu);
        menu.add(keyGeneratorMenu);
    }

    private JMenu initAttackerMenu(String name) {
        JMenu menu = new JMenu(name);

        menu.add(new MenuItem("Caesar Strong Combination", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strongCombinationWindow.open();
            }
        }));
        menu.add(new MenuItem("Trithemius Key Finder", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trithemiusKeyFinder.open();
            }
        }));


        return menu;
    }

    private JMenu initKeyGeneratorMenu(String name) {
        JMenu menu = new JMenu(name);

        menu.add(new MenuItem("Generate Key 2048", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyGenerator(2048);
            }
        }));

        menu.add(new MenuItem("Generate Key 1024", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyGenerator(1024);
            }
        }));

        menu.add(new MenuItem("Generate Key 512", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyGenerator(512);
            }
        }));

        menu.add(new MenuItem("Generate Key 256", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyGenerator(256);
            }
        }));

        menu.add(new MenuItem("Generate Key 128", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyGenerator(128);
            }
        }));

        menu.add(new MenuItem("Generate Key 64", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyGenerator(64);
            }
        }));

        return menu;
    }

    private void keyGenerator(int length) {
        byte[] key = SecretKeyGenerator.generateKey(length);
        keyField.setText(new String(key, encoder));
        this.log.toLog(KEY_GENERATE, new String(key, encoder), "");
   }

    private void initListener() {

        this.decrypt.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(keyAndTabIsExist()){
                    Editor editor = tabbedEditor.getSelectedEditor();
                    try {
                        byte[] data = crypto.decrypt(editor.getInputData(), keyField.getText().getBytes(encoder));
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
