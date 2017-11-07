package net.unlimited.missurenko.htmlParser.parser.service.mainWork;


import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationForParse;
import net.unlimited.missurenko.htmlParser.parser.service.FileReadWrite;
import net.unlimited.missurenko.htmlParser.parser.service.impl.FileReadWriteImpl;

import java.io.File;
import java.util.*;

// no need for programm
public class ConfigAnalise {


    /**
     * @param configList list what contain all line cfs file
     * @param idCustomer list customer indification CUSTOMER-RNID
     * @return map what contain map key customer indification CUSTOMER-RNID and value keyWord for this task
     */
    //TODO this

    public Map<String, AllInformationForParse> parceConfigCFG(List<String> configList, List<String> idCustomer) {
        System.out.println("Start work condig reader");

        System.out.println("Read config");
        String captionConfig = "License";
        String nameTask = "null";

        // think what change
        Map<String, AllInformationForParse> allTasksInfo = new HashMap<>();
        for (String configLine : configList) {
            if (configLine.equals("[FetchTasks]")) {
                captionConfig = configLine;
            }
            String[] splitLine = configLine.split("=");
            String config = "";
            String value = "";
            if (splitLine.length == 2) {
                config = splitLine[0];
                value = splitLine[1];
            } else if (splitLine.length == 1) {
                config = splitLine[0];
            }
            if (captionConfig.equals("[FetchTasks]")) {
                if (!config.matches("^\\D*$") &
                        configLine.charAt(0) != '[') {
                    nameTask = value;
                    allTasksInfo.put("[" + nameTask + "]", new AllInformationForParse());
                }
            }
            if (configLine.charAt(0) == '[') {
                if (allTasksInfo.containsKey(configLine)) {
                    nameTask = configLine;
                }
            }
            if (config.equals("PageMustHaveRegex")) {
                String have = value.replaceAll("[.*]", "");
                String[] regexList = have.split(",");
                allTasksInfo.get(nameTask).setKeyWords(new ArrayList<>(Arrays.asList(regexList)));

                for (String key : allTasksInfo.get(nameTask).getKeyWords()) {
                    System.out.println("Key word[" + key + "]");

                }
            }
        }
        return allTasksInfo;
    }


}
