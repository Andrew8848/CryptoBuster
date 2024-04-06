package org.cryptobuster.gui.panels;

import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.Charset;
import java.nio.file.Path;

@Getter
public class TabEditorPanel extends JPanel {

    private static final String PATH = "src/main/resources/material/Close.png";

    private static TabEditorPanel instance;

    public static TabEditorPanel getInstance(){
        return instance == null ? new TabEditorPanel() : instance;
    }

    private TabbedEditors tabbedEditors;

    private EmptyTabPanel emptyTabPanel;


    private TabEditorPanel() {
        this.tabbedEditors = new TabbedEditors();
        this.emptyTabPanel = EmptyTabPanel.getInstance();
        init();
    }

    public void add(Path path, byte[] data, Charset encoder){

        if(this.tabbedEditors.tabIsNull()) setTabbedEditors();

        this.tabbedEditors.add(path, data, encoder);

        int index = this.tabbedEditors.indexOfPath(path);

        this.tabbedEditors.setSelectedIndex(index);

        this.getTabbedEditors().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(tabbedEditors.tabIsNull()) {
                    setEmpty();
                }
            }
        });

        this.tabbedEditors.setTabComponentAt(index, path, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tabbedEditors.indexOfPath(path);
                if(index >= 0) {
                    tabbedEditors.removeTabAt(index);
                }
            }
        });
    }

    public void setEmpty(){
        removeAll();
        addContent(this.emptyTabPanel);
        rerender();
    }

    private void setTabbedEditors(){
        removeAll();
        addContent(this.tabbedEditors);
        rerender();
    }

    private void rerender(){
        repaint();
        revalidate();
    }

    private void addContent(JComponent component){
        super.add(component, BorderLayout.CENTER);
    }

    private void init() {
        setLayout(new BorderLayout());
        setEmpty();
    }
}
