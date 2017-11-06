package net.unlimited.missurenko.htmlParser.parser.dto;

import org.jsoup.nodes.Element;

import java.io.File;

public class ElementFilteredDto {

    private Element filteredElement;

    private File file;

    public Element getFilteredElement() {
        return filteredElement;
    }

    public void setFilteredElement(Element filteredElement) {
        this.filteredElement = filteredElement;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
