package org.wizbots.labtab.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.util.NetworkUtils;

public class NetworkConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        boolean isConnected = NetworkUtils.isConnected(LabTabApplication.getInstance());
        if (!isConnected) {
            Toast.makeText(context, "Not Connected To Internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Connected To Internet", Toast.LENGTH_SHORT).show();
        }
    }
}