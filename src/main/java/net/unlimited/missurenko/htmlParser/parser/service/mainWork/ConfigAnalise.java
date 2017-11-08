package net.unlimited.missurenko.htmlParser.parser.service.mainWork;


import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationForTask;

import java.util.*;

// no need for programm
public class ConfigAnalise {


    /**
     * @param configList list what contain all line cfs file
     * @return map what contain map key customer indification CUSTOMER-RNID and value keyWord for this task
     */
    //TODO fix bag this [] - key &&&
    public List<AllInformationForTask> parceConfigCFG(List<String> configList) {
        System.out.println("Start work condig reader");

        System.out.println("Read config");
        String captionConfig = "License";
        String nameTask = "null";

        // think what change
        Map<String, AllInformationForTask> allTasksInfo = new HashMap<>();
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
                    // WARNING hete bags whis [] then stay if
                    if (!nameTask.equals("")) {
                        AllInformationForTask task = new AllInformationForTask();
                        task.setTaskName(nameTask);
                        allTasksInfo.put("[" + nameTask + "]", task);

                    }
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
            if (splitLine[0].equals("IngestAction0")) {
                allTasksInfo.get(nameTask).setCustomerId(splitLine[2]);
            }

        }
        List<AllInformationForTask> result = new ArrayList<>();
        result.addAll(allTasksInfo.values());
        return result;
    }


}
