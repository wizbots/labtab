package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.model.video.response.EditProjectResponse;

public interface EditProjectListener extends BaseUIListener {
    void projectUpdatedSuccessfully(EditProjectResponse editProjectResponse, Video video);

    void unableToUpdateProject(int responseCode);
}
