package org.wizbots.labtab.requesters;

import android.net.Uri;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.interfaces.requesters.AddWizchipsListener;
import org.wizbots.labtab.interfaces.requesters.OnVideoUploadListener;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.model.video.response.CreateProjectResponse;
import org.wizbots.labtab.pushnotification.NotiManager;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.service.LabTabSyncService;
import org.wizbots.labtab.service.SyncManager;
import org.wizbots.labtab.util.LabTabUtil;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreateProjectRequester implements Runnable, LabTabConstants {
    private LabTabSyncService labTabSyncService;
    private Video videoInDB;
    private int position;

    public CreateProjectRequester() {
    }

    public CreateProjectRequester(LabTabSyncService labTabSyncService, Video videoInDB, int position) {
        this.labTabSyncService = labTabSyncService;
        this.videoInDB = videoInDB;
        this.position = position;
    }

    @Override
    public void run() {
        int statusCode = 0;
        Uri fileUri = Uri.parse(videoInDB.getPath());

        File file = new File(videoInDB.getPath());

        RequestBody requestFile = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file
        );

        MultipartBody.Part body = MultipartBody.Part.createFormData("video", file.getName(), requestFile);

        LabTabResponse<CreateProjectResponse> createProjectResponse = LabTabHTTPOperationController.createProject(videoInDB.getCategory(),
                videoInDB.getLab_sku(), videoInDB.getDescription(), videoInDB.getTitle(),
                videoInDB.getNotes_to_the_family(), body,
                getKnowledgeNuggets(LabTabUtil.convertStringToKnowledgeNuggets(videoInDB.getKnowledge_nuggets())),
                getProjectCreators(LabTabUtil.convertStringToProjectCreators(videoInDB.getProject_creators())));

        if (createProjectResponse != null) {
            statusCode = createProjectResponse.getResponseCode();
            if (createProjectResponse.getResponseCode() == StatusCode.CREATED) {
                projectCreatedSuccessfully(createProjectResponse.getResponse());
                NotiManager.getInstance().updateNotification();
            } else {
                unableToCreateProject();
            }
        } else {
            SyncManager.getInstance().onRefreshData(2);
        }
        labTabSyncService.videoUploadCompleted(videoInDB, position);
        for (OnVideoUploadListener listener : LabTabApplication.getInstance().getUIListeners(OnVideoUploadListener.class)) {
            if (statusCode == LabTabConstants.StatusCode.CREATED) {
                listener.onVideoUploadSussess();
            } else {
                listener.onVideoUploadError(statusCode);
            }
        }
    }

    private String getProjectCreators(ArrayList<Student> studentArrayList) {
       /* String[] students = new String[studentArrayList.size()];
        for (int i = 0; i < studentArrayList.size(); i++) {
            students[i] = studentArrayList.get(i).getStudent_id();
        }
        return students;*/
        String creators = "";
        for (Student student : studentArrayList) {
            creators = creators + student.getStudent_id() + ".";
        }
        int len = creators.length();
        creators = creators.substring(0, len - 1);
        return creators;
    }

    private String[] getKnowledgeNuggets(ArrayList<String> knowledgeNuggets) {
        String[] nuggets = new String[knowledgeNuggets.size()];
        for (int i = 0; i < knowledgeNuggets.size(); i++) {
            nuggets[i] = knowledgeNuggets.get(i);
        }
        return nuggets;
    }


    private void projectCreatedSuccessfully(CreateProjectResponse projectCreated) {
        Video videoUploaded = new Video();
        videoUploaded.setId(videoInDB.getId());
        videoUploaded.setMentor_id(videoInDB.getMentor_id());
        videoUploaded.setStatus(100);
        videoUploaded.setPath(videoInDB.getPath());
        videoUploaded.setTitle(projectCreated.getTitle());
        videoUploaded.setCategory(projectCreated.getCategory());
        videoUploaded.setMentor_name(videoInDB.getMentor_name());
        videoUploaded.setLab_sku(projectCreated.getSku());
        videoUploaded.setLab_level(videoInDB.getLab_level());
        videoUploaded.setKnowledge_nuggets(LabTabUtil.toJson(projectCreated.getComponents()));
        videoUploaded.setDescription(projectCreated.getDescription());
        videoUploaded.setProject_creators(videoInDB.getProject_creators());
        videoUploaded.setNotes_to_the_family(projectCreated.getNotes());
        videoUploaded.setEdit_sync_status(videoInDB.getEdit_sync_status());
        videoUploaded.setIs_transCoding(String.valueOf(projectCreated.is_transcoding()));
        videoUploaded.setVideo(LabTabUtil.toJson(projectCreated.getVideo()));
        videoUploaded.setVideoId(projectCreated.getId());
        videoUploaded.setProgramId(videoInDB.getProgramId());
        VideoTable.getInstance().updateVideo(videoUploaded);
    }

    private void unableToCreateProject() {
        Video storeVideoInDB = new Video();
        storeVideoInDB.setId(videoInDB.getId());
        storeVideoInDB.setMentor_id(videoInDB.getMentor_id());
        storeVideoInDB.setStatus(0);
        storeVideoInDB.setPath(videoInDB.getPath());
        storeVideoInDB.setTitle(videoInDB.getTitle());
        storeVideoInDB.setCategory(videoInDB.getTitle());
        storeVideoInDB.setMentor_name(videoInDB.getMentor_name());
        storeVideoInDB.setLab_sku(videoInDB.getLab_sku());
        storeVideoInDB.setLab_level(videoInDB.getLab_level());
        storeVideoInDB.setKnowledge_nuggets(videoInDB.getKnowledge_nuggets());
        storeVideoInDB.setDescription(videoInDB.getDescription());
        storeVideoInDB.setProject_creators(videoInDB.getProject_creators());
        storeVideoInDB.setNotes_to_the_family(videoInDB.getNotes_to_the_family());
        storeVideoInDB.setEdit_sync_status(videoInDB.getEdit_sync_status());
        storeVideoInDB.setIs_transCoding(String.valueOf(false));
        storeVideoInDB.setVideo("");
        storeVideoInDB.setVideoId("");
        storeVideoInDB.setProgramId(videoInDB.getProgramId());
    }
}
