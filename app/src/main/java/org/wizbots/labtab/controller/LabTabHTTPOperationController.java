package org.wizbots.labtab.controller;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.model.LoginRequest;
import org.wizbots.labtab.retrofit.ConnectionUtil;
import org.wizbots.labtab.retrofit.LabTabApiInterface;
import org.wizbots.labtab.retrofit.LabTabResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

public class LabTabHTTPOperationController {

    private final String TAG = LabTabHTTPOperationController.class.getSimpleName();

    public static LabTabResponse loginUser(LoginRequest loginRequest) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser(loginRequest.getPassword(), loginRequest.getEmail()));
    }

    public static LabTabResponse getProgramsOrLabs() {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.returnPrograms(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken()));
    }

    public static LabTabResponse getProgramsOrLabsUsingFromAndTo(String from, String to) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.returnPrograms
                (
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getMember_id(),
                        from,
                        to
                )
        );
    }

    public static LabTabResponse getMentorProfile() {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.returnMentor(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken()));
    }

    public static LabTabResponse getProgramWithListOfStudents(String programId) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.returnProgramWithListOfStudents
                (
                        programId,
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken())
        );
    }

    public static LabTabResponse getStudentStatsAndProfile(String student_id) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.returnStudentProfileAndStats
                (
                        student_id,
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken())
        );
    }

    public static LabTabResponse markStudentAbsents(String[] students, String date, String programId, boolean sendNotification) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.markStudentAbsent
                (
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),
                        students,
                        date,
                        programId,
                        sendNotification
                ));
    }

    public static LabTabResponse promoteDemoteStudents(String[] students, boolean promoteDemote) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.promoteDemoteStudent
                (
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),
                        students,
                        promoteDemote
                ));

    }

    public static LabTabResponse createProject(String category, String sku, String description, String title, String notes,
                                               MultipartBody.Part file, String[] components, String creators) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.createProject
                (
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getCreateTokenResponse().getToken(),
                        category,
                        sku,
                        description,
                        title,
                        notes,
                        file,
                        components,
                        creators
                ));
    }

    public static LabTabResponse editProject(String projectId, String category, String sku, String description, String title, String notes,
                                             String[] components, String creators) {
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
                        creators
                ));
    }

    public static LabTabResponse addWizchips(List<String> studentId, int count) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.addWizchips(LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken(), studentId, count));
    }

    public static LabTabResponse withdrawWizchips(List<String> studentId, int count) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.withdrawWizchips(LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken(), studentId, count));
    }

    public static LabTabResponse getProjectMetaData() {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.getProjectsMetaData());
    }

    public static LabTabResponse getLocation() {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.location(LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken()));
    }

    public static LabTabResponse getFilter(Map<String, String> params) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.getFilter(LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken(), params));
    }

    public static LabTabResponse deleteVideo(String programId) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.deleteVideo(programId, LabTabPreferences.getInstance
                (LabTabApplication.getInstance()).getCreateTokenResponse().getToken()));
    }

}