package org.wizbots.labtab.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.service.LabTabSyncService;
import org.wizbots.labtab.util.NetworkUtils;

public class NetworkConnectivityChangeReceiver extends BroadcastReceiver implements LabTabConstants {

    @Override
    public void onReceive(final Context context, Intent intent) {
        boolean isConnected = NetworkUtils.isConnected(LabTabApplication.getInstance());
        if (!isConnected) {
            //Device Is Not Connected To Internet
            Toast.makeText(context, "Not Connected To Internet", Toast.LENGTH_SHORT).show();
        } else {
            //Device Is Connected To Internet
            Intent uploadService = new Intent(context, LabTabSyncService.class);
            uploadService.putExtra(LabTabSyncService.EVENT, Events.DEVICE_CONNECTED_TO_INTERNET);
            context.startService(uploadService);
            Toast.makeText(context, "Connected To Internet", Toast.LENGTH_SHORT).show();
        }
    }
}