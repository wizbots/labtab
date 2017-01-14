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
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.interfaces.VideoUploadListener;
import org.wizbots.labtab.model.Video;
import org.wizbots.labtab.requesters.VideoUploaderRequester;
import org.wizbots.labtab.util.BackgroundExecutor;

import java.util.ArrayList;

public class LabTabUploadService extends Service implements LabTabConstants, VideoUploadListener {

    private static final String TAG = LabTabUploadService.class.getSimpleName();
    public static final String EVENT = "EVENT";
    public static LabTabUploadService labTabUploadService = new LabTabUploadService();
    public static boolean statusOfSingleVideoUploadBackgroundExecutor[];
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
                    uploadVideo();
                    break;
                case Events.ADD_VIDEO:
                    Log.d("Service Starter Event", event);
                    uploadVideo();
                    break;
                case Events.DEVICE_REBOOTED:
                    Log.d("Service Starter Event", event);
                    uploadVideo();
                    break;
                case Events.USER_LOGGED_IN:
                    Log.d("Service Starter Event", event);
                    uploadVideo();
                    break;
                case Events.DEVICE_CONNECTED_TO_INTERNET:
                    Log.d("Service Starter Event", event);
                    uploadVideo();
                    break;
                case Events.NO_EVENT:
                    Log.d("Service Starter Event", event);
                    uploadVideo();
                    break;
                default:
                    Log.d("Service Starter Event", Events.DEFAULT);
                    uploadVideo();
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

    private void uploadVideo() {
        synchronized (this) {
            if (statusOfSingleVideoUploadBackgroundExecutor == null) {
                ArrayList<Video> videoArrayList = VideoTable.getInstance().getVideosToBeUploaded();
                if (!videoArrayList.isEmpty()) {
                    startForegroundIntent();
                    statusOfSingleVideoUploadBackgroundExecutor = new boolean[videoArrayList.size()];
                    for (int i = 0; i < videoArrayList.size(); i++) {
                        BackgroundExecutor.getInstance().execute(new VideoUploaderRequester(labTabUploadService, videoArrayList.get(i), i));
                    }
                }
            }
        }
    }

    @Override
    public void videoUploadCompleted(Video video, int position) {
        statusOfSingleVideoUploadBackgroundExecutor[position] = true;
        boolean videoUploadTaskCompleted = true;
        for (boolean all : statusOfSingleVideoUploadBackgroundExecutor) {
            videoUploadTaskCompleted &= all;
        }
        if (videoUploadTaskCompleted) {
            Log.i(TAG, "Received Stop Foreground Intent");
            statusOfSingleVideoUploadBackgroundExecutor = null;
            stopForeground(true);
            stopSelf();
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

        Intent notificationLayoutIntent = new Intent(this, LabTabUploadService.NotificationLayoutHandler.class);
        notificationLayoutIntent.putExtra("CLICK", "NOTIFICATION_LAYOUT");

        PendingIntent buttonPlayPendingIntent = pendingIntent.getBroadcast(this, 0, notificationLayoutIntent, 0);
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
}
