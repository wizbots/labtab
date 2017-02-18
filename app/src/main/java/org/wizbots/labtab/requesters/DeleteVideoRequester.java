package org.wizbots.labtab.requesters;

import android.graphics.Path;
import android.provider.MediaStore;
import android.util.Log;
import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.interfaces.requesters.OnDeleteVideoListener;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.retrofit.LabTabResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        int statusCode = 0;
        LabTabResponse<String> programsOrLabs = LabTabHTTPOperationController.deleteVideo(mVideo.getProgramId());
        if (programsOrLabs != null) {
            Log.d(TAG,String.valueOf(programsOrLabs.getResponseCode()));
            statusCode = programsOrLabs.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK){
                VideoTable.getInstance().deleteVideoById(mVideo.getId());
            }else {
                statusCode = HttpURLConnection.HTTP_OK;
                VideoTable.getInstance().updateDeletedVideo(mVideo.getId(), true);
            }
        } else {
            statusCode = HttpURLConnection.HTTP_OK;
            VideoTable.getInstance().updateDeletedVideo(mVideo.getId(), true);
        }
        deleteVideoFileFromStorage();
        for (OnDeleteVideoListener listener : LabTabApplication.getInstance().getUIListeners(OnDeleteVideoListener.class)) {
            if (statusCode == LabTabConstants.StatusCode.OK) {
                listener.onDeleteVideoSuccess();
            } else {
                listener.onDeleteVideoError();
            }
        }
    }

    private void deleteVideoFileFromStorage(){
        File file = new File(mVideo.getPath());
        if (file.exists()){
            file.delete();
        }
    }
}
