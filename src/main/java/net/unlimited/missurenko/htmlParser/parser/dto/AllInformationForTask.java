package net.unlimited.missurenko.htmlParser.parser.dto;

import sun.plugin.dom.core.Document;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class AllInformationForTask {

    private List<String> keyWords;

    private List<Document> fileForParsing;

    private String taskName;

    private String fileName;

    // oldPatch and newPatch
    private Map<String, String> allPatchsFiles;

    private String customerId;

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public List<Document> getFileForParsing() {
        return fileForParsing;
    }

    public void setFileForParsing(List<Document> fileForParsing) {
        this.fileForParsing = fileForParsing;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> getAllPatchsFiles() {
        return allPatchsFiles;
    }

    public void setAllPatchsFiles(Map<String, String> allPatchsFiles) {
        this.allPatchsFiles = allPatchsFiles;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
