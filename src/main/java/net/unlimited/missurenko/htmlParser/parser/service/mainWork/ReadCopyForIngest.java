package net.unlimited.missurenko.htmlParser.parser.service.mainWork;


import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationForTaskDto;
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
     * @param allTask DTO
     * @return DTO what contain new file patch for change in fetch task
     */
    public List<AllInformationForTaskDto> start(List<AllInformationForTaskDto> allTask) {

        FileReadWrite fileReadWrite = new FileReadWriteImpl();

        // set list documents
        allTask = fileReadWrite.mapDocFilteredByKeyWord(allTask);
//        // write and set new patch for fetch list parsered documents
        allTask = parseredDocuments(allTask);


        return null;
//                resultPatchAndKeyId(allParsedDocByCumtomerId, allFilesMap, fileReadWrite);
    }

    /**
     * @param allDocByCumtomerId
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
     * @param allTask /DTO  AllInformationForTaskDto
     * @return add to DTo map filePatch value parsered documents
     */
    private List<AllInformationForTaskDto> parseredDocuments(List<AllInformationForTaskDto> allTask) {
        List<String> filePatchForRemove = new ArrayList<>();
        for (AllInformationForTaskDto task : allTask) {
            List<Document> docsByCustomerId = task.getDocForParsing();
            for (Document doc : docsByCustomerId) {
                Element allElement = doc.getAllElements().first();
                Parser parser = new Parser(allElement, task.getKeyWords(), TAG_FILTER);
                System.out.println("Parsed element");
                Element parseredOrigin = parser.start();
                if (parseredOrigin == null) {
                    System.out.println("Element == null");
//                    filePatchForRemove.add(key);
                    //TODO how how what no save
                }
            }
        }
        return allTask;
    }

}
