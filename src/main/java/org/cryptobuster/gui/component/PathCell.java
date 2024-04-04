package org.cryptobuster.gui.component;

import lombok.Getter;

import java.lang.reflect.Type;
import java.nio.file.Path;

@Getter
public class PathCell {

    private final TypeCell type;
    private final Path fullPath;

    public PathCell(TypeCell type, Path name) {
        this.type = type;
        this.fullPath = name;
    }

    public static PathCell getEmpty(){
        return new PathCell(TypeCell.EMPTY, null);
    }

    public static PathCell getDenied(){
        return new PathCell(TypeCell.DENIED, null);
    }


    @Override
    public String toString() {
        if(this.type == TypeCell.EMPTY) return "EMPTY";
        if(this.type == TypeCell.DENIED) return "DENIED";
        if(this.fullPath.toString().equals("/")) return "/";
        return this.fullPath.getFileName().toString();
    }
}
