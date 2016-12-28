package org.wizbots.labtab.tests;

import android.test.ActivityInstrumentationTestCase2;

import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class LoginTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    public static final String USER_NAME = "judy@wizbots.com";
    public static final String RIGHT_PASSWORD = "robotics";
    public static final String WRONG_PASSWORD = "robot";

    public LoginTest() {
        super(HomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testEmailAndPasswordContent() {
        onView(withId(R.id.edt_email)).check(matches(withText("")));
        onView(withId(R.id.edt_password)).check(matches(withText("")));
    }

    public void testTypeEmailAndPassword() {
        onView(withId(R.id.edt_email)).perform(typeText(USER_NAME));
        onView(withId(R.id.edt_password)).perform(typeText(RIGHT_PASSWORD));
    }

    public void testLoginButton() {
        onView(withId(R.id.btn_login)).perform(click());
    }

}
