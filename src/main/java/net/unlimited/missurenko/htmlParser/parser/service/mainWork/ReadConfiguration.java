package net.unlimited.missurenko.htmlParser.parser.service.mainWork;


import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationAboutTaskDto;
import net.unlimited.missurenko.htmlParser.parser.service.FileReadWrite;
import net.unlimited.missurenko.htmlParser.parser.service.impl.FileReadWriteImpl;

import java.io.File;
import java.util.*;

// no need for programm
public class ReadConfiguration {

    public void start() {

        FileReadWrite fileReadWrite = new FileReadWriteImpl();

        Map<String, AllInformationAboutTaskDto> listForFilter = readConfigurationFile(fileReadWrite);
        System.out.println(listForFilter.toString());
        ReadCopyForIngest readAndIngest = new ReadCopyForIngest(fileReadWrite, listForFilter);
    }

    private Map<String, AllInformationAboutTaskDto> readConfigurationFile(FileReadWrite fileReadWrite) {
        System.out.println("Start work condig reader");
        String path = new File(".").getAbsolutePath();
        System.getProperty("user.dir");

        List<String> configList = fileReadWrite.readConfigByLine("temp");
        System.out.println("Read config");
        String captionConfig = "License";
        String nameTask = "null";

        Map<String, AllInformationAboutTaskDto> allTasksInfo = new HashMap<>();
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
                    allTasksInfo.put("[" + nameTask + "]", new AllInformationAboutTaskDto());
                }
            }

            if (configLine.charAt(0) == '[') {
                if (allTasksInfo.containsKey(configLine)) {
                    nameTask = configLine;
                }
            }
            if (config.equals("IngestSharedPath")) {
                allTasksInfo.get(nameTask).setNameFolderTask(value);
            }
            if (config.equals("Depth")) {
                allTasksInfo.get(nameTask).setNameFolderTask(value);
            }
            if (config.equals("PageMustHaveRegex")) {
                String have = value.replaceAll("[.*]", "");
                String[] regexList = have.split(",");
                allTasksInfo.get(nameTask).setKeyWords(new ArrayList<>(Arrays.asList(regexList)));

                for (String key : allTasksInfo.get(nameTask).getKeyWords()) {
                    System.out.println("Key word[" + key+"]");

                }
            }
        }
        return allTasksInfo;
    }


}
