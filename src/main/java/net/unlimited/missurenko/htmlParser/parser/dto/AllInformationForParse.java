package net.unlimited.missurenko.htmlParser.parser.dto;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class AllInformationForParse {

    private List<String> keyWords;

    private List<ElementFilteredDto> nameFileAndFilteredElement;

    private List<File> needDelete;

    public List<ElementFilteredDto> getNameFileAndFilteredElement() {
        return nameFileAndFilteredElement;
    }

    public void setNameFileAndFilteredElement(List<ElementFilteredDto> nameFileAndFilteredElement) {
        this.nameFileAndFilteredElement = nameFileAndFilteredElement;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public List<File> getNeedDelete() {
        return needDelete;
    }

    public void setNeedDelete(List<File> needDelete) {
        this.needDelete = needDelete;
    }
}
