package org.cryptobuster.gui.panels;


import lombok.Getter;
import org.cryptobuster.gui.component.Split;

import javax.swing.*;
import java.awt.*;

@Getter
public class CryptPanel extends JPanel {

    private static CryptPanel instance;
    private FileManagementPanel fileManagementPanel;
    private ControlPanel controlPanel;


    public static CryptPanel getInstance() {
        return instance == null ? new CryptPanel() : instance;
    }

    private CryptPanel() {
        this.fileManagementPanel = FileManagementPanel.getInstance();
        this.controlPanel = ControlPanel.getInstance();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        add(this.fileManagementPanel, BorderLayout.CENTER);
        add(this.controlPanel, BorderLayout.SOUTH);


        SplitEditorAndControler();
    }

    private void SplitEditorAndControler() {
        Split splitPane = new Split(JSplitPane.VERTICAL_SPLIT, this.fileManagementPanel, this.controlPanel);
        splitPane.setResizeWeight(0.8d);
        add(splitPane);
    }

}
