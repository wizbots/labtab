package org.wizbots.labtab.requesters;


import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.interfaces.requesters.GetMentorProfileListener;
import org.wizbots.labtab.model.CreateTokenResponse;
import org.wizbots.labtab.model.Mentor;
import org.wizbots.labtab.retrofit.LabTabResponse;


public class MentorProfileRequester implements Runnable, LabTabConstants {

    private CreateTokenResponse createTokenResponse;

    public MentorProfileRequester() {
    }

    public MentorProfileRequester(CreateTokenResponse createTokenResponse) {
        this.createTokenResponse = createTokenResponse;
    }

    @Override
    public void run() {
        LabTabResponse<Mentor> mentorProfileResponse = LabTabHTTPOperationController.getMentorProfile();
        if (mentorProfileResponse != null) {
            for (GetMentorProfileListener getMentorProfileListener : LabTabApplication.getInstance().getUIListeners(GetMentorProfileListener.class)) {
                if (mentorProfileResponse.getResponseCode() == StatusCode.OK) {
                    Mentor mentor = mentorProfileResponse.getResponse();
                    mentor.setDate(createTokenResponse.getDate());
                    mentor.setToken(createTokenResponse.getToken());
                    mentor.setId(createTokenResponse.getId());
                    mentor.setMember_id(createTokenResponse.getMember_id());
                    getMentorProfileListener.mentorProfileFetchedSuccessfully(mentor, createTokenResponse);
                } else {
                    getMentorProfileListener.unableToFetchMentor(mentorProfileResponse.getResponseCode());
                }
            }
        } else {
            for (GetMentorProfileListener getMentorProfileListener : LabTabApplication.getInstance().getUIListeners(GetMentorProfileListener.class)) {
                getMentorProfileListener.unableToFetchMentor(0);
            }
        }
    }
}
