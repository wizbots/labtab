package org.wizbots.labtab.interfaces;

import org.wizbots.labtab.model.VideoList;

public interface VideoListAdapterClickListener {
    void onVideoListItemActionView(VideoList videoList);

    void onVideoListItemActionEdit(VideoList videoList);

}
