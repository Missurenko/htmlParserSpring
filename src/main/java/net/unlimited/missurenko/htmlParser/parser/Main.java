package net.unlimited.missurenko.htmlParser.parser;

import net.unlimited.missurenko.htmlParser.parser.service.FileReadWrite;
import net.unlimited.missurenko.htmlParser.parser.service.impl.FileReadWriteImpl;
import net.unlimited.missurenko.htmlParser.parser.service.mainWork.ReadTxtAndWriteForIngest;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Start main");
//        ReadConfiguration readConfiguration = new ReadConfiguration();
//        readConfiguration.start();
        FileReadWrite fileReadWrite = new FileReadWriteImpl();
        ReadTxtAndWriteForIngest readTxtAndWriteForIngest = new ReadTxtAndWriteForIngest(fileReadWrite);
    }

}