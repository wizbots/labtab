package org.wizbots.labtab.util;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wizbots.labtab.model.Project;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LabTabUtil {

    private static final String TAG = LabTabUtil.class.getName();

    public static ArrayList<Project> convertStringToProjects(String string) {
        ArrayList<Project> projects = null;
        Gson gson = new Gson();
        if (!string.equals("")) {
            projects = gson.fromJson(string, new TypeToken<ArrayList<Project>>() {
            }.getType());
        } else {
            projects = null;
        }
        return projects;
    }

    public static String convertProjectsToString(ArrayList<Project> projectArrayList) {
        String projects = "";
        if (!projectArrayList.isEmpty()) {
            projects = toJson(projectArrayList);
        } else {
            projects = "";
        }
        return projects;
    }

    public static Object fromJson(String responseString, Type listType) {
        try {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            return gson.fromJson(responseString, listType);
        } catch (Exception e) {
            Log.e(TAG, "Error In Converting JsonToModel", e);
        }
        return null;
    }

    public static String toJson(Object object) {
        try {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            return gson.toJson(object);
        } catch (Exception e) {
            Log.e(TAG, "Error In Converting ModelToJson", e);
        }
        return null;
    }

}
