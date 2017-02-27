package org.wizbots.labtab.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.SplashActivity;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.database.ProgramAbsencesTable;
import org.wizbots.labtab.database.ProgramStudentsTable;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.interfaces.requesters.MarkAbsentListener;
import org.wizbots.labtab.interfaces.requesters.PromoteDemoteListener;
import org.wizbots.labtab.interfaces.requesters.UpdateProjectListener;
import org.wizbots.labtab.interfaces.requesters.VideoUploadListener;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.model.video.Video;
import org.wizbots.labtab.pushnotification.NotiManager;
import org.wizbots.labtab.requesters.AddWizchipsRequester;
import org.wizbots.labtab.requesters.CreateProjectRequester;
import org.wizbots.labtab.requesters.DeleteVideoRequester;
import org.wizbots.labtab.requesters.MarkStudentAbsentSyncRequester;
import org.wizbots.labtab.requesters.PromotionDemotionSyncRequester;
import org.wizbots.labtab.requesters.UpdateProjectRequester;
import org.wizbots.labtab.requesters.WithdrawWizchipsRequester;
import org.wizbots.labtab.util.BackgroundExecutor;

import java.util.ArrayList;

public class LabTabSyncService extends Service implements LabTabConstants, VideoUploadListener, MarkAbsentListener, PromoteDemoteListener, UpdateProjectListener {

    private static final String TAG = LabTabSyncService.class.getSimpleName();
    public static final String EVENT = "EVENT";
    public static LabTabSyncService labTabSyncService = new LabTabSyncService();
    public static boolean statusOfSingleEditVideoBackgroundExecutor[];
    public static boolean statusOfSingleVideoUploadBackgroundExecutor[];
    public static boolean statusOfSingleMarkAbsentUploadBackgroundExecutor[];
    public static boolean statusOfSinglePromotionDemotionBackgroundExecutor[];
    private boolean isUploadServiceRunning = false;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service Created");
        setUploadServiceRunning(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service OnStartCommand");
        if (LabTabPreferences.getInstance(LabTabApplication.getInstance()).isUserLoggedIn()) {
            String event = intent != null ? intent.getStringExtra(EVENT) : Events.NO_EVENT;
            switch (event) {
                case Events.VIDEO_LIST:
                    Log.d("Service Starter Event", event);
                    checkAndStartWhatToSync();
                    break;
                case Events.ADD_VIDEO:
                    Log.d("Service Starter Event", event);
                    checkAndStartWhatToSync();
                    break;
                case Events.DEVICE_REBOOTED:
                    Log.d("Service Starter Event", event);
                    checkAndStartWhatToSync();
                    break;
                case Events.USER_LOGGED_IN:
                    Log.d("Service Starter Event", event);
                    checkAndStartWhatToSync();
                    break;
                case Events.DEVICE_CONNECTED_TO_INTERNET:
                    Log.d("Service Starter Event", event);
                    checkAndStartWhatToSync();
                    break;
                case Events.NO_EVENT:
                    Log.d("Service Starter Event", event);
                    checkAndStartWhatToSync();
                    break;
                case Events.LAB_DETAIL_LIST:
                    Log.d("Service Starter Event", event);
                    checkAndStartWhatToSync();
                    break;
                default:
                    Log.d("Service Starter Event", Events.DEFAULT);
                    checkAndStartWhatToSync();
                    break;
            }
        }
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service Destroyed");
        Log.i(TAG, "Received Stop Foreground Intent");
        setUploadServiceRunning(false);
        stopForeground(true);
        sendBroadcast(new Intent("LabTabServiceReactivation"));
    }

    public boolean isUploadServiceRunning() {
        return isUploadServiceRunning;
    }

    public void setUploadServiceRunning(boolean uploadServiceRunning) {
        isUploadServiceRunning = uploadServiceRunning;
    }

    @Override
    public void videoUploadCompleted(Video video, int position) {
        statusOfSingleVideoUploadBackgroundExecutor[position] = true;
        boolean videoUploadTaskCompleted = true;
        for (boolean all : statusOfSingleVideoUploadBackgroundExecutor) {
            videoUploadTaskCompleted &= all;
        }
        if (videoUploadTaskCompleted) {
            statusOfSingleVideoUploadBackgroundExecutor = null;
            if (isServiceToBeStopped()) {
                stopForeground(true);
                stopSelf();
            }
        }
    }

    /**
     * Called when user clicks on the on-going system Notification.
     */
    public static class NotificationLayoutHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

    private void startForegroundIntent() {
        Intent notificationIntent = new Intent(this, SplashActivity.class);
        notificationIntent.setAction(Action.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        RemoteViews notificationView = new RemoteViews(this.getPackageName(), R.layout.notification);

        Intent notificationLayoutIntent = new Intent(this, LabTabSyncService.NotificationLayoutHandler.class);
        notificationLayoutIntent.putExtra("CLICK", "NOTIFICATION_LAYOUT");

        PendingIntent buttonPlayPendingIntent = PendingIntent.getBroadcast(this, 0, notificationLayoutIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification, buttonPlayPendingIntent);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContent(notificationView)
                .setOngoing(true).build();

        startForeground(FOREGROUND_SERVICE, notification);
    }

    private boolean isServiceToBeStopped() {
        boolean editVideoCompleted;
        boolean videoUploadCompleted;
        boolean markAbsentCompleted;
        boolean promoteDemoteCompleted;

        editVideoCompleted = statusOfSingleEditVideoBackgroundExecutor == null;
        videoUploadCompleted = statusOfSingleVideoUploadBackgroundExecutor == null;
        markAbsentCompleted = statusOfSingleMarkAbsentUploadBackgroundExecutor == null;
        promoteDemoteCompleted = statusOfSinglePromotionDemotionBackgroundExecutor == null;

        return videoUploadCompleted && markAbsentCompleted && promoteDemoteCompleted && editVideoCompleted;
    }

    private void checkAndStartWhatToSync() {
        syncVideoUpload();
        syncMarkAbsent();
        syncPromoteDemote();
        syncEditVideo();
        syncWizchips();
        syncVideoToBeDeleted();
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

    @Override
    public void onMarkAbsentCompleted(int position) {
        statusOfSingleMarkAbsentUploadBackgroundExecutor[position] = true;
        boolean markAbsentTaskCompleted = true;
        for (boolean all : statusOfSingleMarkAbsentUploadBackgroundExecutor) {
            markAbsentTaskCompleted &= all;
        }
        if (markAbsentTaskCompleted) {
            statusOfSingleMarkAbsentUploadBackgroundExecutor = null;
            if (isServiceToBeStopped()) {
                stopForeground(true);
                stopSelf();
            }
        }
    }

    @Override
    public void promoteDemoteCompleted(int position) {
        statusOfSinglePromotionDemotionBackgroundExecutor[position] = true;
        boolean promoteDemoteTaskCompleted = true;
        for (boolean all : statusOfSinglePromotionDemotionBackgroundExecutor) {
            promoteDemoteTaskCompleted &= all;
        }
        if (promoteDemoteTaskCompleted) {
            statusOfSinglePromotionDemotionBackgroundExecutor = null;
            if (isServiceToBeStopped()) {
                stopForeground(true);
                stopSelf();
            }
        }
    }

    @Override
    public void projectUpdateCompleted(int position) {
        statusOfSingleEditVideoBackgroundExecutor[position] = true;
        boolean editVideoTaskCompleted = true;
        for (boolean all : statusOfSingleEditVideoBackgroundExecutor) {
            editVideoTaskCompleted &= all;
        }
        if (editVideoTaskCompleted) {
            statusOfSingleEditVideoBackgroundExecutor = null;
            if (isServiceToBeStopped()) {
                stopForeground(true);
                stopSelf();
            }
        }
    }


    private void syncVideoUpload() {
        synchronized (this) {
            if (statusOfSingleVideoUploadBackgroundExecutor == null) {
                ArrayList<Video> videoArrayList = VideoTable.getInstance().getVideosToBeUploaded();
                if (!videoArrayList.isEmpty()) {
                    startForegroundIntent();
                    statusOfSingleVideoUploadBackgroundExecutor = new boolean[videoArrayList.size()];
                    if(LabTabApplication.getInstance().isNetworkAvailable()){
                        NotiManager.getInstance().showNotification(videoArrayList.size());
                    }
                    for (int i = 0; i < videoArrayList.size(); i++) {
                        BackgroundExecutor.getInstance().execute(new CreateProjectRequester(labTabSyncService, videoArrayList.get(i), i));
                    }
                }
            }
        }
    }

    private void syncMarkAbsent() {
        synchronized (this) {
            if (statusOfSingleMarkAbsentUploadBackgroundExecutor == null) {
                ArrayList<Absence> absenceArrayList = ProgramAbsencesTable.getInstance().getStudentToBeMarkedAbsent();
                if (!absenceArrayList.isEmpty()) {
                    startForegroundIntent();
                    statusOfSingleMarkAbsentUploadBackgroundExecutor = new boolean[absenceArrayList.size()];
                    for (int i = 0; i < absenceArrayList.size(); i++) {
                        BackgroundExecutor.getInstance().execute(new MarkStudentAbsentSyncRequester(
                                labTabSyncService,
                                absenceArrayList.get(i),
                                absenceArrayList.get(i).getDate(),
                                absenceArrayList.get(i).getSend_absent_notification().equals("1"),
                                i));
                    }
                }
            }
        }
    }

    private void syncPromoteDemote() {
        synchronized (this) {
            if (statusOfSinglePromotionDemotionBackgroundExecutor == null) {
                ArrayList<Student> studentsToBePromotedOrDemoted = ProgramStudentsTable.getInstance().getStudentsToBePromotedOrDemoted();
                if (!studentsToBePromotedOrDemoted.isEmpty()) {
                    startForegroundIntent();
                    statusOfSinglePromotionDemotionBackgroundExecutor = new boolean[studentsToBePromotedOrDemoted.size()];
                    for (int i = 0; i < studentsToBePromotedOrDemoted.size(); i++) {
                        BackgroundExecutor.getInstance().execute(new PromotionDemotionSyncRequester(
                                labTabSyncService,
                                studentsToBePromotedOrDemoted.get(i),
                                i));
                    }
                }
            }
        }
    }

    private void syncEditVideo() {
        synchronized (this) {
            if (statusOfSingleEditVideoBackgroundExecutor == null) {
                ArrayList<Video> videoArrayList = VideoTable.getInstance().getEditedVideosToBeUploaded();
                if (!videoArrayList.isEmpty()) {
                    startForegroundIntent();
                    statusOfSingleEditVideoBackgroundExecutor = new boolean[videoArrayList.size()];
                    for (int i = 0; i < videoArrayList.size(); i++) {
                        BackgroundExecutor.getInstance().execute(new UpdateProjectRequester(labTabSyncService, videoArrayList.get(i), i));
                    }
                }
            }
        }
    }

}
