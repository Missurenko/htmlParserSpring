package net.unlimited.missurenko.htmlParser.parser.service.mainWork;


import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationForParse;
import net.unlimited.missurenko.htmlParser.parser.dto.ElementFilteredDto;
import net.unlimited.missurenko.htmlParser.parser.service.FileReadWrite;
import net.unlimited.missurenko.htmlParser.parser.service.impl.FileReadWriteImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.File;
import java.io.IOException;
import java.util.*;

// TODO use this for main project
class ReadCopyForIngest {

    private String tempFolder = "temp";

    private List<String> keyWords;

    private final List<String> TAG_FILTER = Arrays.asList("script", "noscript", "style");

    ReadCopyForIngest() {
    }

    /**
     * @param allFilesMap key filePatch file , value indification in actions AUTN_IDENTIFIER
     * @param idCustomer  list customer indification CUSTOMER-RNID
     * @return map what contain new file patch and value indification in actions AUTN_IDENTIFIER
     */
    public Map<String, String> start(Map<String, String> allFilesMap, List<String> idCustomer) {

        FileReadWrite fileReadWrite = new FileReadWriteImpl();

        //
//        fileReadWrite.readDir()
// return list what contain all lines cfg file
        //        fileReadWrite.readConfigByLine()


        ConfigAnalise configAnalise = new ConfigAnalise();
//        configAnalise.parceConfigCFG();
        // key AUTN_IDENTIFIER  value list keyWord
//        Map<String, List<String>> keyWordsByCumtomerId = configAnalise



        System.out.println("Stafrt work ReadCopyForIngest do while");
        String dir = "";

        // read configuration file

        AllInformationForParse oneInfo = null;

        List<AllInformationForParse> allInfo = new ArrayList<>();
// take directory temple and take all file
//            try {
//                info.setFilesForCopy(fileReadWrite.readDir(new HashMap<String, File>(), dirTask, info.getKeyWords()));
//                System.out.println("what contain info." + info.getFilesForCopy());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


        for (AllInformationForParse info : allInfo) {
            List<ElementFilteredDto> filteredElement = new ArrayList<>();

            List<String> fileForDalete = new ArrayList<>();

            int countDoc = 0;
//  info.getFilesForCopy()
            for (File html : info.getNeedDelete()) {

                if (html != null) {
                    Document doc = null;
                    try {

                        doc = Jsoup.parse(html, "UTF-8");
                        System.out.println("ReadCopyForIngest read doc Jsoup.parse");
                        countDoc++;
                        System.out.println("number of doc position =" + countDoc);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Exeption this Jsoup.parse in class ReadCopyForIngest");
                    }

                    Element allElement = doc.getAllElements().first();

                    Parser parser = new Parser(allElement, info.getKeyWords(), TAG_FILTER);
                    System.out.println("Parsed element");
                    Element parseredOrigin = parser.start();

                    if (parseredOrigin == null) {
                        fileForDalete.add(html.getName());
                        System.out.println("Element == null");
                    } else {
                        ElementFilteredDto nameAndFilteredElem = new ElementFilteredDto();

                        //set filtered element
//                        nameAndFilteredElem.setFile(html);
                        nameAndFilteredElem.setFilteredElement(parseredOrigin);
                        filteredElement.add(nameAndFilteredElem);
                    }

                }
            }
            // delete file what no need after parsing
//
//                fileReadWrite.delete(info.getFilesForCopy().get(nameFile));
//


            // TODO WARNING  // what have if have zero element
            info.setNameFileAndFilteredElement(filteredElement);
        }
        String fullDir = "";
        String[] splitDir = dir.split("\\\\");
        for (int i = 0; i < splitDir.length - 1; i++) {
            if (fullDir.equals("")) {
                fullDir = splitDir[0];
            } else {
                fullDir = fullDir.concat("/" + splitDir[i]);
            }
        }

        // write to dir and remove after
//                fileReadWrite.writeToDir(dto.getFilteredElement(), endDir, splitNameTask1[0]
//                        , nameFile);
//                fileReadWrite.delete(dto.getFile());


        return null;
    }

    private Element getElementFromFile(File main) {
        Document doc = null;
        try {
            doc = Jsoup.parse(main, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc.getAllElements().first();
    }
}
