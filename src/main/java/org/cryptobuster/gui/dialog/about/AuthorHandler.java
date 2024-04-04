package org.cryptobuster.gui.dialog.about;

import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

@Getter
public class AuthorHandler extends DefaultHandler {

    public static String ATTRIBUTE_NAME = "attributeName";

    private final Author author;
    private StringBuilder data;

    public AuthorHandler() {
        this.author = new Author();
        this.author.setStudent(new Student());
        this.author.setGroup(new Group());
        this.author.setSubject(new Subject());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        switch (qName) {
            case "student" -> this.author.getStudent().setAttributeName(attributes.getValue(ATTRIBUTE_NAME));
            case "group" -> this.author.getGroup().setAttributeName(attributes.getValue(ATTRIBUTE_NAME));
            case "subject" -> this.author.getSubject().setAttributeName(attributes.getValue(ATTRIBUTE_NAME));
        }
        this.data = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        this.data.append(new String(ch, start, length));
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "name" -> this.author.getStudent().setName(this.data.toString());
            case "lastname" -> this.author.getStudent().setLastname(this.data.toString());
            case "parentname" -> this.author.getStudent().setParentname(this.data.toString());
            case "group" -> this.author.getGroup().setElementName(this.data.toString());
            case "subject" -> this.author.getSubject().setElementName(this.data.toString());
        }
    }
}
