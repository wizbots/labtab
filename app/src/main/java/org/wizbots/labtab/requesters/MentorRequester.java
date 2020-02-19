package org.wizbots.labtab.requesters;
import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.MentorsTable;
import org.wizbots.labtab.interfaces.requesters.MentorListener;
import org.wizbots.labtab.model.Mentor;
import org.wizbots.labtab.retrofit.LabTabResponse;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MentorRequester implements Runnable {
    @Override
    public void run() {
        int statusCode = 0;
        LabTabResponse<ArrayList<Mentor>> mentorResponse = LabTabHTTPOperationController.getMentor();
        if (mentorResponse != null) {
            statusCode = mentorResponse.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK)
                MentorsTable.getInstance().insert(mentorResponse.getResponse());
        }
        for (MentorListener listener : LabTabApplication.getInstance().getUIListeners(MentorListener.class)) {
            if (statusCode == HttpURLConnection.HTTP_OK)
                listener.onMentorSuccess();
            else
                listener.onMentorFailure();
        }
    }
}
