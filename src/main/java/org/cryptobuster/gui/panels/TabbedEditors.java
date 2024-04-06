package org.cryptobuster.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;

public class TabbedEditors extends JTabbedPane {


    public TabbedEditors() {
    }

    public Editor add(Path path, byte[] data, Charset encoder) {
        Editor editor = new Editor(path, encoder);
        editor.setInputData(data);
//        editor.setDataInInputTextArea();
        if (notExist(path)) return (Editor) super.add(path.getFileName().toString(), editor);
        return null;
    }


    public int indexOfPath(Path path) {
        for(int i = 0; i < getTabCount(); i++) {
            if (getEditorAt(i).getPath().toString().equals(path.toString())) {
                return i;
            }
        }
        return -1;
    }

    public Editor getEditorAt(int index){
        return (Editor) getComponentAt(index);
    }

    public void setTabComponentAt(int index, Path path, MouseAdapter mouseAdapter) {
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        content.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        content.add(new JLabel(path.getFileName().toString()), constraints);

        constraints.insets = new Insets(0,10,0,0);
        constraints.gridx = 1;
        content.add(getButtonClose(mouseAdapter), constraints);

        super.setTabComponentAt(index, content);
    }

    public Editor getSelectedEditor(){
        return (Editor) getSelectedComponent();
    }

    public boolean tabIsNull() {
        return getTabCount() == 0;
    }

    private JLabel getButtonClose(MouseAdapter mouseAdapter) {
        JLabel label = new JLabel("×");
        label.addMouseListener(mouseAdapter);
        return label;
    }

    public boolean notExist(Path path) {
        return Arrays.stream(getAllTabs()).map(Editor::getPath).noneMatch(p -> path.toString().equals(p.toString()));
    }

    public Editor[] getAllTabs(){
        Editor[] editors = new Editor[getTabCount()];
        for (int i = 0; i < editors.length; i++) {
            editors[i] = getEditorAt(i);
        }
        return editors;
    }
}
