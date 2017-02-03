package org.wizbots.labtab.requesters;

import android.os.SystemClock;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.service.LabTabUploadService;
import org.wizbots.labtab.util.NetworkUtils;

public class VideoUploaderRequester implements Runnable, LabTabConstants {
    private LabTabUploadService labTabUploadService;
    private Video video;
    private int position;

    public VideoUploaderRequester() {
    }

    public VideoUploaderRequester(LabTabUploadService labTabUploadService, Video video, int position) {
        this.labTabUploadService = labTabUploadService;
        this.video = video;
        this.position = position;
    }

    @Override
    public void run() {
        for (int i = (video.getStatus() / 10) + 1; i <= 10; i++) {
            try {
                if (NetworkUtils.isConnected(LabTabApplication.getInstance()) && LabTabPreferences.getInstance(LabTabApplication.getInstance()).isUserLoggedIn()) {
                    video.setStatus((i * 10));
                    VideoTable.getInstance().updateVideo(video);
                    SystemClock.sleep(10000);
                } else {
                    break;
                }
            } catch (Exception ignored) {
            }
        }
        labTabUploadService.videoUploadCompleted(video, position);
    }
}
