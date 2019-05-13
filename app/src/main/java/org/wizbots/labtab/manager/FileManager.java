package org.wizbots.labtab.manager;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import org.wizbots.labtab.LabTabApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {

    public static final String TAG = FileManager.class.getName();
    private static FileManager _instance = null;

    static {
        _instance = new FileManager(LabTabApplication.getInstance());
        LabTabApplication.getInstance().addManager(_instance);
    }

    private FileManager(Context context) {
        super();
    }

    public static FileManager getInstance() {
        return _instance;
    }

    public FilePathAndStatus getFileFromBase64AndSaveInSDCard(String base64, String filename){
        FilePathAndStatus filePathAndStatus = new FilePathAndStatus();
        try {
            byte[] pdfAsBytes = Base64.decode(base64, 0);
            FileOutputStream os;
            os = new FileOutputStream(getRosterPath(filename), false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
            filePathAndStatus.filStatus = true;
            filePathAndStatus.filePath = getRosterPath(filename);
            return filePathAndStatus;
        } catch (IOException e) {
            e.printStackTrace();
            filePathAndStatus.filStatus = false;
            filePathAndStatus.filePath = getRosterPath(filename);
            return filePathAndStatus;
        }
    }

    private String getRosterPath(String filename) {
//        File file = new File(Environment.getExternalStorageDirectory().getPath(), "ParentFolder/Report");
        File file = new File(getLabTabPdfDirectory());

        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + filename + ".pdf");
        return uriSting;
    }

    private   String getLabTabPdfDirectory() {
        return Environment.getExternalStorageDirectory() + "/LabTabPdf";
    }

    public boolean isRosterDownloaded(String fileName) {
        File myFile = new File(getLabTabPdfDirectory() + "/"+fileName+".pdf");

        if(myFile.exists()){
            return true;
        }
        return false;
    }

    public String getFilePath(String fileName) {
        File myFile = new File(getLabTabPdfDirectory() + "/"+fileName);
        return myFile.getAbsolutePath();
    }

    public static class FilePathAndStatus {
        public boolean filStatus;
        public String filePath;
    }
}
