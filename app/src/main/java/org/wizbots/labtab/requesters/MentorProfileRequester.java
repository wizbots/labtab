package org.wizbots.labtab.requesters;


import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.interfaces.requesters.GetMentorProfileListener;
import org.wizbots.labtab.model.CreateTokenResponse;
import org.wizbots.labtab.model.Mentor;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.SyncManager;


public class MentorProfileRequester implements Runnable, LabTabConstants {

    private final static String TAG = MentorProfileRequester.class.getSimpleName();
    private CreateTokenResponse createTokenResponse;

    public MentorProfileRequester() {
    }

    public MentorProfileRequester(CreateTokenResponse createTokenResponse) {
        this.createTokenResponse = createTokenResponse;
    }

    @Override
    public void run() {
        Log.d(TAG, "mentorProfileResponse Request");
        LabTabResponse<Mentor> mentorProfileResponse = LabTabHTTPOperationController.getMentorProfile();
        if (mentorProfileResponse != null) {
            for (GetMentorProfileListener getMentorProfileListener : LabTabApplication.getInstance().getUIListeners(GetMentorProfileListener.class)) {
                if (mentorProfileResponse.getResponseCode() == StatusCode.OK) {
                    Mentor mentor = mentorProfileResponse.getResponse();
                    mentor.setDate(createTokenResponse.getDate());
                    mentor.setToken(createTokenResponse.getToken());
                    mentor.setId(createTokenResponse.getId());
                    mentor.setMember_id(createTokenResponse.getMember_id());
                    SyncManager.getInstance().onRefreshData(3);
                    getMentorProfileListener.mentorProfileFetchedSuccessfully(mentor, createTokenResponse);
                    Log.d(TAG, "mentorProfileResponse Success, Response Code : " + mentorProfileResponse.getResponseCode() + " mentorProfileResponse  response: " + new Gson().toJson(mentorProfileResponse.getResponse()));
                } else {
                    getMentorProfileListener.unableToFetchMentor(mentorProfileResponse.getResponseCode());
                    Log.d(TAG, "mentorProfileResponse Failed, Response Code : " + mentorProfileResponse.getResponseCode());
                }
            }
        } else {
            for (GetMentorProfileListener getMentorProfileListener : LabTabApplication.getInstance().getUIListeners(GetMentorProfileListener.class)) {
                getMentorProfileListener.unableToFetchMentor(0);
                Log.d(TAG, "mentorProfileResponse Failed, Response Code : " + mentorProfileResponse.getResponseCode());
            }
        }
    }
}
