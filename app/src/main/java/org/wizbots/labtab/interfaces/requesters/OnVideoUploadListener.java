package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;

/**
 * Created by ashish on 28/2/17.
 */

public interface OnVideoUploadListener extends BaseUIListener {

    void onVideoUploadSussess();
    void onVideoUploadError(int statusCode);
}
