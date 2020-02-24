package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.interfaces.requesters.OnRosterDetailsListener;
import org.wizbots.labtab.manager.FileManager;
import org.wizbots.labtab.model.RosterModel;
import org.wizbots.labtab.retrofit.LabTabResponse;

public class RosterDetailsRequester implements Runnable, LabTabConstants {

    private final String TAG = RosterDetailsRequester.class.getSimpleName();

    private OnRosterDetailsListener mListener;
    private String mRosterId;
    private String mRosterTitle;

    public RosterDetailsRequester(OnRosterDetailsListener listener, String rosterId, String rosterTitle) {
        this.mListener = listener;
        this.mRosterId = rosterId;
        this.mRosterTitle = rosterTitle;
    }

    @Override
    public void run() {
        Log.d(TAG, "labTabResponse Request");

        LabTabResponse<RosterModel> labTabResponse = LabTabHTTPOperationController.getRosterDetails(mRosterId);

        if (labTabResponse.getResponse() != null && labTabResponse.getResponseCode() == StatusCode.OK) {
            Log.d(TAG, "labTabResponse Success, Response Code : " + labTabResponse.getResponseCode() + " labTabResponse response: " + new Gson().toJson(labTabResponse.getResponse()));

            FileManager.FilePathAndStatus filePathAndStatus = FileManager.getInstance().getFileFromBase64AndSaveInSDCard(labTabResponse.getResponse().getData(), mRosterTitle);

            if (filePathAndStatus != null && filePathAndStatus.filStatus) {
                mListener.onRosterDetailsSuccess(mRosterTitle);
            } else {
                mListener.onRosterDetailsError(labTabResponse.getResponseCode());
            }

        } else if (labTabResponse.getResponseCode() == StatusCode.NO_INTERNET) {
            mListener.noInternetConnection();
        } else {
            Log.d(TAG, "labTabResponse Failed, Response Code : " + labTabResponse.getResponseCode());
            mListener.onRosterDetailsError(labTabResponse.getResponseCode());
        }
    }
}
