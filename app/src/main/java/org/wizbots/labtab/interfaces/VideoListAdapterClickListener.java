package org.wizbots.labtab.interfaces;

import org.wizbots.labtab.model.Video;

public interface VideoListAdapterClickListener {
    void onVideoListItemActionView(Video video);

    void onVideoListItemActionEdit(Video video);

    void playVideo(Video video);
}
