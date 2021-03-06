package org.wizbots.labtab.controller;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.model.LoginRequest;
import org.wizbots.labtab.retrofit.ConnectionUtil;
import org.wizbots.labtab.retrofit.LabTabApiInterface;
import org.wizbots.labtab.retrofit.LabTabResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;

public class LabTabHTTPOperationController {

    private final String TAG = LabTabHTTPOperationController.class.getSimpleName();

    public static LabTabResponse loginUser(LoginRequest loginRequest) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser(loginRequest.getPassword(), loginRequest.getEmail(), LabTabApplication.getInstance().getUserAgent()));
    }

    public static LabTabResponse getProgramsOrLabs() {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.returnPrograms(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),LabTabApplication.getInstance().getUserAgent()));
    }

    public static LabTabResponse getProgramsOrLabsUsingFromAndTo(String from, String to) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.returnPrograms
                (
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getMember_id(),
                        from,
                        to,LabTabApplication.getInstance().getUserAgent()
                )
        );
    }

    public static LabTabResponse getMentorProfile() {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.returnMentor(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),LabTabApplication.getInstance().getUserAgent()));
    }

    public static LabTabResponse getProgramWithListOfStudents(String programId) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.returnProgramWithListOfStudents
                (
                        programId,
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),LabTabApplication.getInstance().getUserAgent())
        );
    }

    public static LabTabResponse getStudentStatsAndProfile(String student_id) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.returnStudentProfileAndStats
                (
                        student_id,
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),LabTabApplication.getInstance().getUserAgent())
        );
    }

    public static LabTabResponse markStudentAbsents(String[] students, String date, String programId, boolean sendNotification, String userAgent) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.markStudentAbsent
                (
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),
                        students,
                        date,
                        programId,
                        sendNotification,userAgent
                ));
    }

    public static LabTabResponse promoteDemoteStudents(String[] students, boolean promoteDemote,String userAgent) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.promoteDemoteStudent
                (
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),
                        students,
                        promoteDemote,
                        userAgent
                ));

    }

    public static LabTabResponse createProject(String category, String sku, String description, String title, String notes,
                                               MultipartBody.Part file, String[] components, String[] creators,String userAgent) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        Call call = labTabApiInterface.createProject
                (
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),
                        category,
                        sku,
                        description,
                        title,
                        notes,
                        file,
                        components,
                        creators,
                        userAgent
                );

        Log.v(LabTabConstants.VIDEO_LOGS_TAG, "Request headers - " + call.request().headers() + " \nRequest url - " + call.request().url().url() + " " + "\nDevice time - " + java.text.DateFormat.getDateTimeInstance().format(new Date()));
        LabTabResponse labTabResponse = ConnectionUtil.execute(call);
        if (labTabResponse == null){
            Log.v(LabTabConstants.VIDEO_LOGS_TAG,"Request Fail to execute");
        }
        try {
            Log.v(LabTabConstants.VIDEO_LOGS_TAG, "Response Code - " + labTabResponse.getResponseCode() + "\nResponse header - " + labTabResponse.getHeaderParams().toString() + "\nResponse Body : " + (labTabResponse.getResponse() != null ? new Gson().toJson(labTabResponse.getResponse()) : "null ") + "\nDevice time - " + java.text.DateFormat.getDateTimeInstance().format(new Date()));
        }catch (Exception ignore){

        }
        return labTabResponse;
    }

    public static LabTabResponse editProject(String projectId, String category, String sku, String description, String title, String notes,
                                             String[] components, String[] creators,String userAgent) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.editProject
                (
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),
                        projectId,
                        category,
                        description,
                        title,
                        notes,
                        components,
                        creators,
                        userAgent
                ));
    }

    public static LabTabResponse addWizchips(List<String> studentId, int count) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return  ConnectionUtil.execute(labTabApiInterface.addWizchips(LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken(), studentId, count,LabTabApplication.getInstance().getUserAgent()));
    }

    public static LabTabResponse withdrawWizchips(List<String> studentId, int count) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.withdrawWizchips(LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken(), studentId, count, LabTabApplication.getInstance().getUserAgent()));
    }

    public static LabTabResponse setWizchips(String studentId, int count) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.setWizchips(LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken(), studentId, count, LabTabApplication.getInstance().getUserAgent()));
    }

    public static LabTabResponse getProjectMetaData() {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.getProjectsMetaData(LabTabApplication.getInstance().getUserAgent()));
    }

    public static LabTabResponse getLocation() {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.location(LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),LabTabApplication.getInstance().getUserAgent()));
    }

    public static LabTabResponse getFilter(Map<String, String> params) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.getFilter(LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken(), params,LabTabApplication.getInstance().getUserAgent()));
    }

    public static LabTabResponse deleteVideo(String programId) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.deleteVideo(programId, LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),LabTabApplication.getInstance().getUserAgent()));
    }

    public static LabTabResponse getRosterDetails(String rosterId) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.getRosterDetails(LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken(), rosterId, LabTabApplication.getInstance().getUserAgent()));
    }
}