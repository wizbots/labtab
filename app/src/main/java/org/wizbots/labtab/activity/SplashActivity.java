package org.wizbots.labtab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import org.wizbots.labtab.R;

public class SplashActivity extends ParentActivity {

    Handler splashHandler;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashHandler = new Handler();
        splashHandler.postDelayed(splashRunnable, DELAY_MILLIS);
    }

    /**
     * Processes splash screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            splashHandler.removeCallbacks(splashRunnable);
            launchNextActivity();
        }
        return true;
    }

    private Runnable splashRunnable = new Runnable() {
        @Override
        public void run() {
            launchNextActivity();
        }
    };

    private void launchNextActivity() {
        finish();
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
