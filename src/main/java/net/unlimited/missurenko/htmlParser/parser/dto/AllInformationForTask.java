package net.unlimited.missurenko.htmlParser.parser.dto;


import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;


public class AllInformationForTask {

    private String serverPort;

    private String CFSPort;

    private List<String> keyWords;
    // old patch document
    private Map<String, Document> docForParsing;

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

    public Map<String, Document> getDocForParsing() {
        return docForParsing;
    }

    public void setDocForParsing(Map<String, Document> docForParsing) {
        this.docForParsing = docForParsing;
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

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getCFSPort() {
        return CFSPort;
    }

    public void setCFSPort(String CFSPort) {
        this.CFSPort = CFSPort;
    }
}
