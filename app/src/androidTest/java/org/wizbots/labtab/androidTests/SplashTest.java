package org.wizbots.labtab.androidTests;

import android.test.ActivityInstrumentationTestCase2;

import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.SplashActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class SplashTest extends ActivityInstrumentationTestCase2<SplashActivity> {

    public SplashTest() {
        super(SplashActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testSplash() {
        onView(withId(R.id.root_container)).perform(click());
    }

}
