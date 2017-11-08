package net.unlimited.missurenko.htmlParser.parser.service.mainWork;


import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationForTask;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class Spliter {

    private String codeStr;

    private List<AllInformationForTask> infoListAboutTask;

    public Spliter(String codeStr, List<AllInformationForTask> infoListAboutTask) {
        this.codeStr = codeStr;
        this.infoListAboutTask = infoListAboutTask;
    }

    /**
     * can be indexer exeption if no have remove part
     *
     * @return true actions for CFS
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public String splitByPart() throws IOException, ParserConfigurationException, SAXException {

        String urlDecoder = java.net.URLDecoder.decode(codeStr, "UTF-8");
        String splitByNoNeed = splitReturnByNumber(urlDecoder, "&adds=", 1);
        String[] splitResult = splitReturn(splitByNoNeed, "&removedocs=");
        String adds = splitResult[0];
        String removes = null;
        if (splitResult.length > 1) {
            removes = splitResult[1];
            removes = splitReturnByNumber(removes, "</removedocs>", 0);
            removes = splitReturnByNumber(removes, "<removedocs>", 1);

        }
        adds = splitReturnByNumber(adds, "</adds>", 0);
        adds = splitReturnByNumber(adds, "<adds>", 1);
        // take all add from fetch
        List<String> allAddList = getAllNodes(adds, "<add>", "</add>");

        // key patch // value key indification


        Map<String, List<String>> allFileMap = getFilesByCumtomerId(allAddList);
        infoListAboutTask = transferObjectSetIdAndFile(allFileMap, infoListAboutTask);


        ReadCopyForIngest readCopyForIngest = new ReadCopyForIngest();

        // / must return update  List<AllInformationForTask>
        infoListAboutTask = readCopyForIngest.start(infoListAboutTask);


        List<String> allFileList = new ArrayList<>();
//        allFileList.addAll(request.values());
        // create all remove map

        Map<String, String> allRemoveMap = null;
        if (removes != null) {
            // take all remove from fetch
            List<String> allRemoveList = getAllNodes(removes, "<remove>", "</remove>");
            allRemoveMap = doInfoByKeyValue(allFileList, allRemoveList);
        }


        Map<String, String> allAddMap = doInfoByKeyValue(allFileList, allAddList);

        String result = concatAndEncodeString(allAddMap, allRemoveMap);

        String xmlText = "";
        return result;
    }


    private List<AllInformationForTask> transferObjectSetIdAndFile(Map<String, List<String>> transfer, List<AllInformationForTask> infoListAboutTask) {

        for (AllInformationForTask task : infoListAboutTask) {

            Map<String, String> oldAndNewPatch = new HashMap<>();
            for (String patch : transfer.get(task.getCustomerId())) {
                oldAndNewPatch.put(patch, null);
            }
            task.setAllPatchsFiles(oldAndNewPatch);
        }
        return infoListAboutTask;
    }

    /**
     * @param allAddList all add by fetch
     * @return map key  CUSTOMER-RNID values files Patchs what link on CUSTOMER-RNID
     */
    // if have time refactoring whis code what split patchFiles
    private Map<String, List<String>> getFilesByCumtomerId(List<String> allAddList) {

        Map<String, List<String>> result = new HashMap<>();
        for (int i = 0; i < allAddList.size(); i++) {
            String addByFentch = allAddList.get(i);
            // send to read for ingest

            List<String> allCustomerIdListNew = getAllNodes(addByFentch, "<CUSTOMER-RNID>", "</CUSTOMER-RNID>");
            String customerId = allCustomerIdListNew.get(1);
            List<String> allFileList = getAllNodes(allCustomerIdListNew.get(2), "<source filename=\"", "\" lifetime=");
            String filePatch = allFileList.get(1);
            if (!result.containsKey(customerId)) {
                result.put(customerId, Arrays.asList(filePatch));
            } else {
                List<String> addNewValue = result.get(customerId);
                addNewValue.add(filePatch);
                result.put(customerId, addNewValue);
            }

        }

        return result;
    }


    private Map<String, String> changeFilePatchForAdd(Map<String, String> allFilesMap) {


        return allFilesMap;
    }

//    private Map<String,String>

    /**
     * @param allFilesMap contain all patch for File
     * @return changed map where change key (file patch)
     */
    private Map<String, String> setFilePatchToFileMap(Map<String, String> allFilesMap, List<String> listFilePatch) {
        Map<String, String> newFilePatch = new HashMap<>();
        for (String newPAtch : listFilePatch) {
            String nameFileNew = getNameHtmlFile(newPAtch);
            newFilePatch.put(nameFileNew, newPAtch);
        }

        for (String keyPatch : allFilesMap.keySet()) {
            String nameFileKey = getNameHtmlFile(keyPatch);

            if (newFilePatch.containsKey(nameFileKey)) {
                String valueNewPatchToKey = newFilePatch.get(nameFileKey);
                String valueIndification = allFilesMap.get(keyPatch);
                allFilesMap.put(valueNewPatchToKey, valueIndification);
            }

        }
        return allFilesMap;
    }

    private String getNameHtmlFile(String fullPatch) {
        String[] splitBy = fullPatch.split("/");
        return splitBy[splitBy.length - 1];
    }

    /**
     * @param allAddMap    all add
     * @param allRemoveMap all remove
     * @return encoder string what contain concat String
     */
    private String concatAndEncodeString(Map<String, String> allAddMap, Map<String, String> allRemoveMap) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder("&adds=<adds>");
        for (String add : allAddMap.values()) {
            sb.append("<add>");
            sb.append(add);
            sb.append("</add>");
        }
        sb.append("</adds>");
        if (allRemoveMap != null) {
            sb.append("&removedocs=<removedocs>");
            for (String remove : allRemoveMap.values()) {
                sb.append("<remove>");
                sb.append(remove);
                sb.append("</remove>");
            }
            sb.append("</removedocs>");
        }
        String result = sb.toString();
        result = URLEncoder.encode(result, "UTF-8");
        return result;
    }

    /**
     * @param filesPatchList list files patch what return parser
     * @param allFilesMap    all file patch what contain ingest(Main String)
     * @return list key Indefication  what need delete
     */
    private List<String> getKeyByFilePatchForDelete(List<String> filesPatchList, Map<String, String> allFilesMap) {
        List<String> result = new ArrayList<>();
        for (String fimePatch : filesPatchList) {
            allFilesMap.remove(fimePatch);
        }
        result.addAll(allFilesMap.values());
        return result;
    }

    /**
     * remove add and remove what file been remove parser
     *
     * @param allSomething key CUSTOMER-RNID value add,remove
     * @param keys         task indification "CUSTOMER-RNID"
     * @return map what contain part what need for encode
     */
    private Map<String, String> removeByKey(Map<String, String> allSomething, List<String> keys) {
        for (String key : keys) {
            allSomething.remove(key);
        }
        return allSomething;
    }

    /**
     * @param keyIndefication key task indification from xml doc
     * @param value           file or add or remove by  key from xml(from fetch) doc
     * @return put value in map  for indetified later
     */
    private Map<String, String> doInfoByKeyValue(List<String> keyIndefication, List<String> value) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < keyIndefication.size(); i++) {
            result.put(keyIndefication.get(i), value.get(i));
        }
        return result;
    }

    /**
     * @param needSplit
     * @param regex
     * @param position  what need return
     * @return string split and take by potion
     */
    private String splitReturnByNumber(String needSplit, String regex, int position) {
        String[] splitResult = needSplit.split(regex);
        return splitResult[position];
    }

    /**
     * @param needSplit
     * @param regex
     * @return array split by regex
     */
    private String[] splitReturn(String needSplit, String regex) {
        return needSplit.split(regex);
    }

    /**
     * @param needSplit string what contain all nodes
     * @param regex1    what start
     * @param regex2    what end
     * @return metod return list nodes like all add or all remove from String
     */
    private List<String> getAllNodes(String needSplit, String regex1, String regex2) {
        List<String> endResult = new ArrayList<>();
        String[] result = needSplit.split(regex1);
        for (String aResult : result) {
            String[] splitResult = aResult.split(regex2);
            for (String aSplitResult : splitResult) {
                if (!aSplitResult.equals("")) {
                    endResult.add(aSplitResult);
                }
            }
        }
        return endResult;
    }

    /**
     * @param nodes contain main string file or add
     * @return list what no contain part what need cut
     */
    private List<String> removeFirstAndEnd(List<String> nodes) {
        nodes.remove(0);
        nodes.remove(nodes.size() - 1);
        return nodes;
    }

}
