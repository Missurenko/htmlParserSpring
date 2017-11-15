package net.unlimited.missurenko.htmlParser.parser.service.mainWork;


import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationForTaskDto;
import net.unlimited.missurenko.htmlParser.parser.service.FileReadWrite;
import net.unlimited.missurenko.htmlParser.parser.service.impl.FileReadWriteImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.File;
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


        return resultPatchAndKeyIdWriteToTmpFile(allTask, fileReadWrite);
        // create new patch
//                resultPatchAndKeyId(allTask, fileReadWrite);


    }

    /**
     * @param fileReadWrite object for write
     * @return map key CUSTOMER-RNID , value new fullPatch file
     */
    private List<AllInformationForTaskDto> resultPatchAndKeyId(List<AllInformationForTaskDto> allTask, FileReadWrite fileReadWrite) {
        for (AllInformationForTaskDto info : allTask) {
            Map<String, Document> allDoc = info.getDocForParsing();
            Map<String, String> oldAndNewPatch = new HashMap<>();
            for (String keyPatch : allDoc.keySet()) {
                Document doc = allDoc.get(keyPatch);
                // get new patch

                String newPatch = null;
                if (newPatch == null) {
                    newPatch = splitAndTakeNewPatch(keyPatch);
                }

                String[] split = keyPatch.split("\\\\");
                String name = split[split.length - 1];
                newPatch = newPatch.concat("//" + name);
                //write file
                fileReadWrite.writeToDir(doc, newPatch);
                oldAndNewPatch.put(keyPatch, newPatch);

            }
            info.setAllPatchsFiles(oldAndNewPatch);
        }
        return allTask;
    }

    /**
     * write to temp file
     *
     * @param allTask
     * @param fileReadWrite
     * @return
     */
    private List<AllInformationForTaskDto> resultPatchAndKeyIdWriteToTmpFile(List<AllInformationForTaskDto> allTask, FileReadWrite fileReadWrite) {
        for (AllInformationForTaskDto info : allTask) {
            Map<String, Document> allDoc = info.getDocForParsing();

            for (String keyPatch : allDoc.keySet()) {
                Document doc = allDoc.get(keyPatch);
                File file = new File(keyPatch);

                // TODO WARNING change after
                file.delete();

                fileReadWrite.writeToDir(doc, keyPatch);
            }

        }
        return allTask;
    }

    /**
     * @param keyPatch full natch for reading
     * @return directory for writing
     */
    private String splitAndTakeNewPatch(String keyPatch) {

        String[] split = keyPatch.split("\\\\");
        List<String> needPart = new ArrayList<>();
        for (int i = 0; i < split.length - 2; i++) {
            needPart.add(split[i]);
        }
        needPart.add("parsered");

        return String.join("//", needPart);
    }


    /**
     * @param allTask /DTO  AllInformationForTaskDto
     * @return add to DTo map filePatch value parsered documents
     */
    private List<AllInformationForTaskDto> parseredDocuments(List<AllInformationForTaskDto> allTask) {

        for (AllInformationForTaskDto task : allTask) {
            Map<String, Document> docsByCustomerId = task.getDocForParsing();
            Map<String, Element> allParseredDoc = new HashMap<>();
            for (String keyPatch : docsByCustomerId.keySet()) {

                Document doc = docsByCustomerId.get(keyPatch);
                Element allElement = doc.getAllElements().first();
                Parser parser = new Parser(allElement, task.getKeyWords(), TAG_FILTER);
                System.out.println("Parsed element");
                Element parseredOrigin = parser.start();
                if (parseredOrigin == null) {
                    System.out.println("Element == null");
                    allParseredDoc.put(keyPatch, null);
                    //TODO how how what no save
                } else {
                    allParseredDoc.put(keyPatch, parseredOrigin);
                }
            }
        }

        return allTask;
    }

}
