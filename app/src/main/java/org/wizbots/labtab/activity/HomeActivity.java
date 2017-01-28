package org.wizbots.labtab.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.adapter.LeftDrawerAdapter;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.fragment.AddVideoFragment;
import org.wizbots.labtab.fragment.AdditionalInformationFragment;
import org.wizbots.labtab.fragment.EditVideoFragment;
import org.wizbots.labtab.fragment.ForgotPasswordFragment;
import org.wizbots.labtab.fragment.HomeFragment;
import org.wizbots.labtab.fragment.LabDetailsFragment;
import org.wizbots.labtab.fragment.LabListFragment;
import org.wizbots.labtab.fragment.ListOfSkipsFragment;
import org.wizbots.labtab.fragment.LoginFragment;
import org.wizbots.labtab.fragment.MentorProfileFragment;
import org.wizbots.labtab.fragment.ParentFragment;
import org.wizbots.labtab.fragment.StudentLabDetailsFragment;
import org.wizbots.labtab.fragment.StudentProfileFragment;
import org.wizbots.labtab.fragment.StudentStatsDetailsFragment;
import org.wizbots.labtab.fragment.VideoListFragment;
import org.wizbots.labtab.fragment.ViewVideoFragment;
import org.wizbots.labtab.model.LeftDrawerItem;
import org.wizbots.labtab.service.LabTabUploadService;

public class HomeActivity extends ParentActivity implements View.OnClickListener {

    private ParentFragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LabTabHeaderLayout labTabHeaderLayout;
    private Handler drawerHandler = new Handler();
    private ViewGroup myHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView(savedInstanceState);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(final int position) {
        mDrawerLayout.closeDrawer(mDrawerList);
        drawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (position) {
                    case 1:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ToastTexts.GO_TO_WIZBOTS_COM));
                        if (browserIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(browserIntent);
                        }
                        break;
                    case 2:
                        replaceFragment(Fragments.LAB_LIST, new Bundle());
                        break;
                    case 3:
                        replaceFragment(Fragments.VIDEO_LIST, new Bundle());
                        break;
                    case 4:
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).clear();
                        ActivityCompat.finishAffinity(HomeActivity.this);
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        intent.putExtra(Constants.FINISH, true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                }
            }
        }, 400);
    }

    @Override
    public void onClick(View view) {
    }

    public void initView(Bundle savedInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.tool_bar_lab_tab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LeftDrawerItem[] leftDrawerItem = new LeftDrawerItem[4];
        leftDrawerItem[0] = new LeftDrawerItem(R.drawable.ic_home_button_go_to_web, DrawerItem.ITEM_GO_TO);
        leftDrawerItem[1] = new LeftDrawerItem(R.drawable.ic_home_button_lab_list, DrawerItem.ITEM_LAB_LIST);
        leftDrawerItem[2] = new LeftDrawerItem(R.drawable.ic_home_button_video_list, DrawerItem.ITEM_VIDEO_LIST);
        leftDrawerItem[3] = new LeftDrawerItem(R.drawable.ic_logout, DrawerItem.ITEM_LOGOUT);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater myinflater = getLayoutInflater();
        myHeader = (ViewGroup) myinflater.inflate(R.layout.header_left_drawer, mDrawerList, false);
        mDrawerList.addHeaderView(myHeader, null, false);
        myHeader.findViewById(R.id.header_left_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(mDrawerList);
                drawerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        replaceFragment(Fragments.MENTOR_PROFILE, new Bundle());
                    }
                }, 400);
            }
        });

        LeftDrawerAdapter adapter = new LeftDrawerAdapter(this, R.layout.item_left_drawer, leftDrawerItem);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);
//        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        putFragmentInContainer(savedInstanceState);

        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getMenuImageView().setOnClickListener(this);
        labTabHeaderLayout.getMenuImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        });
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
                lockDrawerLayout();
            } else {
                unlockDrawerLayout();
                fragment = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(fragment.getFragmentName());
                fragmentTransaction.commit();
            }
        }
    }

    public void replaceFragment(int fragmentToBePut, Bundle bundle) {
        fragmentManager = getSupportFragmentManager();
        switch (fragmentToBePut) {
            case Fragments.LOGIN:
                fragment = new LoginFragment();
                lockDrawerLayout();
                break;
            case Fragments.FORGOT_PASSWORD:
                fragment = new ForgotPasswordFragment();
                lockDrawerLayout();
                break;
            case Fragments.HOME:
                fragment = new HomeFragment();
                unlockDrawerLayout();
                break;
            case Fragments.LAB_LIST:
                fragment = new LabListFragment();
                break;
            case Fragments.LAB_DETAILS_LIST:
                fragment = new LabDetailsFragment();
                break;
            case Fragments.MENTOR_PROFILE:
                fragment = new MentorProfileFragment();
                break;
            case Fragments.STUDENT_PROFILE:
                fragment = new StudentProfileFragment();
                break;
            case Fragments.STUDENT_STATS_DETAILS:
                fragment = new StudentStatsDetailsFragment();
                break;
            case Fragments.STUDENT_LAB_DETAILS:
                fragment = new StudentLabDetailsFragment();
                break;
            case Fragments.VIDEO_LIST:
                Intent uploadService = new Intent(this, LabTabUploadService.class);
                uploadService.putExtra(LabTabUploadService.EVENT, Events.VIDEO_LIST);
                startService(uploadService);
                fragment = new VideoListFragment();
                break;
            case Fragments.EDIT_VIDEO:
                fragment = new EditVideoFragment();
                break;
            case Fragments.ADD_VIDEO:
                fragment = new AddVideoFragment();
                break;
            case Fragments.LIST_OF_SKIPS:
                fragment = new ListOfSkipsFragment();
                break;
            case Fragments.ADDITIONAL_INFORMATION:
                fragment = new AdditionalInformationFragment();
                break;
            case Fragments.VIEW_VIDEO:
                fragment = new ViewVideoFragment();
                break;

        }
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
        fragmentTransaction.addToBackStack(fragment.getFragmentName());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        fragmentManager = getSupportFragmentManager();
        int backStackCount = fragmentManager.getBackStackEntryCount();
        if (backStackCount == 1) {
            finish();
        } else if (backStackCount > 1) {
            super.onBackPressed();
        }
    }

    public void lockDrawerLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockDrawerLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void clearBackStack() {
        fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStackImmediate();
        }
    }

    public void setNameOfTheLoggedInUser(String loggedInUserName) {
        ((TextViewCustom) myHeader.findViewById(R.id.profile_name)).setText(loggedInUserName);
    }

    public void clearAllTheFragmentFromStack() {
//        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }

    }

}
