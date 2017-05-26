package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.model.video.response.CreateProjectResponse;

public interface CreateProjectListener extends BaseUIListener {
    void projectCreatedSuccessfully(CreateProjectResponse createProjectResponse, Video video);

    void unableToCreateProject(int responseCode);
}
