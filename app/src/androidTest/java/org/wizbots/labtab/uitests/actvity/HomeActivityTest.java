package org.wizbots.labtab.uitests.actvity;

import android.test.ActivityInstrumentationTestCase2;

import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    public HomeActivityTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }


    public void testLabListButton() {
        onView(withId(R.id.cv_lab_list)).perform(click());
        pressBack();
    }

    public void testVideoListButton() {
        onView(withId(R.id.cv_video_list)).perform(click());
        pressBack();
    }

    public void testMyProfileButton() {
        onView(withId(R.id.cv_my_profile)).perform(click());
        pressBack();
    }

    public void testGoToButton() {
        onView(withId(R.id.iv_menu)).perform(click());
        pressBack();
    }

    public void testMenuButton() {
        onView(withId(R.id.iv_menu)).perform(click());
        pressBack();
        onView(withId(R.id.iv_menu)).perform(click());
        pressBack();
        onView(withId(R.id.iv_menu)).perform(click());
        pressBack();
        onView(withId(R.id.iv_menu)).perform(click());
        pressBack();
        onView(withId(R.id.iv_menu)).perform(click());
    }
}
