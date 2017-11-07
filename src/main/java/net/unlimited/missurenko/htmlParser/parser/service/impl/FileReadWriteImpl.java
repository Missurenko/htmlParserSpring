package net.unlimited.missurenko.htmlParser.parser.service.impl;

//import org.apache.commons.io.FileUtils;

import net.unlimited.missurenko.htmlParser.parser.service.FileReadWrite;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileReadWriteImpl implements FileReadWrite {


    // delete child
    // собрать обратно
    //поток прикрутить
    //и чтоб можно автоматически параметри
    //
    @Override
    public Map<String, File> readDir(Map<String, File> allFiles, String dirPathHtml, List<String> keyWord) throws IOException {
        System.out.println("readDir name: dir" + dirPathHtml);
        File htmlFile = new File(dirPathHtml);
        return listFilesFilteredForFolder(allFiles, htmlFile, keyWord);
    }

    @Override
    public boolean writeToDir(Element parseredOrigin, String path, String nameDirTask, String nameDoc) {


        File pathFile = new File(path);


        if (!pathFile.exists()) {

            boolean craete = new File(path).mkdirs();
            System.out.println("create folder" + craete);
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }

//        File dirPath = new File("C:\\Autonomy\\WebConnector\\example\\" +nameDoc);
        File dirPath = new File(path + "/" + nameDoc);
        PrintStream out = null;
        try {
            System.out.println("writeToDir name: dir" + nameDirTask + "/n" +
                    "name doc" + nameDoc);
            out = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(dirPath, true)));
            out.println(parseredOrigin);
        } catch (IOException e) {
            System.out.println("Exeption in writer");
            System.out.println(e.toString());
        } finally {
            if (out != null) {
                out.close();
            }
        }
        System.out.println("Writer : ");

        return true;

    }

    @Override
    public boolean writeToDir(Element parseredOrigin, String nameDoc) {

        File pathFile = new File(nameDoc);
        PrintStream out = null;
        try {
            System.out.println("writeToDir name: dir" + nameDoc);
            out = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(pathFile, false)));
            out.println(parseredOrigin);
        } catch (IOException e) {
            System.out.println("Exeption in writer");
            System.out.println(e.toString());
        } finally {
            if (out != null) {
                out.close();
            }
        }
        System.out.println("Writer : ");

        return true;

    }


    // TODO Change metod give only 1 folder
    private Map<String, File> listFilesFilteredForFolder(Map<String, File> allFiles, File folder, List<String> keyWord) {

        if (folder.listFiles() == null) {
            return allFiles;
        }
        // test this part
        List<File> fileForDelete = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {

            Document doc = null;
            if (fileEntry != null) {
                if (allFiles.containsKey(fileEntry)) {
                    if (!allFiles.get(fileEntry).equals(null)) {
                        allFiles.put(fileEntry.getName(), null);
                    }
                }
                try {
                    doc = Jsoup.parse(fileEntry, "UTF-8");
                    System.out.println("read doc in FileReadWriteImpl");
                } catch (IOException e) {
                    System.out.println("Problem this parser in FileReadWriteImpl");
                    e.printStackTrace();
                }
                for (String key : keyWord) {
                    System.out.println("KeyWord [" + key);
                }
                if (!allFiles.containsKey(fileEntry)) {
                    if (containKeyWordInDoc(doc, keyWord)) {
                        allFiles.put(fileEntry.getName(), fileEntry);
                    } else {
                        fileForDelete.add(fileEntry);
                        System.out.println("fileForDelete add");
                    }
                }
            }
        }
        for (File file : fileForDelete) {
            file.delete();
        }
        return allFiles;
    }

    /**
     * @param folder temp folder by defolt is temp file in webConnector
     * @return Object File what contain cfg webConnector
     */
    private File fileConfigFullPath(File folder) {
        for (final File fileEntry : folder.listFiles()) {
            String[] fileName = fileEntry.getName().split("\\.");
            if (fileName.length > 1) {
                if (fileName[1].equals("cfg")) {
                    return fileEntry;
                }
            }
        }
        System.out.println("Need do txt file configuration webConnector/temp/");

        return folder;

    }

    /**
     * TODO return value is idiots
     *
     * @param file what need delete
     * @return
     */
    @Override
    public boolean delete(File file) {
        file.delete();
        return true;
    }

    @Override
    public List<String> readConfigByLine(String folder) {
        List<String> result = new ArrayList<>();
        File folderFile = new File(folder);
        String fullPath = fileConfigFullPath(folderFile).getAbsolutePath();

        // File file = new File("webconnector.cfg");

        //String str = FileUtils.readFileToString(file, "UTF-8");

        System.out.println("Start read config file");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            System.out.println("Exeption read config FileNotFoundException");
            e.printStackTrace();
        }
        //Construct BufferedReader from InputStreamReader
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(fis, "UTF8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("");
        String line = null;
        try {
            System.out.println("Start read lines");
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split("/");
                if (!splitLine[0].equals("")) {
                    if (!line.equals("")) {
                        result.add(line);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Exeption when read config by line");
            e.printStackTrace();
        }


        return result;
    }


    @Override
    public String readConfigurationTxt(String folder, String txt) {
        String url = "";
        String path = folder.concat("/" + txt);
        String lines = "";
        FileInputStream fis = null;

            try {

                fis = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                System.out.println("Exeption read readConfigurationTxt");
                e.printStackTrace();
            }
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(fis, "UTF8"));
                lines = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File file = new File(path);
//                file.delete();
            return lines;
    }

    @Override
    public void writeToDir(String whereWrite) {
        File pathFile = new File(whereWrite);
        PrintStream out = null;
        try {
            System.out.println("writeToDir name: dir" + whereWrite);
            out = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(pathFile, false)));
            out.println("");
        } catch (IOException e) {
            System.out.println("Exeption in writer");
            System.out.println(e.toString());
        } finally {
            if (out != null) {
                out.close();
            }
        }
        System.out.println("Writer : clean file ");


    }


    public boolean containKeyWordInDoc(Document doc, List<String> keyWords) {
        for (String keyWord : keyWords) {
            if (doc.text().contains(keyWord)) {
                System.out.println("FileReadWriteImpl ");
                System.out.println("Contain keyWord In text" + doc.text().contains(keyWord));
                return true;
            }
        }
        return false;
    }
}
