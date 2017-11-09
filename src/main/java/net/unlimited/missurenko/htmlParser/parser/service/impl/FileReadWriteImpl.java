package net.unlimited.missurenko.htmlParser.parser.service.impl;

//import org.apache.commons.io.FileUtils;

import net.unlimited.missurenko.htmlParser.parser.dto.AllInformationForTask;
import net.unlimited.missurenko.htmlParser.parser.service.FileReadWrite;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.*;
import java.util.*;

public class FileReadWriteImpl implements FileReadWrite {

    /**
     * @param parseredOrigin html what beem parsered
     * @param path           full patch where write file
     * @param nameDoc        name origin file
     * @return all ok
     */
    @Override
    public boolean writeToDir(Element parseredOrigin, String path, String nameDoc) {
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

    // no need
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

    /**
     * //@param allFiles map what contain key  CUSTOMER-RNID  value list filePatch
     * //@param keyWord  key CUSTOMER-RNID  value list keyWord
     *
     * @return map what contain list documents by value and   key CUSTOMER-RNID
     */
    @Override
    public List<AllInformationForTask> mapDocFilteredByKeyWord(List<AllInformationForTask> allTask) {
        for (AllInformationForTask task : allTask) {
            List<String> patchs = new ArrayList<>();
            patchs.addAll(task.getAllPatchsFiles().keySet());
            List<File> files = getFilesByPatchs(patchs);
            Map<String,Document> resultByDoc = extractDocFromFile(files, task.getKeyWords());
            task.setDocForParsing(resultByDoc);
        }
        return allTask;
    }

    /**
     * @param filesForOneCustomerTask all files
     * @param keyWord                 key word for one task by key CUSTOMER-RNID
     * @return key patch value doc
     */
    private Map<String, Document> extractDocFromFile(List<File> filesForOneCustomerTask, List<String> keyWord) {
        Map<String, Document> resultByDoc = new HashMap<>();
        for (File file : filesForOneCustomerTask) {
            Document doc = null;
            if (file != null) {
                try {
                    doc = Jsoup.parse(file, "UTF-8");
                    System.out.println("read doc in FileReadWriteImpl");
                } catch (IOException e) {
                    System.out.println("Problem this parser in FileReadWriteImpl");
                    e.printStackTrace();
                }
                if (containKeyWordInDoc(doc, keyWord)) {
                    resultByDoc.put(file.getAbsolutePath(), doc);
                }
            }
        }
        return resultByDoc;
    }

    /**
     * @param allPatch all patchs by  key CUSTOMER-RNID
     * @return list files
     */
    private List<File> getFilesByPatchs(List<String> allPatch) {
        List<File> filesForOneKeyCustomer = new ArrayList<>();
        for (String patch : allPatch) {
            File file = new File(patch);
            filesForOneKeyCustomer.add(file);
        }
        return filesForOneKeyCustomer;
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
        return folder;
    }


    /**
     * @param folder full patch to CFS
     * @return list string what contain all file lines
     */
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

    // no need
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
        return lines;
    }

    // no need
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

    /**
     * @return all lines WebConnector.cfg in temp directory
     */
    @Override
    public List<String> webConnectorConfigByLines() {
        return readConfigByLine("temp");
    }

    /**
     * @param allTask
     * @return patch where stay webConnector
     */
    private String getConfigPatch(List<AllInformationForTask> allTask) {
        String tempPach = "";
        for (AllInformationForTask task : allTask) {
            String customerId = task.getCustomerId();
            for (String patch : task.getAllPatchsFiles().keySet()) {
                System.out.println("TASK NAME " + customerId + " FILE PATCH " + patch);
                tempPach = patch;
            }
        }
        String[] split = tempPach.split("temp");
        tempPach = split[0];
        return tempPach;
    }

    /**
     * @param doc      html doc use jsoup
     * @param keyWords
     * @return if true this is html what need
     */
    private boolean containKeyWordInDoc(Document doc, List<String> keyWords) {
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
