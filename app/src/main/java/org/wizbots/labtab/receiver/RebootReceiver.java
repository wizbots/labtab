package org.wizbots.labtab.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.service.LabTabUploadService;

public class RebootReceiver extends BroadcastReceiver implements LabTabConstants {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            //Start Upload Service When Device Rebooted
            Intent uploadService = new Intent(context, LabTabUploadService.class);
            uploadService.putExtra(LabTabUploadService.EVENT, Events.DEVICE_REBOOTED);
            context.startService(uploadService);
        } catch (Exception e) {
            //Something Went Wrong While Starting Service When Device Reboots
            Log.d("RebootReceiver", e.toString());
        }
    }
}
