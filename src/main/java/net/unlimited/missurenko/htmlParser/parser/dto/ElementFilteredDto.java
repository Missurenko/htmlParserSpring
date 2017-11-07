package net.unlimited.missurenko.htmlParser.parser.dto;

import org.jsoup.nodes.Element;

import java.io.File;

// think why need

public class ElementFilteredDto {

    private Element filteredElement;

    private String olderFilePatch;

    private String newFilePatch;

    public Element getFilteredElement() {
        return filteredElement;
    }

    public void setFilteredElement(Element filteredElement) {
        this.filteredElement = filteredElement;
    }

    public String getOlderFilePatch() {
        return olderFilePatch;
    }

    public void setOlderFilePatch(String olderFilePatch) {
        this.olderFilePatch = olderFilePatch;
    }

    public String getNewFilePatch() {
        return newFilePatch;
    }

    public void setNewFilePatch(String newFilePatch) {
        this.newFilePatch = newFilePatch;
    }
}
