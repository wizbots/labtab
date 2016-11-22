package org.wizbots.labtab.activity;

import android.os.Environment;

import java.io.File;

public class ResourceClass {
    public static String getVideoDirectory() {

        return Environment.getExternalStorageDirectory() + "/LabTabVideo";

    }

    public static boolean deleteDirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/LabTabVideo");
        if (!dir.exists()) {
            dir.mkdir();
        }
        String[] children = dir.list();
        if (children.length > 0) {
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
            return true;
        } else {
            return false;
        }

    }
}
