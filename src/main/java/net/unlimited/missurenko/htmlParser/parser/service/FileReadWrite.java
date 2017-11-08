package net.unlimited.missurenko.htmlParser.parser.service;

import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationForTask;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface FileReadWrite {


    /**
     * @param parseredOrigin html what beem parsered
     * @param path full patch where write file
     * @param nameDoc name origin file
     * @return all ok
     */
    boolean writeToDir(Element parseredOrigin, String path, String nameDoc);

    /**
     * @param allFiles map what contain key  CUSTOMER-RNID  value list filePatch
     * @param keyWord  // key CUSTOMER-RNID  value list keyWord
     * @return map what contain  key CUSTOMER-RNID and   list documents by value
     */
    Map<String, List<Document>> mapDocFilteredByKeyWord(Map<String, List<String>> allFiles, Map<String, List<String>> keyWord);

    /**
     * @param folder full patch to CFS
     * @return list string what contain all file lines
     */
    List<String> readConfigByLine(String folder);
   /**
     *
     * @return all lines WebConnector.cfg in temp directory
     * @param allTask here need take main folder for know absolut TEMP directory
     */
    List<String> webConnectorConfigByLines(List<AllInformationForTask> allTask);


    String readConfigurationTxt(String folder, String pathName);

    void writeToDir(String whereWrite);

    boolean writeToDir(Element parseredOrigin, String nameDoc);
}
