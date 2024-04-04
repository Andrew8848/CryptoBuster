package org.cryptobuster.gui.component;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Getter
public class ActionLog extends JTextArea {

    private static ActionLog instance;

    public static ActionLog getInstance(){
        return instance == null ? new ActionLog() : instance;
    }

    private Logger logger;

    private  ActionLog() {
        this.logger = Logger.getLogger(this.getClass().getName());
        init();
    }

    private void init() {
        setFont(new Font("Calibri", Font.PLAIN, 14));
        setEditable(false);
    }

    public void toLog(String title, String description, String path){
        StringBuilder builder = new StringBuilder();
        builder.append(ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")));
        builder.append(" - ");
        builder.append( "["+title.toUpperCase()+"]:");
        builder.append("\t");
        builder.append("Description: ");
        builder.append(description);
        builder.append("\t");
        builder.append("From: ");
        builder.append(path);
        builder.append("\n");

        append(builder.toString());
    }
}
