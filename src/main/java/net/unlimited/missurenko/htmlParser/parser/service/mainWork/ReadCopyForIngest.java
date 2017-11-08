package net.unlimited.missurenko.htmlParser.parser.service.mainWork;


import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationForTask;
import net.unlimited.missurenko.htmlParser.parser.service.FileReadWrite;
import net.unlimited.missurenko.htmlParser.parser.service.impl.FileReadWriteImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.*;


@Component
class ReadCopyForIngest {

    private final List<String> TAG_FILTER = Arrays.asList("script", "noscript", "style");

    @Autowired
    ReadCopyForIngest() {

    }

    /**
     * @param allTask now contain customer indification CUSTOMER-RNID    ,  map where  oldfilePatchs key
     * @return map key customer indification CUSTOMER-RNID    ,  value new patchs for tasks
     */
    public List<AllInformationForTask> start(List<AllInformationForTask> allTask) {
        FileReadWrite fileReadWrite = new FileReadWriteImpl();
        List<String> configsWebConnector = fileReadWrite.webConnectorConfigByLines(allTask);

        ConfigAnalise configAnalise = new ConfigAnalise();

        List< AllInformationForTask> infoListAboutTask = configAnalise.parceConfigCFG(configsWebConnector);


        // key CUSTOMER-RNID   value list keyWord
        Map<String, List<String>> keyWordsByCumtomerId = keyWordByCustomerId(infoListAboutTask);
        // key CUSTOMER-RNID   value list documents
        Map<String, List<Document>> allDocByCumtomerId = fileReadWrite.mapDocFilteredByKeyWord(allFilesMap, keyWordsByCumtomerId);
        // key CUSTOMER-RNID   value list parsered documents
        Map<String, List<Document>> allParsedDocByCumtomerId = parseredDocuments(allDocByCumtomerId, keyWordsByCumtomerId);


        return resultPatchAndKeyId(allParsedDocByCumtomerId, allFilesMap, fileReadWrite);
    }

    /**
     * @param allDocByCumtomerId // key CUSTOMER-RNID   value list parsered documents
     * @param allFilesMap        map key customer indification CUSTOMER-RNID    ,  value list filePatchs
     * @param fileReadWrite      object for write
     * @return map key CUSTOMER-RNID , value new fullPatch file
     */
    private Map<String, List<String>> resultPatchAndKeyId(Map<String, List<Document>> allDocByCumtomerId, Map<String, List<String>> allFilesMap, FileReadWrite fileReadWrite) {
        for (String key : allDocByCumtomerId.keySet()) {
            List<Document> parseredDocsByCustomerId = allDocByCumtomerId.get(key);
            for (Document doc : parseredDocsByCustomerId) {

//                fileReadWrite.writeToDir(doc,);
            }
        }
        return null;
    }


    /**
     * @param allDocByCumtomerId   // key CUSTOMER-RNID   value list documents
     * @param keyWordsByCumtomerId // key CUSTOMER-RNID   value list keyWord
     * @return // key CUSTOMER-RNID   value list parsered documents
     */
    private Map<String, List<Document>> parseredDocuments(Map<String, List<Document>> allDocByCumtomerId, Map<String, List<String>> keyWordsByCumtomerId) {
        for (String key : allDocByCumtomerId.keySet()) {
            List<Document> docsByCustomerId = allDocByCumtomerId.get(key);
            for (Document doc : docsByCustomerId) {
                Element allElement = doc.getAllElements().first();
                Parser parser = new Parser(allElement, keyWordsByCumtomerId.get(key), TAG_FILTER);
                System.out.println("Parsed element");
                Element parseredOrigin = parser.start();
                if (parseredOrigin == null) {
                    System.out.println("Element == null");
                    allDocByCumtomerId.remove(key);
                }
            }
        }
        return allDocByCumtomerId;
    }

    /**
     * @param infoListAboutTask key CUSTOMER-RNID value info about task in webConnector
     * @return map key CUSTOMER-RNID value key word
     */
    private Map<String, List<String>> keyWordByCustomerId(Map<String, AllInformationForTask> infoListAboutTask) {
        Map<String, List<String>> result = new HashMap<>();
        for (String key : infoListAboutTask.keySet()) {
            AllInformationForTask info = infoListAboutTask.get(key);
            result.put(info.getCustomerId(), info.getKeyWords());
        }
        return result;
    }
}

/**
 * some part code for create new folders by task name
 * //        String fullDir = "";
 * //        String[] splitDir = dir.split("\\\\");
 * //        for (int i = 0; i < splitDir.length - 1; i++) {
 * //            if (fullDir.equals("")) {
 * //                fullDir = splitDir[0];
 * //            } else {
 * //                fullDir = fullDir.concat("/" + splitDir[i]);
 * //            }
 * //        }
 * <p>
 */
