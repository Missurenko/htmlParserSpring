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


        return resultPatchAndKeyId(allTask, fileReadWrite);


    }

    /**
     * @param fileReadWrite object for write
     * @return map key CUSTOMER-RNID , value new fullPatch file
     */
    private List<AllInformationForTaskDto> resultPatchAndKeyId(List<AllInformationForTaskDto> allTask, FileReadWrite fileReadWrite) {
        for (AllInformationForTaskDto info : allTask) {
            List<Document> allDoc = info.getDocForParsing();

// full patch
            for (Document doc : allDoc) {

                fileReadWrite.writeToDir(doc,"C:\\HewlettPackardEnterprise\\IDOLServer-11.3.0\\webconnector\\Parsered\\2d8585bd0e25c91ecf3584f3369bdc2e.html" );
            }
        }
        return null;
    }


    /**
     * @param allTask /DTO  AllInformationForTaskDto
     * @return add to DTo map filePatch value parsered documents
     */
    private List<AllInformationForTaskDto> parseredDocuments(List<AllInformationForTaskDto> allTask) {

        for (AllInformationForTaskDto task : allTask) {
            List<Document> docsByCustomerId = task.getDocForParsing();
            List<Element> allParseredDoc = new ArrayList<>();
            for (Document doc : docsByCustomerId) {
                Element allElement = doc.getAllElements().first();
                Parser parser = new Parser(allElement, task.getKeyWords(), TAG_FILTER);
                System.out.println("Parsed element");
                Element parseredOrigin = parser.start();
                if (parseredOrigin == null) {
                    System.out.println("Element == null");
                    allParseredDoc.add(null);
                    //TODO how how what no save
                } else {
                    allParseredDoc.add(parseredOrigin);
                }
            }
        }
        return allTask;
    }

}
