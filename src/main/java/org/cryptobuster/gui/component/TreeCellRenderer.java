package org.cryptobuster.gui.component;

import org.cryptobuster.gui.MainFrame;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class TreeCellRenderer extends DefaultTreeCellRenderer {

    private static final URL HOUSE_ICO_PATH = TreeCellRenderer.class.getClassLoader().getResource("house.png");
    private static final URL EMPTY_ICO_PATH = TreeCellRenderer.class.getClassLoader().getResource("empty.png");
    private static final URL DENIED_ICO_PATH = TreeCellRenderer.class.getClassLoader().getResource("denied.png");
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

    private static ImageIcon getScaledIcon(URL path) {
        try {
            BufferedImage bufferedImage = ImageIO.read(path);
            ImageIcon imageIcon = new ImageIcon(bufferedImage);
            imageIcon.getImage().getScaledInstance(8, 8, Image.SCALE_SMOOTH);
            return imageIcon;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
