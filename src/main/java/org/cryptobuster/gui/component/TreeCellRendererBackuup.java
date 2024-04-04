package org.cryptobuster.gui.component;

import org.cryptobuster.gui.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class TreeCellRendererBackuup extends DefaultTreeCellRenderer {

    private static final InputStream HOUSE_ICO_PATH = MainFrame.class.getClassLoader().getResourceAsStream("house.png");
    private static final InputStream EMPTY_ICO_PATH = MainFrame.class.getClassLoader().getResourceAsStream("empty.png");
    private static final InputStream DENIED_ICO_PATH = MainFrame.class.getClassLoader().getResourceAsStream("denied.png");

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (value instanceof DefaultMutableTreeNode node) {
            if (node.getUserObject() instanceof PathCell pathCell) {
                if (Objects.requireNonNull(pathCell.getType()) == TypeCell.ROOT) setIcon(getScaledIcon(HOUSE_ICO_PATH));
                else if(Objects.requireNonNull(pathCell.getType()) == TypeCell.EMPTY) setIcon(getScaledIcon(EMPTY_ICO_PATH));
                else if(Objects.requireNonNull(pathCell.getType()) == TypeCell.DENIED) setIcon(getScaledIcon(DENIED_ICO_PATH));
            }
        }

        return this;
    }

    private static ImageIcon getScaledIcon(InputStream path) {
        try {
            BufferedImage io = ImageIO.read(path);
            ImageIcon imageIcon = new ImageIcon(io);
            imageIcon.getImage().getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            return imageIcon;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
