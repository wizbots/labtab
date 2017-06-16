package org.wizbots.labtab.util;


import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.enums.LabLevel;
import org.wizbots.labtab.enums.LabSteps;
import org.wizbots.labtab.enums.ProjectStatus;
import org.wizbots.labtab.model.Project;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.student.response.ProjectResponse;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.retrofit.LabTabApiInterface;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LabTabUtil implements LabTabConstants {

    private static final String TAG = LabTabUtil.class.getName();
    public static Video videoRequestOnOpeningEditScreen;

    public static ArrayList<ProjectResponse> convertStringToProjects(String string) {
        ArrayList<ProjectResponse> projects = null;
        Gson gson = new Gson();
        if (!string.equals("")) {
            projects = gson.fromJson(string, new TypeToken<ArrayList<ProjectResponse>>() {
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
        switch (generateRandomInteger(0, 8)) {
            case 1:
                labLevel = LabLevels.APPRENTICE;
                break;
            case 2:
                labLevel = LabLevels.EXPLORER;
                break;
            case 3:
                labLevel = LabLevels.IMAGINEER;
                break;
            case 4:
                labLevel = LabLevels.LAB_CERTIFIED;
                break;
            case 5:
                labLevel = LabLevels.MAKER;
                break;
            case 6:
                labLevel = LabLevels.MASTER;
                break;
            case 7:
                labLevel = LabLevels.WIZARD;
                break;
            case 8:
                labLevel = LabLevels.NOVICE;
                break;
            default:
                labLevel = LabLevels.APPRENTICE;
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
        switch (labLevel.toUpperCase()) {
            case LabLevels.APPRENTICE:
                imageView.setImageResource(LabLevel.APPRENTICE.getLabLevel());
                break;
            case LabLevels.EXPLORER:
                imageView.setImageResource(LabLevel.EXPLORER.getLabLevel());
                break;
            case LabLevels.IMAGINEER:
                imageView.setImageResource(LabLevel.IMAGINEER.getLabLevel());
                break;
            case LabLevels.LAB_CERTIFIED:
                imageView.setImageResource(LabLevel.LAB_CERTIFIED.getLabLevel());
                break;
            case LabLevels.MAKER:
                imageView.setImageResource(LabLevel.MAKER.getLabLevel());
                break;
            case LabLevels.MASTER:
                imageView.setImageResource(LabLevel.MASTER.getLabLevel());
                break;
            case LabLevels.WIZARD:
                imageView.setImageResource(LabLevel.WIZARD.getLabLevel());
                break;
            case LabLevels.NOVICE:
                imageView.setImageResource(LabLevel.NOVICE.getLabLevel());
                break;
            default:
                imageView.setImageResource(LabLevel.APPRENTICE.getLabLevel());
                break;
        }
    }

    public static void setProjectStatusImageResource(String projectStatus, ImageView imageView) {
        switch (projectStatus.toUpperCase()) {
            case Marks.NONE:
                break;
            case Marks.DONE:
                imageView.setImageResource(ProjectStatus.DONE.getProjectStatus());
                break;
            case Marks.SKIPPED:
                imageView.setImageResource(ProjectStatus.SKIPPED.getProjectStatus());
                break;
            case Marks.PENDING:
                imageView.setImageResource(ProjectStatus.PENDING.getProjectStatus());
                break;
            case Marks.IMAGINEERING:
                imageView.setImageResource(ProjectStatus.IMAGINEERING.getProjectStatus());
                break;
            case Marks.PROGRAMMING:
                imageView.setImageResource(ProjectStatus.PROGRAMMING.getProjectStatus());
                break;
            case Marks.MECHANISMS:
                imageView.setImageResource(ProjectStatus.MECHANISMS.getProjectStatus());
                break;
            case Marks.STRUCTURES:
                imageView.setImageResource(ProjectStatus.STRUCTURES.getProjectStatus());
                break;
            case Marks.CLOSE_TO_NEXT_LEVEl:
                imageView.setImageResource(ProjectStatus.CLOSE_NEXT_LEVEL.getProjectStatus());
                break;
            default:
                imageView.setImageResource(ProjectStatus.DONE.getProjectStatus());
                break;

        }
    }

    public static void setLabStepImageResource(String labStep, ImageView imageView) {
        switch (labStep) {
            case Steps.LAB_STEP_1:
                imageView.setImageResource(LabSteps.LAB_STEP1.getLabStep());
                break;
            case Steps.LAB_STEP_2:
                imageView.setImageResource(LabSteps.LAB_STEP2.getLabStep());
                break;
            case Steps.LAB_STEP_3:
                imageView.setImageResource(LabSteps.LAB_STEP3.getLabStep());
                break;
            case Steps.LAB_STEP_4:
                imageView.setImageResource(LabSteps.LAB_STEP4.getLabStep());
                break;
            default:
                imageView.setImageResource(LabSteps.LAB_STEP1.getLabStep());
                break;
        }
    }

    public static boolean isValidEmail(CharSequence email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static LabTabApiInterface getApiInterface() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LabTabApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(LabTabApiInterface.class);
    }

    public static void setServiceEnabled(final Class<? extends Service> serviceClass, final boolean enable) {
        final PackageManager pm = LabTabApplication.getInstance().getPackageManager();
        final int enableFlag = enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        pm.setComponentEnabledSetting(new ComponentName(LabTabApplication.getInstance(), serviceClass), enableFlag, PackageManager.DONT_KILL_APP);
    }

    public static String getFormattedDate(String dateFormat, Date date) {
        String formattedDate = "";
        switch (dateFormat) {
            case DateFormat.DEFAULT:
                SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormat.DEFAULT, Locale.getDefault());
                formattedDate = sdf1.format(date);
                break;
            case DateFormat.YYYYMMDD:
                SimpleDateFormat sdf2 = new SimpleDateFormat(DateFormat.YYYYMMDD, Locale.getDefault());
                formattedDate = sdf2.format(date);
                break;
            default:
                SimpleDateFormat sdfDefault = new SimpleDateFormat(DateFormat.DEFAULT, Locale.getDefault());
                formattedDate = sdfDefault.format(date);
                break;
        }
        return formattedDate;
    }

    public static void setBackGroundImageResource(String labLevel, ImageView imageView) {
        switch (labLevel.toUpperCase()) {
            case LabLevels.APPRENTICE:
                imageView.setBackgroundResource(R.drawable.ic_filter_apprentice);
                break;
            case LabLevels.EXPLORER:
                imageView.setBackgroundResource(R.drawable.ic_filter_explorer);
                break;
            case LabLevels.IMAGINEER:
                imageView.setBackgroundResource(R.drawable.ic_filter_imagineer);
                break;
            case LabLevels.LAB_CERTIFIED:
                imageView.setBackgroundResource(R.drawable.ic_filter_lab_certified);
                break;
            case LabLevels.MAKER:
                imageView.setBackgroundResource(R.drawable.ic_filter_maker);
                break;
            case LabLevels.MASTER:
                imageView.setBackgroundResource(R.drawable.ic_filter_master);
                break;
            case LabLevels.WIZARD:
                imageView.setBackgroundResource(R.drawable.ic_filter_wizard);
                break;
            case LabLevels.NOVICE:
                imageView.setBackgroundResource(R.drawable.ic_filter_novice);
                break;
            default:
                imageView.setBackgroundResource(R.drawable.ic_filter_apprentice);
                break;
        }
    }

    public static String getPromotionDemotionLevel(String level, boolean promote) {
        if (promote) {
            switch (level.toUpperCase()) {
                case LabLevels.NOVICE:
                    level = LabLevels.LAB_CERTIFIED;
                    break;
                case LabLevels.LAB_CERTIFIED:
                    level = LabLevels.EXPLORER;
                    break;
                case LabLevels.EXPLORER:
                    level = LabLevels.APPRENTICE;
                    break;
                case LabLevels.APPRENTICE:
                    level = LabLevels.MAKER;
                    break;
                case LabLevels.MAKER:
                    level = LabLevels.IMAGINEER;
                    break;
                case LabLevels.IMAGINEER:
                    level = LabLevels.WIZARD;
                    break;
                case LabLevels.WIZARD:
                    level = LabLevels.MASTER;
                    break;
                case LabLevels.MASTER:
                    level = LabLevels.MASTER;
                    break;
                default:
                    level = LabLevels.NOVICE;
                    break;
            }
        } else {
            switch (level.toUpperCase()) {
                case LabLevels.NOVICE:
                    level = LabLevels.NOVICE;
                    break;
                case LabLevels.LAB_CERTIFIED:
                    level = LabLevels.NOVICE;
                    break;
                case LabLevels.EXPLORER:
                    level = LabLevels.LAB_CERTIFIED;
                    break;
                case LabLevels.APPRENTICE:
                    level = LabLevels.EXPLORER;
                    break;
                case LabLevels.MAKER:
                    level = LabLevels.APPRENTICE;
                    break;
                case LabLevels.IMAGINEER:
                    level = LabLevels.MAKER;
                    break;
                case LabLevels.WIZARD:
                    level = LabLevels.IMAGINEER;
                    break;
                case LabLevels.MASTER:
                    level = LabLevels.WIZARD;
                    break;
                default:
                    level = LabLevels.NOVICE;
                    break;
            }
        }
        return level;
    }

    public static ArrayList<String> convertStringToKnowledgeNuggets(String string) {
        ArrayList<String> knowledgeNuggets = null;
        Gson gson = new Gson();
        if (!string.equals("")) {
            knowledgeNuggets = gson.fromJson(string, new TypeToken<ArrayList<String>>() {
            }.getType());
        } else {
            knowledgeNuggets = new ArrayList<>();
        }
        return knowledgeNuggets;
    }

    public static ArrayList<Student> convertStringToProjectCreators(String string) {
        ArrayList<Student> studentArrayList = null;
        Gson gson = new Gson();
        if (!string.equals("")) {
            studentArrayList = gson.fromJson(string, new TypeToken<ArrayList<Student>>() {
            }.getType());
        } else {
            studentArrayList = new ArrayList<>();
        }
        return studentArrayList;
    }

    public static boolean isValidDateSelection(Date selectedDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.YYYYMMDD);
        boolean isValidDateSelection = false;
        try {
            Date currentDate = new Date();
            Date dateSelected = sdf.parse(getFormattedDate(DateFormat.YYYYMMDD, selectedDate));

            if (currentDate.compareTo(dateSelected) > 0) {
                isValidDateSelection = true;
            } else if (currentDate.compareTo(dateSelected) < 0) {
                isValidDateSelection = false;
            } else if (currentDate.compareTo(dateSelected) == 0) {
                isValidDateSelection = true;
            } else {
                isValidDateSelection = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isValidDateSelection;
    }

    public static long getTimeStamp(String dateInString) {
        long timeStamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.MMDDYY);
        try {
            timeStamp = sdf.parse(dateInString).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public static long getTimeStamp(String dateInString, String dateFormate) {
        long timeStamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormate);
        try {
            timeStamp = sdf.parse(dateInString).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    public static boolean compareEditedVideo(Video videoToBeCompared) {
        boolean change = false;

        if (!videoToBeCompared.getTitle().equals(videoRequestOnOpeningEditScreen.getTitle())) {
            change = true;
        }
        if (!videoToBeCompared.getCategory().equals(videoRequestOnOpeningEditScreen.getCategory())) {
            change = true;
        }
        if (!videoToBeCompared.getCategory().equals(videoRequestOnOpeningEditScreen.getCategory())) {
            change = true;
        }
        if (!videoToBeCompared.getKnowledge_nuggets().equals(videoRequestOnOpeningEditScreen.getKnowledge_nuggets())) {
            change = true;
        }
        if (!videoToBeCompared.getDescription().equals(videoRequestOnOpeningEditScreen.getDescription())) {
            change = true;
        }

        if (!videoToBeCompared.getNotes_to_the_family().equals(videoRequestOnOpeningEditScreen.getNotes_to_the_family())) {
            change = true;
        }

        if (!videoToBeCompared.getProject_creators().equals(videoRequestOnOpeningEditScreen.getProject_creators())) {
            change = true;
        }
        return change;
    }

    //This method return todays and tomorrow date. if set to true return tomorror otherwise todays.
    public static String getTodayDate(boolean isTomorrow) {
        Calendar cal = Calendar.getInstance();
        if (isTomorrow)
            cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());
        return formatted;
    }

    public static int getCurrentMonth() {
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getCurrentYear() {
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static String getSeason() {
        String season = null;
        switch (getCurrentMonth()) {
            case 1:
            case 2:
            case 12:
                season = "Summer";
                break;
            case 3:
            case 4:
            case 5:
                season = "Fall";
                break;
            case 6:
            case 7:
            case 8:
                season = "Winter";
                break;
            case 9:
            case 10:
            case 11:
                season = "Spring";
                break;
            default:
                break;
        }
        return season;
    }
}
