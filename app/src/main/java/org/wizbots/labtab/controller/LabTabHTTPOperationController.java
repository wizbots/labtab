package org.wizbots.labtab.controller;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.model.LoginRequest;
import org.wizbots.labtab.retrofit.ConnectionUtil;
import org.wizbots.labtab.retrofit.LabTabApiInterface;
import org.wizbots.labtab.retrofit.LabTabResponse;

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


}