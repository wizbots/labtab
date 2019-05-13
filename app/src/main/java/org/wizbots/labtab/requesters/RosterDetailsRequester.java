package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.interfaces.requesters.OnRosterDetailsListener;
import org.wizbots.labtab.manager.FileManager;
import org.wizbots.labtab.model.metadata.ProgramMetaData;
import org.wizbots.labtab.retrofit.LabTabResponse;

public class RosterDetailsRequester implements Runnable, LabTabConstants {

    private final String TAG = RosterDetailsRequester.class.getSimpleName();

    private OnRosterDetailsListener mListener;
    private String mRosterId;

    public RosterDetailsRequester(OnRosterDetailsListener listener, String rosterId) {
        this.mListener = listener;
        this.mRosterId = rosterId;
    }

    @Override
    public void run() {
        Log.d(TAG, "labTabResponse Request");

        LabTabResponse<ProgramMetaData> labTabResponse = LabTabHTTPOperationController.getRosterDetails(mRosterId);

        if (labTabResponse.getResponse() != null && labTabResponse.getResponseCode() == StatusCode.OK) {
            Log.d(TAG, "labTabResponse Success, Response Code : " + labTabResponse.getResponseCode() + " labTabResponse response: " +new Gson().toJson(labTabResponse.getResponse()));

            FileManager.FilePathAndStatus filePathAndStatus = FileManager.getInstance().getFileFromBase64AndSaveInSDCard("base64EncodedString", mRosterId);

            if(filePathAndStatus != null && filePathAndStatus.filStatus) {
                mListener.onRosterDetailsSuccess(mRosterId);
            } else {
                mListener.onRosterDetailsError(labTabResponse.getResponseCode());
            }

        }else{
            Log.d(TAG, "labTabResponse Failed, Response Code : " + labTabResponse.getResponseCode());
            mListener.onRosterDetailsError(labTabResponse.getResponseCode());
        }
    }
}
