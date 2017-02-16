package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.database.ProgramAbsencesTable;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.interfaces.requesters.SyncListener;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.video.Video;

import java.util.ArrayList;

public class GetSyncingStatusRequester implements Runnable, LabTabConstants {

    int fragment;

    public GetSyncingStatusRequester() {
    }

    public GetSyncingStatusRequester(int fragment) {
        this.fragment = fragment;
    }

    @Override
    public void run() {
        boolean syncStatus = true;
        ArrayList<Video> videoArrayList = VideoTable.getInstance().getVideosToBeUploaded();
        ArrayList<Absence> absenceArrayList = ProgramAbsencesTable.getInstance().getStudentToBeMarkedAbsent();
        ArrayList<Student> studentsToBePromotedOrDemoted = ProgramStudentsTable.getInstance().getStudentsToBePromotedOrDemoted();
        ArrayList<Video> editVideoArrayList = VideoTable.getInstance().getEditedVideosToBeUploaded();
        ArrayList<Student> studentWizChipsToBeSynced = ProgramStudentsTable.getInstance().getUnSyncData();

        switch (fragment) {
            case Fragments.LAB_DETAILS_LIST:
                syncStatus = syncStatus && videoArrayList.isEmpty();
                syncStatus = syncStatus && absenceArrayList.isEmpty();
                syncStatus = syncStatus && studentsToBePromotedOrDemoted.isEmpty();
                syncStatus = syncStatus && editVideoArrayList.isEmpty();
                syncStatus = syncStatus && studentWizChipsToBeSynced.isEmpty();
                break;
            case Fragments.LIST_OF_SKIPS:
                syncStatus = syncStatus && absenceArrayList.isEmpty();
                break;
            case Fragments.VIDEO_LIST:
                syncStatus = syncStatus && videoArrayList.isEmpty();
                syncStatus = syncStatus && editVideoArrayList.isEmpty();
                break;
        }

//        syncStatus = syncStatus && videoArrayList.isEmpty();
//        syncStatus = syncStatus && absenceArrayList.isEmpty();
//        syncStatus = syncStatus && studentsToBePromotedOrDemoted.isEmpty();
//        syncStatus = syncStatus && editVideoArrayList.isEmpty();
//        syncStatus = syncStatus && studentWizChipsToBeSynced.isEmpty();


        for (SyncListener syncListener : LabTabApplication.getInstance().getUIListeners(SyncListener.class)) {
            syncListener.syncStatusFetchedSuccessfully(syncStatus);
        }
    }
}
