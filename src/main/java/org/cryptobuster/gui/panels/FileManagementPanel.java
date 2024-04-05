package org.cryptobuster.gui.panels;

import lombok.Getter;
import org.cryptobuster.gui.component.Split;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Getter
public class FileManagementPanel extends JPanel {

    private static FileManagementPanel instance;

    private FilePanel filePanel;

    private TabEditorPanel tabEditorPanel;


    public static FileManagementPanel getInstance(){
        return instance == null ? new FileManagementPanel() : instance;
    }

    private FileManagementPanel() {
        this.filePanel = FilePanel.getInstance();
        this.tabEditorPanel = TabEditorPanel.getInstance();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        SplitFileAndTab();
    }

    private void SplitFileAndTab() {
        Split splitPane = new Split(JSplitPane.HORIZONTAL_SPLIT, filePanel, tabEditorPanel);
        splitPane.setResizeWeight(0.1d);
        splitPane.setDividerLocation(210);
        add(splitPane);
    }
    

}