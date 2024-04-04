package org.cryptobuster.gui.manager;

import lombok.Getter;
import org.cryptobuster.gui.MainFrame;
import org.cryptobuster.gui.component.*;
import org.cryptobuster.gui.dialog.alert.AlertDialog;
import org.cryptobuster.gui.panels.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class FileManager {

    public static final String CREATED_FILE = "CREATED FILE";

    public static final String FILE_EXIST = "FILE EXIST";

    public static final String OPENED_FILE = "OPENED FILES";

    public static final String OPENED_FOLDER = "OPENED ROOT FOLDER";

    public static final String SAVE = "SAVED";

    public static final String SAVE_AS = "SAVED AS";

    public static final String EXPORT = "EXPORTED";

    public static final String ERROR_ENCODER_READ = "ERROR ENCODER READ";

    public static final String ERROR_SAVING_FILE = "ERROR SAVING FILE";

    public static final String ERROR_OPENING_FILE = "ERROR OPENING FILE";

    public static final String ACCESS_DENIED = "ACCESS DENIED";

    private static final Charset encoder = StandardCharsets.UTF_8;

    private static FileManager instance;

    public static FileManager getInstance(MainFrame own) {
        return instance == null ? new FileManager(own) : instance;
    }

    private final JMenu fileMenu;
    private final FilePanel filePanel;
    private final TabEditorPanel tabEditorPanel;
    private final ActionLog log;
    private final InformationPanel information;

    private final AlertDialog alertDialog;

    private Path rootPath;



    private  FileManager(MainFrame own) {

        this.fileMenu = own.getMenuFile();
        this.filePanel = own.getCryptPanel().getFileManagementPanel().getFilePanel();
        this.tabEditorPanel = own.getCryptPanel().getFileManagementPanel().getTabEditorPanel();
        this.log = own.getCryptPanel().getControlPanel().getLog();
        this.information = own.getInformationPanel();
        this.alertDialog = new AlertDialog(own);

        setHome();

        setMenuItemListeners(this.fileMenu);
        setMenuListenerOnEmptyTab(this.tabEditorPanel);
        setFilePanelListener(this.filePanel);
        addTabSelectionListener(this.tabEditorPanel.getTabbedEditors());
    }

    private void setHome() {
        setRootPath(Paths.get(System.getProperty("user.home")));
    }


    public void create(){
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        fileChooser.setCurrentDirectory(this.filePanel.getTree().getLastLocation().toFile());
        fileChooser.setFileFilter(setFilter("txt", "txt files (*.txt)"));
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            Path path = getPath(fileChooser);
            if(Files.notExists(path)) {
                try {
                    Files.createFile(path);
//                    setRootPath(path.getParent());
                    createTab(path);
                    this.log.toLog(CREATED_FILE, fileChooser.getSelectedFile().getName(), path.toString());
                } catch (IOException e) {
                    log.toLog(ERROR_SAVING_FILE, "possible the problem is include with permission", path.toString());
                }
            } else {
                this.log.toLog(FILE_EXIST, path.getFileName().toString(),path.toString());
            }
        }
    }


    public void openFiles(){
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        fileChooser.setMultiSelectionEnabled(true);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){

            List<Path> paths = Arrays.stream(fileChooser.getSelectedFiles()).map(File::toPath).toList();

            paths.forEach(path -> {
                if (this.tabEditorPanel.getTabbedEditors().notExist(path)){
                   try {
                       createTab(path);
                       this.log.toLog(OPENED_FILE, path.getFileName().toString(),  path.toString());
                   } catch (IOException e){
                       log.toLog(ERROR_OPENING_FILE, "possible the problem is include with permission or encoder", paths.toString());
                   }
                } else {
                    this.log.toLog(FILE_EXIST, path.getFileName().toString(), path.toString());
                }
            });
            setRootPath(getRootOfPaths(paths.stream().map(Path::getParent).toList()));
        }
    }



    public void openFolder(){
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            Path path = fileChooser.getSelectedFile().toPath();

            setRootPath(path);

            this.log.toLog(OPENED_FOLDER, "set root path", path.toString());
        }
    }

    public void save(){
        Editor editor = this.tabEditorPanel.getTabbedEditors().getSelectedEditor();
        if(editor != null){
            Path path = editor.getPath();
            saveFile(path, editor);
        } else{
            this.alertDialog.open("WARNING", "it is impossible to save files because there are no open windows");
        }
    }

    public void saveAll(){
        Editor[] editors = this.tabEditorPanel.getTabbedEditors().getAllTabs();

        Arrays.stream(editors).forEach(editor -> {
            Path path = editor.getPath();
            saveFile(path, editor);
        });

    }

    private void saveFile(Path path, Editor editor) {
        this.alertDialog.open("WARNING", "The " + path.getFileName().toString() + " can destroy the source data after saving", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            try {
                if(Files.notExists(path)) Files.createFile(editor.getPath());
                Files.writeString(editor.getPath(), editor.getTextArea().getText(), encoder);
                log.toLog(SAVE, "successfully saved the " + path.getFileName().toString() + " file", editor.getPath().toString());
            } catch (IOException ex) {
                log.toLog(ERROR_SAVING_FILE, "possible the problem is include with permission", editor.getPath().toString());
            }
            }
        });
    }

    public void saveAs(){
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        fileChooser.setCurrentDirectory(this.filePanel.getTree().getLastLocation().toFile());
        fileChooser.setFileFilter(setFilter("txt", "txt files (*.txt)"));
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            Path path = getPath(fileChooser);
            try {
                if(Files.notExists(path)) Files.createFile(path);
                Files.writeString(path, this.tabEditorPanel.getTabbedEditors().getSelectedEditor().getTextArea().getText());
                setRootPath(path.getParent());
                this.log.toLog(SAVE_AS, fileChooser.getSelectedFile().getName(), path.toString());
            } catch (IOException e) {
                log.toLog(ERROR_SAVING_FILE, "possible the problem is include with permission", path.toString());
            }
        }

    }


    public void export(){
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        fileChooser.setCurrentDirectory(this.filePanel.getTree().getLastLocation().toFile());
        fileChooser.setFileFilter(setFilter("txt", "txt files (*.txt)"));
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            Path path = getPath(fileChooser);
            try {
                if(Files.notExists(path)) Files.createFile(path);
                Files.writeString(path, this.tabEditorPanel.getTabbedEditors().getSelectedEditor().getResult().getText());
                setRootPath(path.getParent());
                this.log.toLog(SAVE_AS, fileChooser.getSelectedFile().getName(), path.toString());
            } catch (IOException e) {
                log.toLog(ERROR_SAVING_FILE, "possible the problem is include with permission", path.toString());
            }
        }
    }

    private void setMenuItemListeners(JMenu menu) {

        menu.add(new MenuItem(
                "New File",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        create();
                    }
                }
        ));

        menu.addSeparator();

        menu.add(new MenuItem(
                "Open File",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openFiles();
                    }
                }
        ));

        menu.add(new MenuItem(
                "Open Folder",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openFolder();
                    }
                }
        ));

        menu.addSeparator();

        menu.add(new MenuItem(
                "Save",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        save();
                    }
                }
        ));

        menu.add(new MenuItem(
                "Save As",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveAs();
                    }
                }
        ));
        menu.add(new MenuItem(
                "Save All",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveAll();
                    }
                }
        ));

        menu.addSeparator();

        menu.add(new MenuItem(
                "Export",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        export();
                    }
                }
        ));

    }


    private Path getPath(JFileChooser fileChooser) {
        Path path = fileChooser.getSelectedFile().toPath();
        if(hasExtension(fileChooser.getSelectedFile().toPath())) {
            return path;
        }
        return path.resolveSibling(path + getExtensionFromDescription(fileChooser));
    }

    private boolean hasExtension(Path path) {
        Pattern pattern = Pattern.compile("\\.[a-z]+$");
        Matcher matcher = pattern.matcher(path.toString());
        return matcher.find();
    }


    private String getExtensionFromDescription(JFileChooser chooser) {
        Pattern pattern = Pattern.compile("(?<=\\(\\*)[^(]+[^)](?=\\))");
        Matcher matcher = pattern.matcher(chooser.getFileFilter().getDescription());
        if(matcher.matches()){
            return matcher.group();
        }
        return ".txt";
    }

    private void setFilePanelListener(FilePanel filePanel) {
        filePanel.getTree().setLog(this.log);
        setTextFieldListener(filePanel);
        setTreeClickListener(filePanel);
    }

    private void setTreeClickListener(FilePanel filePanel) {
        filePanel.getTree().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Tree tree = filePanel.getTree();
                if (e.getClickCount() == 2) {
                    selectFile(e, tree);
                }
                if (e.getClickCount() == 3) {
                    selectPath(e, tree);
                }
            }
        });
    }

    private void selectPath(MouseEvent e, Tree tree) {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1) {
            PathCell cell = (PathCell) (((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject());
            if (cell.getType() == TypeCell.FOLDER) {
                setRootPath(cell.getFullPath());
            } else if (cell.getType() == TypeCell.ROOT) {
                setRootPath(cell.getFullPath().getParent());
            }
        }
    }

    private void selectFile(MouseEvent e, Tree tree) {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1) {
            PathCell cell = (PathCell) (((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject());
            if (cell.getType() == TypeCell.FILE) {
                try {
                    createTab(cell.getFullPath());
                } catch (MalformedInputException ex) {
                    this.log.toLog(ERROR_ENCODER_READ, "file extension is not supported", "");
                } catch (IOException ex) {
                    log.toLog(ERROR_OPENING_FILE, "possible the problem is include with permission or encoder", cell.getFullPath().toString());
                }
            }
        }
    }

    private void setTextFieldListener(FilePanel filePanel) {
        filePanel.getTextField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRootPath(Paths.get(e.getActionCommand()));
            }
        });
    }

    private Path getRootOfPaths(List<Path> paths) {
        List<Path> list = paths;
        while(!isAllMatch(paths)){
            paths = paths.stream().map(Path::getParent).toList();
        }
        return paths.getFirst();
    }

    private static boolean isAllMatch(List<Path> paths) {
        return paths.stream().allMatch(path -> paths.getFirst().toString().equals(path.toString()));
    }


    private static FileFilter setFilter(String fileExstension, String description) {
        return new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(fileExstension);
                }
            }

            @Override
            public String getDescription() {
                return description;
            }
        };
    }

    private void createTab(Path path) throws IOException {
        this.tabEditorPanel.add(path, Files.readString(path, encoder));

    }

    private void setRootPath(Path path) {
        if(!Files.isDirectory(path)) path = path.getParent();
        this.rootPath = path;
        this.filePanel.getTextField().setText(path.toString());
        try {
            this.filePanel.getTree().setRootPath(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setMenuListenerOnEmptyTab(TabEditorPanel tabEditorPanel){
        tabEditorPanel.getEmptyTabPanel().addAction("Create new file", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) create();
            }
        });
        tabEditorPanel.getEmptyTabPanel().addAction("Open file", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) openFiles();
            }
        });
        tabEditorPanel.getEmptyTabPanel().addAction("Open Folder", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1) openFolder();
            }
        });
    }

    private void addTabSelectionListener(TabbedEditors tabbedEditors){
        tabbedEditors.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(!tabbedEditors.tabIsNull()){
                    setCharsInformation(tabbedEditors.getSelectedEditor().getCharsSize());
                    addListenerEditor(tabbedEditors.getSelectedEditor());
                }
            }
        });
    }

    private void addListenerEditor(Editor editor){
        editor.getTextArea().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                setCharsInformation(editor.getCharsSize());
            }
        });
    }

    private void setCharsInformation(long chars){
        this.information.setCharsInfo(chars);
    }

}