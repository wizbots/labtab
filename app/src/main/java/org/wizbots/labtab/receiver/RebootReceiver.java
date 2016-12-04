package org.wizbots.labtab.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // restart  the service  after system is rebooted here

        } catch (Exception ignored) {
        }
    }
}
