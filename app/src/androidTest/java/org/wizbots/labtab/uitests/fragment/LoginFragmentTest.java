package org.wizbots.labtab.uitests.fragment;

import android.test.ActivityInstrumentationTestCase2;

import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class LoginFragmentTest extends ActivityInstrumentationTestCase2<HomeActivity> {

    public static final String USER_NAME = "judy@wizbots.com";
    public static final String RIGHT_PASSWORD = "robotics";
    public static final String WRONG_PASSWORD = "robot";

    public LoginFragmentTest() {
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
