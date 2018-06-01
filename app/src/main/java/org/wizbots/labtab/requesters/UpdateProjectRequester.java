package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.model.video.response.EditProjectResponse;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.LabTabSyncService;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class UpdateProjectRequester implements Runnable, LabTabConstants {
    private final String TAG = UpdateProjectRequester.class.getSimpleName();
    private LabTabSyncService labTabSyncService;
    private Video video;
    private int position;

    public UpdateProjectRequester() {
    }

    public UpdateProjectRequester(LabTabSyncService labTabSyncService, Video video, int position) {
        this.labTabSyncService = labTabSyncService;
        this.video = video;
        this.position = position;
    }

    @Override
    public void run() {
        Log.d(TAG, "editProjectResponse Request");
        LabTabResponse<EditProjectResponse> editProjectResponse = LabTabHTTPOperationController.editProject(video.getVideoId(), video.getCategory(),
                video.getLab_sku(), video.getDescription(), video.getTitle(),
                video.getNotes_to_the_family(),
                getKnowledgeNuggets(LabTabUtil.convertStringToKnowledgeNuggets(video.getKnowledge_nuggets())),
                getProjectCreators(LabTabUtil.convertStringToProjectCreators(video.getProject_creators())), LabTabApplication.getInstance().getUserAgent());

        if (editProjectResponse != null) {
            if (editProjectResponse.getResponseCode() == StatusCode.OK) {
                projectUpdatedSuccessfully(editProjectResponse.getResponse());
                Log.d(TAG, "editProjectResponse Success, Response Code : " + editProjectResponse.getResponseCode() + " editProjectResponse response: " +new Gson().toJson(editProjectResponse.getResponse()));
            } else {
                projectEditedSuccessfully();
                Log.d(TAG, "editProjectResponse Failed, Response Code : " + editProjectResponse.getResponseCode());
            }
        } else {
            projectEditedSuccessfully();
            Log.d(TAG, "editProjectResponse Failed, Response Code : " + editProjectResponse.getResponseCode());
        }
        labTabSyncService.projectUpdateCompleted(position);
    }

    private String[] getProjectCreators(ArrayList<Student> studentArrayList) {
        String[] students = new String[studentArrayList.size()];
        for (int i = 0; i < studentArrayList.size(); i++) {
            students[i] = studentArrayList.get(i).getStudent_id();
        }
        /*String creators = "";
        for (Student student : studentArrayList) {
            creators = creators + student.getStudent_id() + ".";
        }
        int len = creators.length();
        creators = creators.substring(0, len - 1);
        return creators;*/
        return students;
    }

    private String[] getKnowledgeNuggets(ArrayList<String> knowledgeNuggets) {
        String[] nuggets = new String[knowledgeNuggets.size()];
        for (int i = 0; i < knowledgeNuggets.size(); i++) {
            nuggets[i] = knowledgeNuggets.get(i);
        }
        return nuggets;
    }

    private void projectUpdatedSuccessfully(EditProjectResponse projectCreated) {
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
        videoUploaded.setEdit_sync_status(SyncStatus.SYNCED);
        videoUploaded.setIs_transCoding(String.valueOf(projectCreated.is_transcoding()));
        videoUploaded.setVideo(LabTabUtil.toJson(projectCreated.getVideo()));
        videoUploaded.setVideoId(projectCreated.getId());
        videoUploaded.setProgramId(video.getProgramId());
        VideoTable.getInstance().updateVideo(videoUploaded);
    }

    private void projectEditedSuccessfully() {
        Video videoEdited = new Video();
        videoEdited.setId(video.getId());
        videoEdited.setMentor_id(video.getMentor_id());
        videoEdited.setStatus(0);
        videoEdited.setPath(video.getPath());
        videoEdited.setTitle(video.getTitle());
        videoEdited.setCategory(video.getCategory());
        videoEdited.setMentor_name(video.getMentor_name());
        videoEdited.setLab_sku(video.getLab_sku());
        videoEdited.setLab_level(video.getLab_level());
        videoEdited.setKnowledge_nuggets(video.getKnowledge_nuggets());
        videoEdited.setDescription(video.getDescription());
        videoEdited.setProject_creators(video.getProject_creators());
        videoEdited.setNotes_to_the_family(video.getNotes_to_the_family());
        videoEdited.setEdit_sync_status(SyncStatus.NOT_SYNCED);
        videoEdited.setIs_transCoding(video.getIs_transCoding());
        videoEdited.setVideo(video.getVideo());
        videoEdited.setVideoId(video.getVideoId());
        videoEdited.setProgramId(video.getProgramId());
        VideoTable.getInstance().updateVideo(videoEdited);
    }

}
