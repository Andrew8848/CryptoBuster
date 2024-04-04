package org.cryptobuster.cryptography;

import lombok.Getter;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AlphabetHandler extends DefaultHandler {


    public static final String EN = "en";
    public static final String UA = "ua";
    private static final String PATH = "src/main/resources/Caesar/alphabets.xml";

    private String language;

    private List<Character> data;

    @Getter
    private List<Character> alphabet;


    public AlphabetHandler(String language) {
        this.language = language;
        getAlphabetFromXml();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.data = new String(ch, start, length).chars().mapToObj(element -> (char) element).toList();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(this.language.equals(qName)) this.alphabet = this.data;
    }

    private void getAlphabetFromXml() {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        AlphabetHandler handler;
        try {
            SAXParser parser = parserFactory.newSAXParser();
            handler = this;
            parser.parse(new File(PATH), handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
