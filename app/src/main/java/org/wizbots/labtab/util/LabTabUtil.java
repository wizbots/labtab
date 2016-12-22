package org.wizbots.labtab.util;


import android.app.Activity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.enums.LabLevel;
import org.wizbots.labtab.enums.LabSteps;
import org.wizbots.labtab.enums.ProjectStatus;
import org.wizbots.labtab.model.Project;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;

public class LabTabUtil implements LabTabConstants {

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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String getRandomLabLevel() {
        String labLevel = "";
        switch (generateRandomInteger(0, 7)) {
            case 1:
                labLevel = LAB_LEVEL_APPRENTICE;
                break;
            case 2:
                labLevel = LAB_LEVEL_EXPLORER;
                break;
            case 3:
                labLevel = LAB_LEVEL_IMAGINEER;
                break;
            case 4:
                labLevel = LAB_LEVEL_LAB_CERTIFIED;
                break;
            case 5:
                labLevel = LAB_LEVEL_MAKER;
                break;
            case 6:
                labLevel = LAB_LEVEL_MASTER;
                break;
            case 7:
                labLevel = LAB_LEVEL_WIZARD;
                break;
            default:
                labLevel = LAB_LEVEL_APPRENTICE;
                break;
        }
        return labLevel;
    }

    public static int generateRandomInteger(int min, int max) {
        SecureRandom rand = new SecureRandom();
        rand.setSeed(new Date().getTime());
        return rand.nextInt((max - min) + 1) + min;
    }

    public static void setLabLevelImageResource(String labLevel, ImageView imageView) {
        switch (labLevel) {
            case LAB_LEVEL_APPRENTICE:
                imageView.setImageResource(LabLevel.APPRENTICE.getLabLevel());
                break;
            case LAB_LEVEL_EXPLORER:
                imageView.setImageResource(LabLevel.EXPLORER.getLabLevel());
                break;
            case LAB_LEVEL_IMAGINEER:
                imageView.setImageResource(LabLevel.IMAGINEER.getLabLevel());
                break;
            case LAB_LEVEL_LAB_CERTIFIED:
                imageView.setImageResource(LabLevel.LAB_CERTIFIED.getLabLevel());
                break;
            case LAB_LEVEL_MAKER:
                imageView.setImageResource(LabLevel.MAKER.getLabLevel());
                break;
            case LAB_LEVEL_MASTER:
                imageView.setImageResource(LabLevel.MASTER.getLabLevel());
                break;
            case LAB_LEVEL_WIZARD:
                imageView.setImageResource(LabLevel.WIZARD.getLabLevel());
                break;
        }
    }

    public static void setProjectStatusImageResource(String projectStatus, ImageView imageView) {
        switch (projectStatus) {
            case MARKS_NONE:
                break;
            case MARKS_DONE:
                imageView.setImageResource(ProjectStatus.DONE.getProjectStatus());
                break;
            case MARKS_SKIPPED:
                imageView.setImageResource(ProjectStatus.SKIPPED.getProjectStatus());
                break;
            case MARKS_PENDING:
                imageView.setImageResource(ProjectStatus.PENDING.getProjectStatus());
                break;
            case MARKS_IMAGINEERING:
                imageView.setImageResource(ProjectStatus.IMAGINEERING.getProjectStatus());
                break;
            case MARKS_PROGRAMMING:
                imageView.setImageResource(ProjectStatus.PENDING.getProjectStatus());
                break;
            case MARKS_MECHANISMS:
                imageView.setImageResource(ProjectStatus.MECHANISMS.getProjectStatus());
                break;
            case MARKS_STRUCTURES:
                imageView.setImageResource(ProjectStatus.STRUCTURES.getProjectStatus());
                break;
            case MARKS_CLOSE_TO_NEXT_LEVEl:
                imageView.setImageResource(ProjectStatus.CLOSE_NEXT_LEVEL.getProjectStatus());
                break;

        }
    }

    public static void setLabStepImageResource(String labStep, ImageView imageView) {
        switch (labStep) {
            case LAB_STEP_1:
                imageView.setImageResource(LabSteps.LAB_STEP1.getLabStep());
                break;
            case LAB_STEP_2:
                imageView.setImageResource(LabSteps.LAB_STEP2.getLabStep());
                break;
            case LAB_STEP_3:
                imageView.setImageResource(LabSteps.LAB_STEP3.getLabStep());
                break;
            case LAB_STEP_4:
                imageView.setImageResource(LabSteps.LAB_STEP4.getLabStep());
                break;
        }
    }

}
