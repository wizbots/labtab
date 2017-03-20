package org.wizbots.labtab.pushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.database.VideoTable;
import org.wizbots.labtab.model.video.Video;

import java.util.ArrayList;

/**
 * Created by ashish on 27/2/17.
 */

public class NotiManager {

    private static final String TAG = NotiManager.class.getSimpleName();
    final NotificationManager mNotificationManager =
            (NotificationManager) LabTabApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
    final NotificationCompat.Builder builder = new NotificationCompat.Builder(LabTabApplication.getInstance());
    private static NotiManager _instance;
    private static int totalCount;
    private static int mCount = 0;

    static {
        _instance = new NotiManager();
        LabTabApplication.getInstance().addManager(_instance);
    }

    public static NotiManager getInstance(){
        return _instance;
    }


    private NotiManager() {
    }

    public void showNotification(int count) {
        totalCount = count;
        Intent notifyIntent = new Intent();
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                        LabTabApplication.getInstance(),
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("TabLab")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Uploading video")
                .setAutoCancel(false)
                .setContentIntent(notifyPendingIntent)
                .setOngoing(true)
                .build();
        builder.setContentText(mCount + " out of " + totalCount);
        builder.setProgress(totalCount, mCount, true);
        mNotificationManager.notify(0, builder.build());
    }

    public void updateNotification(){
        mCount += 1;
        builder.setContentText(mCount + " out of " + totalCount);
        builder.setProgress(totalCount, mCount, true);
        mNotificationManager.notify(0, builder.build());
        if(mCount == totalCount){
            mCount = 0;
            builder.setContentText("Upload Complete").setProgress(0,0,false);
            builder.setOngoing(false);
            mNotificationManager.notify(0, builder.build());
        }
    }

    public void cancelNotification(){
        builder.setProgress(totalCount, mCount, true);
        mNotificationManager.notify(0, builder.build());
            mCount = 0;
            builder.setContentText("Uploading failed").setProgress(0,0,false);
            builder.setOngoing(false);
            mNotificationManager.notify(0, builder.build());
    }
}
