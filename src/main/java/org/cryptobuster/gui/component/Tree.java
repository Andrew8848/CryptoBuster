package org.cryptobuster.gui.component;

import lombok.Getter;
import lombok.Setter;
import org.cryptobuster.gui.manager.FileManager;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public class Tree extends JTree {

    private DefaultTreeModel model;
    private Path rootPath;

    private Path lastLocation;

    @Setter
    private ActionLog log;

    public Tree() {
        this.model = new DefaultTreeModel(null);
        setCellRenderer(new TreeCellRenderer());
        setHome();
        initListeners();
    }

    private void setHome(){
        try {
            setRootPath(Paths.get(System.getProperty("user.home")));
        } catch (IOException e) {
            try {
                setRootPath(Paths.get(""));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void setRootPath(Path rootPath) throws IOException {
        this.rootPath = rootPath;
        DefaultMutableTreeNode rootNode = renderChildTreeNode(
                new DefaultMutableTreeNode(
                        new PathCell(TypeCell.ROOT, rootPath)
                )
        );
        this.model.setRoot(rootNode);
        setModel(this.model);
    }

    private DefaultMutableTreeNode renderChildTreeNode(DefaultMutableTreeNode node) throws IOException {
        PathCell cell = (PathCell) node.getUserObject();
        try {
            node.removeAllChildren();
            Files.list(cell.getFullPath())
                    .map(Tree::getPathCell)
                    .map(Tree::getMutableTreeNode)
                    .forEach(node::add);
            this.lastLocation = cell.getFullPath();
        } catch (AccessDeniedException e){
            setDenied(node);
            this.lastLocation = this.rootPath;
            if(log != null) log.toLog(FileManager.ACCESS_DENIED, "Can't open the folder content", ((PathCell) node.getUserObject()).getFullPath().toString());
        }
        return node;
    }



    private static PathCell getPathCell(Path path) {
        if(Files.isDirectory(path)) return new PathCell(TypeCell.FOLDER, path);
        return new PathCell(TypeCell.FILE, path);
    }

    private static DefaultMutableTreeNode getMutableTreeNode(PathCell pathCell) {
        if(pathCell.getType() == TypeCell.FOLDER) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(pathCell);
            node.add(new DefaultMutableTreeNode(PathCell.getEmpty()));
            return node;
        }
        return new DefaultMutableTreeNode(pathCell);
    }

    private void initListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Optional<TreePath> treePath = Optional.ofNullable(getSelectionPath());
                if (treePath.isPresent()){
                    PathCell cell = (PathCell) ((DefaultMutableTreeNode) treePath.get().getLastPathComponent()).getUserObject();
                    if (cell.getType() == TypeCell.FILE) {
                        lastLocation = cell.getFullPath().getParent();
                    } else {
                        lastLocation = cell.getFullPath();
                    }
                }
            }
        });


      addTreeWillExpandListener(new TreeWillExpandListener() {
          @Override
          public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
              TreePath source = (TreePath) event.getPath();
              DefaultMutableTreeNode node = (DefaultMutableTreeNode) source.getLastPathComponent();
              if(!isEmptyFolder(node)){
                  try {
                      renderChildTreeNode(node);
                  } catch (IOException e) {

                  }
              }
          }

          @Override
          public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
              TreePath source = (TreePath) event.getPath();
              DefaultMutableTreeNode node = (DefaultMutableTreeNode) source.getLastPathComponent();
//              if(!isEmptyFolder(node)) setNull(node);
          }
      });
    }

    private void setNull(DefaultMutableTreeNode node) {
        node.removeAllChildren();
        node.add(new DefaultMutableTreeNode(PathCell.getEmpty()));
    }

    private void setDenied(DefaultMutableTreeNode node) {
        node.removeAllChildren();
        node.add(new DefaultMutableTreeNode(PathCell.getDenied()));
    }

    private static boolean isEmptyFolder(final DefaultMutableTreeNode node) {
        Path path = ((PathCell) node.getUserObject()).getFullPath();
        if (Files.isDirectory(path)) {
            try (Stream<Path> entries = Files.list(path)) {
                return !entries.findFirst().isPresent();
            } catch (IOException e) {

            }
        }
        return false;
    }

}
