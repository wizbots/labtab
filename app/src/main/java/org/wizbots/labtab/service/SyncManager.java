package org.wizbots.labtab.service;

import android.content.Context;
import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.database.ProgramAbsencesTable;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.interfaces.OnLoadListener;
import org.wizbots.labtab.interfaces.OnSyncDoneListener;
import org.wizbots.labtab.model.Mentor;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.video.Video;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashish on 22/2/17.
 */

public class SyncManager implements OnLoadListener {

    public static final String TAG = SyncManager.class.getName();
    private static SyncManager _instance = null;
    private Map<String, ArrayList> mMap = new HashMap<>();

    public static final String SYNC_WISCHIP = "syncwischip";
    public static final String SYNC_DELETE_VIDEO = "syncdeletevideo";
    public static final String SYNC_EDIT_VIDEO = "synceditvideo";
    public static final String SYNC_PROMOTE_DEMOTE = "syncpromotedemote";
    public static final String SYNC_MARK_ABSENT = "syncmarkabsent";
    public static final String SYNC_ADD_VIDEO = "syncaddvideo";

    static {
        _instance = new SyncManager(LabTabApplication.getInstance());
        LabTabApplication.getInstance().addManager(_instance);
    }

    private SyncManager(Context context) {
        super();
    }

    public static SyncManager getInstance() {
        return _instance;
    }

    @Override
    public void onLoad() {
        onRefreshData(3);
    }

    public void onRefreshData(int value){
        Mentor mentor = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor();
        if(mentor == null){
            return;
        }

        switch (value){
            case 1:
                ArrayList<Student> list = ProgramStudentsTable.getInstance().getUnSyncData();
                Log.d(TAG, "List Size = " + String.valueOf(list.size()));
                if (list != null && !list.isEmpty()) {
                    mMap.put(SYNC_WISCHIP, list);
                }else {
                    mMap.remove(SYNC_WISCHIP);
                }

                ArrayList<Absence> absenceArrayList = ProgramAbsencesTable.getInstance().getStudentToBeMarkedAbsent();
                if (absenceArrayList != null && !absenceArrayList.isEmpty()) {
                    mMap.put(SYNC_MARK_ABSENT, absenceArrayList);
                }else {
                    mMap.remove(SYNC_MARK_ABSENT);
                }

                ArrayList<Student> studentsToBePromotedOrDemoted = ProgramStudentsTable.getInstance().getStudentsToBePromotedOrDemoted();
                if (studentsToBePromotedOrDemoted != null && !studentsToBePromotedOrDemoted.isEmpty()) {
                    mMap.put(SYNC_PROMOTE_DEMOTE, studentsToBePromotedOrDemoted);
                }else {
                    mMap.remove(SYNC_PROMOTE_DEMOTE);
                }
                break;
            case 2:
                ArrayList<Video> videoList = VideoTable.getInstance().getAllUnsyncedVideoStatus();
                if (videoList != null && !videoList.isEmpty()) {
                    mMap.put(SYNC_ADD_VIDEO, videoList);
                }else {
                    mMap.remove(SYNC_ADD_VIDEO);
                }
                break;
            default:
                list = ProgramStudentsTable.getInstance().getUnSyncData();
                if (list != null && !list.isEmpty()) {
                    mMap.put(SYNC_WISCHIP, list);
                }else {
                    mMap.remove(SYNC_WISCHIP);
                }

                absenceArrayList = ProgramAbsencesTable.getInstance().getStudentToBeMarkedAbsent();
                if (absenceArrayList != null && !absenceArrayList.isEmpty()) {
                    mMap.put(SYNC_MARK_ABSENT, absenceArrayList);
                }else {
                    mMap.remove(SYNC_MARK_ABSENT);
                }

                studentsToBePromotedOrDemoted = ProgramStudentsTable.getInstance().getStudentsToBePromotedOrDemoted();
                if (studentsToBePromotedOrDemoted != null && !studentsToBePromotedOrDemoted.isEmpty()) {
                    mMap.put(SYNC_PROMOTE_DEMOTE, studentsToBePromotedOrDemoted);
                }else {
                    mMap.remove(SYNC_PROMOTE_DEMOTE);
                }
                break;
        }




/*        ArrayList<Video> videoArrayList = VideoTable.getInstance().getVideosToBeDeleted();
        if (videoArrayList != null && !videoArrayList.isEmpty()) {
            mMap.put(SYNC_DELETE_VIDEO, videoArrayList);
        }else {
            mMap.remove(SYNC_DELETE_VIDEO);
        }*/


/*        ArrayList<Video> editVideoArrayList = VideoTable.getInstance().getEditedVideosToBeUploaded();
        if (editVideoArrayList != null && !editVideoArrayList.isEmpty()) {
            mMap.put(SYNC_EDIT_VIDEO, editVideoArrayList);
        }else {
            mMap.remove(SYNC_EDIT_VIDEO);
        }*/
        Log.d(TAG, "Before Listener for loop");
        LabTabApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (OnSyncDoneListener listener : LabTabApplication.getInstance().getUIListeners(OnSyncDoneListener.class)) {
                    Log.d(TAG, "In Listener for loop");
                    listener.onSyncDone();
                }
            }
        });
    }

    public boolean isWischipSync(){
        ArrayList<Student> list = mMap.get(SYNC_WISCHIP);
        if(list != null && !list.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public boolean isDeleteVideoSync(){
        ArrayList<Video> list = mMap.get(SYNC_DELETE_VIDEO);
        if(list != null && !list.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public boolean isMarkAbsentSync(){
        ArrayList<Video> list = mMap.get(SYNC_MARK_ABSENT);
        if(list != null && !list.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public boolean isPromoteDemoteSync(){
        ArrayList<Video> list = mMap.get(SYNC_PROMOTE_DEMOTE);
        if(list != null && !list.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public boolean isEditSync(){
        ArrayList<Video> list = mMap.get(SYNC_EDIT_VIDEO);
        if(list != null && !list.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public boolean isAddVideoSync(){
        ArrayList<Video> list = mMap.get(SYNC_ADD_VIDEO);
        if(list != null && !list.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    public boolean isLabDetailSynced(){
        return isWischipSync() && isMarkAbsentSync() && isPromoteDemoteSync();
    }

    public boolean isVideoListSynced(){
        return isAddVideoSync();
    }
}
