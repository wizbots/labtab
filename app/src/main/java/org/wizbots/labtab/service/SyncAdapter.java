package org.wizbots.labtab.service;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.requesters.AddWizchipsRequester;
import org.wizbots.labtab.requesters.DeleteVideoRequester;
import org.wizbots.labtab.requesters.WithdrawWizchipsRequester;
import org.wizbots.labtab.util.BackgroundExecutor;

import java.util.ArrayList;

/**
 * Created by ashish on 8/2/17.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String TAG = SyncAdapter.class.getName();

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }
    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d(TAG,"sync is running======>>>>>>>>>>>");
/*        syncWizchips();
        syncVideoToBeDeleted();*/
    }

    private void syncWizchips(){
        if(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor() == null){
            return;
        }
        ArrayList<Student> list = ProgramStudentsTable.getInstance().getUnSyncData();
        if(list != null && !list.isEmpty()){
            for (Student student: list) {
                int count = student.getWizchips() + student.getOfflinewizchips();
                if (student.getWizchips() > count) {
                    BackgroundExecutor.getInstance().execute(new WithdrawWizchipsRequester(student.getProgram_id(),student.getStudent_id(), (-(student.getOfflinewizchips()))));
                }else {
                    BackgroundExecutor.getInstance().execute(new AddWizchipsRequester(student.getProgram_id(),student.getStudent_id(), student.getOfflinewizchips()));
                //!=@todo
                }

            }
        }
    }

    private void syncVideoToBeDeleted() {
        if(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor() == null){
            return;
        }
        ArrayList<Video> videoArrayList = VideoTable.getInstance().getVideosToBeDeleted();
        if (videoArrayList != null && !videoArrayList.isEmpty()) {
            for (int i = 0; i < videoArrayList.size(); i++) {
                BackgroundExecutor.getInstance().execute(new DeleteVideoRequester(videoArrayList.get(i)));
            }
        }
    }
}
