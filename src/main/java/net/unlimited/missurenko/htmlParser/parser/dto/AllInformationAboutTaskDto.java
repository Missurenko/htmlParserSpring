package net.unlimited.missurenko.htmlParser.parser.dto;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class AllInformationAboutTaskDto {

    private int prioritisation;
    private List<String> keyWords;
    private String nameFolderTask;

    private List<ElementFilteredDto> nameFileAndFilteredElement;


    private Map<String, File> filesForCopy;
    private final List<String> TAG_FILTER = Arrays.asList("script", "noscript", "style");


    public List<ElementFilteredDto> getNameFileAndFilteredElement() {
        return nameFileAndFilteredElement;
    }

    public void setNameFileAndFilteredElement(List<ElementFilteredDto> nameFileAndFilteredElement) {
        this.nameFileAndFilteredElement = nameFileAndFilteredElement;
    }



    public List<String> getTAG_FILTER() {
        return TAG_FILTER;
    }

    public Map<String, File> getFilesForCopy() {
        return filesForCopy;
    }

    public void setFilesForCopy(Map<String, File> filesForCopy) {
        this.filesForCopy = filesForCopy;
    }

    public int getPrioritisation() {
        return prioritisation;
    }

    public void setPrioritisation(int prioritisation) {
        this.prioritisation = prioritisation;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public String getNameFolderTask() {
        return nameFolderTask;
    }

    public void setNameFolderTask(String nameFolderTask) {
        this.nameFolderTask = nameFolderTask;
    }

    @Override
    public String toString() {
        return "\nAllInformationAboutTaskDto{" +
                "\nkeyWords=" + keyWords +
                "\n, nameFolderTask='" + nameFolderTask + '\'' +
                "\n, filesForCopy=" + filesForCopy +
                "\n}";
    }
}
