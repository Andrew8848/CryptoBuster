package org.cryptobuster.cryptography;

import lombok.Data;
import lombok.Getter;
import org.cryptobuster.gui.dialog.about.DialogAbout;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
public class AlphabetHandler extends DefaultHandler {

    public static final String EN = "en";
    public static final String UA = "ua";

    private static final Charset encoder = StandardCharsets.UTF_8;

    private static final char[] enAlphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.,?!:;'\"‘’()[]–/".toCharArray();
    private static final char[] uaAlphabet = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюяАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ1234567890.,?!:;'\"‘’()[]–/".toCharArray();

    public static char[] getEnAlphabet(){
        return enAlphabet;
    }

    public static char[] getUaAlphabet(){
        return uaAlphabet;
    }


}
