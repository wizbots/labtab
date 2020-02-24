package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.interfaces.requesters.OnDeleteVideoListener;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.SyncManager;

import java.io.File;
import java.net.HttpURLConnection;

/**
 * Created by ashish on 18/2/17.
 */

public class DeleteVideoRequester implements Runnable {

    private static final String TAG = DeleteVideoRequester.class.getSimpleName();

    private Video mVideo;

    public DeleteVideoRequester(Video video) {
        this.mVideo = video;
    }

    @Override
    public void run() {
        Log.d(TAG, "DeleteVideoRequester Request");
        Video vd = VideoTable.getInstance().getVideoById(mVideo.getId());
        int statusCode = 0;
        LabTabResponse<String> programsOrLabs = LabTabHTTPOperationController.deleteVideo(mVideo.getVideoId());
        if (programsOrLabs != null) {
            Log.d(TAG, String.valueOf(programsOrLabs.getResponseCode()));
            statusCode = programsOrLabs.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                VideoTable.getInstance().deleteVideoById(mVideo.getId());
                deleteVideoFileFromStorage();
            } else if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                VideoTable.getInstance().deleteVideoById(mVideo.getId());
                deleteVideoFileFromStorage();
            }
            Log.d(TAG, "DeleteVideoRequester Success, Response Code : " + statusCode + " DeleteVideoRequester response: " + new Gson().toJson(programsOrLabs.getResponse()));
        } else {
            if (vd.getVideoId() == null || vd.getVideoId().isEmpty()) {
                VideoTable.getInstance().deleteVideoById(mVideo.getId());
            } else {
                VideoTable.getInstance().updateDeletedVideo(mVideo.getId(), true);
            }
            deleteVideoFileFromStorage();
            statusCode = HttpURLConnection.HTTP_NO_CONTENT;
            Log.d(TAG, "DeleteVideoRequester Failed, Response Code : " + statusCode);
        }
        SyncManager.getInstance().onRefreshData(2);
        for (OnDeleteVideoListener listener : LabTabApplication.getInstance().getUIListeners(OnDeleteVideoListener.class)) {
            if (statusCode == HttpURLConnection.HTTP_NO_CONTENT || statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                listener.onDeleteVideoSuccess();
            } else if (statusCode == LabTabConstants.StatusCode.NO_INTERNET) {
                listener.noInternetConnection();
            } else {
                listener.onDeleteVideoError();
            }
        }
    }

    private void deleteVideoFileFromStorage() {
        File file = new File(mVideo.getPath());
        if (file.exists()) {
            file.delete();
        }
    }
}
