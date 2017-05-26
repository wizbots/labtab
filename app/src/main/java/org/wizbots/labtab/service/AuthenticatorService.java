package org.wizbots.labtab.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by ashish on 8/2/17.
 */

public class AuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private static final String ACCOUNT_TYPE = "org.wizbots.labtab";
    public static final String ACCOUNT_NAME = "sync";
    private Authenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}

