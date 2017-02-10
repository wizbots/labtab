package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.interfaces.requesters.EditProjectListener;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.model.video.response.EditProjectResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class EditProjectRequester implements Runnable, LabTabConstants {
    private Video video;

    public EditProjectRequester() {
    }

    public EditProjectRequester(Video video) {
        this.video = video;
    }

    @Override
    public void run() {
        LabTabResponse<EditProjectResponse> editProjectResponse = LabTabHTTPOperationController.editProject(video.getVideoId(), video.getCategory(),
                video.getLab_sku(), video.getDescription(), video.getTitle(),
                video.getNotes_to_the_family(),
                getKnowledgeNuggets(LabTabUtil.convertStringToKnowledgeNuggets(video.getKnowledge_nuggets())),
                getProjectCreators(LabTabUtil.convertStringToProjectCreators(video.getProject_creators())));

        if (editProjectResponse != null) {
            for (EditProjectListener editProjectListener : LabTabApplication.getInstance().getUIListeners(EditProjectListener.class)) {
                if (editProjectResponse.getResponseCode() == StatusCode.OK) {
                    projectCreatedSuccessfully(editProjectResponse.getResponse());
                    editProjectListener.projectUpdatedSuccessfully(editProjectResponse.getResponse(), video);
                } else {
                    editProjectListener.unableToUpdateProject(editProjectResponse.getResponseCode());
                }
            }
        } else {
            for (EditProjectListener editProjectListener : LabTabApplication.getInstance().getUIListeners(EditProjectListener.class)) {
                editProjectListener.unableToUpdateProject(0);
            }
        }
    }

    private String[] getProjectCreators(ArrayList<Student> studentArrayList) {
        String[] students = new String[studentArrayList.size()];
        for (int i = 0; i < studentArrayList.size(); i++) {
            students[i] = studentArrayList.get(i).getStudent_id();
        }
        return students;
    }

    private String[] getKnowledgeNuggets(ArrayList<String> knowledgeNuggets) {
        String[] nuggets = new String[knowledgeNuggets.size()];
        for (int i = 0; i < knowledgeNuggets.size(); i++) {
            nuggets[i] = knowledgeNuggets.get(i);
        }
        return nuggets;
    }


    private void projectCreatedSuccessfully(EditProjectResponse projectCreated) {
        Video videoUploaded = new Video();
        videoUploaded.setId(video.getId());
        videoUploaded.setMentor_id(video.getMentor_id());
        videoUploaded.setStatus(100);
        videoUploaded.setPath(video.getPath());
        videoUploaded.setTitle(projectCreated.getTitle());
        videoUploaded.setCategory(projectCreated.getCategory());
        videoUploaded.setMentor_name(video.getMentor_name());
        videoUploaded.setLab_sku(projectCreated.getSku());
        videoUploaded.setLab_level(video.getLab_level());
        videoUploaded.setKnowledge_nuggets(LabTabUtil.toJson(projectCreated.getComponents()));
        videoUploaded.setDescription(projectCreated.getDescription());
        videoUploaded.setProject_creators(video.getProject_creators());
        videoUploaded.setNotes_to_the_family(projectCreated.getNotes());
        videoUploaded.setIs_transCoding(String.valueOf(projectCreated.is_transcoding()));
        videoUploaded.setVideo(LabTabUtil.toJson(projectCreated.getVideo()));
        videoUploaded.setVideoId(projectCreated.getId());
        videoUploaded.setProgramId(video.getProgramId());
        VideoTable.getInstance().updateVideo(videoUploaded);
    }
}
