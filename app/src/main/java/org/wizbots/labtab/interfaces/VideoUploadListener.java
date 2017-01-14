package org.wizbots.labtab.interfaces;

import org.wizbots.labtab.model.Video;

public interface VideoUploadListener {
    void videoUploadCompleted(Video video,int position);
}
