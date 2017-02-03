package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.model.video.Video;

public interface VideoUploadListener {
    void videoUploadCompleted(Video video,int position);
}
