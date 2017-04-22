package org.wizbots.labtab.receiver;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.interfaces.OnNetworkConnectedListener;
import org.wizbots.labtab.interfaces.OnSyncDoneListener;
import org.wizbots.labtab.pushnotification.NotiManager;
import org.wizbots.labtab.service.LabTabSyncService;
import org.wizbots.labtab.util.NetworkUtils;

import static android.content.Context.ACCOUNT_SERVICE;

public class NetworkConnectivityChangeReceiver extends BroadcastReceiver implements LabTabConstants {

    public static final String AUTHORITY = "org.wizbots.labtab";
    public static final String ACCOUNT_TYPE = "org.wizbots.labtab";
    public static final String ACCOUNT = "default_account";
    Account mAccount;

    @Override
    public void onReceive(final Context context, Intent intent) {
        mAccount = CreateSyncAccount(context);
        boolean isConnected = NetworkUtils.isConnected(LabTabApplication.getInstance());
        for (OnNetworkConnectedListener listener : LabTabApplication.getInstance().getUIListeners(OnNetworkConnectedListener.class)) {
            listener.onNetworkConnected();
        }
        if (!isConnected) {
            Intent uploadService = new Intent(context, LabTabSyncService.class);
            uploadService.putExtra(LabTabSyncService.EVENT, Events.DEVICE_DISCONNECTED_TO_INTERNET);
            context.startService(uploadService);
            //Device Is Not Connected To Internet
//            Toast.makeText(context, "Not Connected To Internet", Toast.LENGTH_SHORT).show();
        } else {
            //Device Is Connected To Internet
            onRefreshButtonClick();
            Intent uploadService = new Intent(context, LabTabSyncService.class);
            uploadService.putExtra(LabTabSyncService.EVENT, Events.DEVICE_CONNECTED_TO_INTERNET);
            context.startService(uploadService);
//            Toast.makeText(context, "Connected To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }

    public void onRefreshButtonClick() {
        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }
}