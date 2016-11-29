package org.wizbots.labtab.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.wizbots.labtab.R;
import org.wizbots.labtab.fragment.ForgotPasswordFragment;
import org.wizbots.labtab.fragment.HomeFragment;
import org.wizbots.labtab.fragment.LabListFragment;
import org.wizbots.labtab.fragment.LoginFragment;

public class BaseActivity extends ParentActivity implements View.OnClickListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    public static final int FRAGMENT_LOGIN = 0;
    public static final int FRAGMENT_FORGOT_PASSWORD = 1;
    public static final int FRAGMENT_HOME = 2;
    public static final int FRAGMENT_LAB_LIST = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initToolBar();
        putFragmentInContainer();
    }

    @Override
    public void onClick(View view) {
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar_lab_tab);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void putFragmentInContainer() {
        fragment = new LoginFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void replaceFragment(int fragmentToBePut) {
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
        }
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
