package org.wizbots.labtab.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.fragment.ForgotPasswordFragment;
import org.wizbots.labtab.fragment.HomeFragment;
import org.wizbots.labtab.fragment.LabDetailsFragment;
import org.wizbots.labtab.fragment.LabListFragment;
import org.wizbots.labtab.fragment.LoginFragment;
import org.wizbots.labtab.fragment.MentorProfileFragment;
import org.wizbots.labtab.fragment.ParentFragment;
import org.wizbots.labtab.fragment.StudentLabDetailsFragment;
import org.wizbots.labtab.fragment.StudentProfileFragment;
import org.wizbots.labtab.fragment.StudentStatsDetailsFragment;

public class HomeActivity extends ParentActivity implements View.OnClickListener {

    private ParentFragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolBar();
        putFragmentInContainer(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar_lab_tab);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void putFragmentInContainer(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (!LabTabPreferences.getInstance(LabTabApplication.getInstance()).isUserLoggedIn()) {
                fragment = new LoginFragment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(fragment.getFragmentName());
                fragmentTransaction.commit();
            } else {
                fragment = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(fragment.getFragmentName());
                fragmentTransaction.commit();
            }
        }
    }

    public void replaceFragment(int fragmentToBePut) {
        fragmentManager = getSupportFragmentManager();
        switch (fragmentToBePut) {
            case FRAGMENT_LOGIN:
                fragment = new LoginFragment();
                break;
            case FRAGMENT_FORGOT_PASSWORD:
                fragment = new ForgotPasswordFragment();
                break;
            case FRAGMENT_HOME:
                fragment = new HomeFragment();
                break;
            case FRAGMENT_LAB_LIST:
                fragment = new LabListFragment();
                break;
            case FRAGMENT_LAB_DETAILS_LIST:
                fragment = new LabDetailsFragment();
                break;
            case FRAGMENT_MENTOR_PROFILE:
                fragment = new MentorProfileFragment();
                break;
            case FRAGMENT_STUDENT_PROFILE:
                fragment = new StudentProfileFragment();
                break;
            case FRAGMENT_STUDENT_STATS_DETAILS:
                fragment = new StudentStatsDetailsFragment();
                break;
            case FRAGMENT_STUDENT_LAB_DETAILS:
                fragment = new StudentLabDetailsFragment();
                break;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
        fragmentTransaction.addToBackStack(fragment.getFragmentName());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        fragmentManager = getSupportFragmentManager();
        int backStackCount = fragmentManager.getBackStackEntryCount();
        if (backStackCount == 1) {
            finish();
        } else if (backStackCount > 1) {
            super.onBackPressed();
        }
    }
}
