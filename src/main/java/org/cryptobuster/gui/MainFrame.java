package org.cryptobuster.gui;

import lombok.Getter;
import org.cryptobuster.gui.component.MenuItem;
import org.cryptobuster.gui.dialog.about.DialogAbout;
import org.cryptobuster.gui.manager.CryptManager;
import org.cryptobuster.gui.manager.FileManager;
import org.cryptobuster.gui.panels.*;
import org.cryptobuster.gui.component.MenuBar;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;

@Getter
public class MainFrame extends JFrame {

    private static final InputStream PATH = MainFrame.class.getClassLoader().getResourceAsStream("icon.png");

    private static final int WIDTH = 1200, HEIGHT = 790;

    private static MainFrame instance;

    private DialogAbout aboutDialog;

    private JMenu menuFile;

    private JMenu menuCipher;

    private JMenu menuTools;

    private JMenu menuMore;

    private CryptPanel cryptPanel;

    private InformationPanel informationPanel;


    private FileManager fileManager;

    private CryptManager cryptManager;


    private MainFrame() {
        this.aboutDialog = DialogAbout.getInstance(this, "About App");

        this.menuFile = new JMenu("File");
        this.menuCipher = new JMenu("Cipher");
        this.menuTools = new JMenu("Tools");
        this.menuMore = new JMenu("More");

        this.cryptPanel = CryptPanel.getInstance();
        this.informationPanel = InformationPanel.getInstance();
        this.fileManager = FileManager.getInstance(this);
        this.cryptManager = CryptManager.getInstance(this);
        init();
    }

    public static MainFrame getInstance() {
        return instance == null ? new MainFrame() : instance;
    }

    public void open() {
        setVisible(true);
    }

    @Override
    public void setContentPane(Container contentPane) {
        super.setContentPane(contentPane);
        getContentPane().removeAll();
        getContentPane().add(contentPane);
        revalidate();
        repaint();
    }

    protected void init() {
        super.frameInit();
        setTitle("CryptoBuster");
        try {
            setIconImage(ImageIO.read(PATH));
        } catch (IOException e) {

        }
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(0,300));
        setCenter();
        initMenuList();
        initInformationPanel();
        initCryptPanel();
    }

    private void setCenter() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
    }

    private void initInformationPanel() {
        add(informationPanel, BorderLayout.SOUTH);
    }

    private void initCryptPanel() {
        add(this.cryptPanel);
    }


    private void initMenuList() {
        MenuBar mainMenuBar = MenuBar.getInstance();
        mainMenuBar.add(this.menuFile);
        mainMenuBar.add(this.menuCipher);
        mainMenuBar.add(this.menuTools);
        mainMenuBar.add(setMenuMoreItems(this.menuMore));
        add(mainMenuBar, BorderLayout.NORTH);
    }

    private JMenu setMenuMoreItems(JMenu menu) {
        menu.add(new MenuItem("About", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAboutDialog();
            }
        }));
        return menu;
    }

    private void openAboutDialog() {
        aboutDialog.open();
    }
}
