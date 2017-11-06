package net.unlimited.missurenko.htmlParser.parser.service;

import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileReadWrite {


    // delete child
    // собрать обратно
    //поток прикрутить
    //и чтоб можно автоматически параметри
    //
    Map<String, File> readDir(Map<String, File> allFiles, String dirPathHtml, List<String> keyWord) throws IOException;

    boolean writeToDir(Element parseredOrigin, String path, String dirPathHtml, String nameDoc);

    boolean writeToDir(Element parseredOrigin, String nameDoc);

    boolean delete(File file);




    List<String> readConfigByLine(String folder);

    String readConfigurationTxt(String folder, String pathName);

    void writeToDir(String whereWrite);


    // delete child
    // собрать обратно
    //поток прикрутить
    //и чтоб можно автоматически параметри
    //

}
