package org.wizbots.labtab.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.craterzone.logginglib.manager.LoggerManager;

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
import org.wizbots.labtab.fragment.PdfBinder;
import org.wizbots.labtab.fragment.SettingsFragment;
import org.wizbots.labtab.fragment.StudentLabDetailsFragment;
import org.wizbots.labtab.fragment.StudentProfileFragment;
import org.wizbots.labtab.fragment.StudentStatsDetailsFragment;
import org.wizbots.labtab.fragment.VideoListFragment;
import org.wizbots.labtab.fragment.ViewVideoFragment;
import org.wizbots.labtab.interfaces.requesters.ShouldDialogueShow;
import org.wizbots.labtab.manager.FileManager;
import org.wizbots.labtab.model.LeftDrawerItem;
import org.wizbots.labtab.pushnotification.NotiManager;
import org.wizbots.labtab.service.LabTabSyncService;
import org.wizbots.labtab.util.DialogueUtil;

import java.io.File;
import java.util.List;

public class HomeActivity extends ParentActivity implements View.OnClickListener {

    private ParentFragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LabTabHeaderLayout labTabHeaderLayout;
    private Handler drawerHandler = new Handler();
    private ViewGroup myHeader;
    private int previousPosition = -1;

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
        if (previousPosition == position) {
            return;
        }
        previousPosition = position;
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
                        replaceFragment(Fragments.BINDER, new Bundle());
                        break;
                    case 3:
                        replaceFragment(Fragments.LAB_LIST, new Bundle());
                        break;
                    case 4:
                        replaceFragment(Fragments.VIDEO_LIST, new Bundle());
                        break;
                    case 5:
                        replaceFragment(Fragments.ADD_VIDEO, new Bundle());
                        break;
                    case 6:
                        replaceFragment(Fragments.SETTINGS, new Bundle());
                        break;
                    case 7:
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).clear();
                        // Clearing mata data on logout
                        LabTabApplication.getInstance().setMetaDatas(null);
                        FileManager.getInstance().clearFileStorage();
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

        LeftDrawerItem[] leftDrawerItem = new LeftDrawerItem[7];
        leftDrawerItem[0] = new LeftDrawerItem(R.drawable.ic_home_button_go_to_web, DrawerItem.ITEM_GO_TO);
        leftDrawerItem[1] = new LeftDrawerItem(R.drawable.ic_home_button_binder, DrawerItem.ITEM_BINDER);
        leftDrawerItem[2] = new LeftDrawerItem(R.drawable.ic_home_button_lab_list, DrawerItem.ITEM_LAB_LIST);
        leftDrawerItem[3] = new LeftDrawerItem(R.drawable.ic_home_button_video_list, DrawerItem.ITEM_VIDEO_LIST);
        leftDrawerItem[4] = new LeftDrawerItem(R.drawable.ic_upload_video, DrawerItem.ITEM_ADD_VIDEO);
        leftDrawerItem[5] = new LeftDrawerItem(R.drawable.ic_lab_step_1, DrawerItem.ITEM_SETTINGS);
        leftDrawerItem[6] = new LeftDrawerItem(R.drawable.ic_logout, DrawerItem.ITEM_LOGOUT);


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

        myHeader.findViewById(R.id.header_left_drawer).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String filePath = LoggerManager.getInstance(HomeActivity.this).getDiagnosticsFilePath();
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("*/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                startActivity(Intent.createChooser(shareIntent, "send logs"));
                return false;
            }
        });


        LeftDrawerAdapter adapter = new LeftDrawerAdapter(this, R.layout.item_left_drawer, leftDrawerItem);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        putFragmentInContainer(savedInstanceState);
        if (savedInstanceState != null) {
            updateCurrentVisibile();
        }

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
                fragmentTransaction.add(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(fragment.getFragmentName());
                fragmentTransaction.commit();
                lockDrawerLayout();
            } else {
                unlockDrawerLayout();
                fragment = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(fragment.getFragmentName());
                fragmentTransaction.commit();
            }
        }
    }

    private void updateCurrentVisibile() {
//        Fragment current = getCurrentFragment();
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (current instanceof ParentFragment) {

            fragment = (ParentFragment) current;
            previousPosition = getPreviousPosition(fragment);
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
            case Fragments.SETTINGS:
                fragment = new SettingsFragment();
                break;
            case Fragments.LAB_LIST:
                fragment = new LabListFragment();
                previousPosition = -1;
                break;
            case Fragments.LAB_DETAILS_LIST:
                fragment = new LabDetailsFragment();
                Intent uploadServiceStudentLabDetails = new Intent(this, LabTabSyncService.class);
                uploadServiceStudentLabDetails.putExtra(LabTabSyncService.EVENT, Events.LAB_DETAIL_LIST);
                startService(uploadServiceStudentLabDetails);
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
                Intent uploadService = new Intent(this, LabTabSyncService.class);
                uploadService.putExtra(LabTabSyncService.EVENT, Events.VIDEO_LIST);
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
            case Fragments.BINDER:
                fragment = new PdfBinder();
                break;
        }
        fragment.setArguments(bundle);
        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (fragment.getFragmentName().equalsIgnoreCase("HomeFragment")) {
                fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(fragment.getFragmentName());
            } else if (fragment.getFragmentName().equalsIgnoreCase("ListOfSkipsFragment")) {
                boolean fragmentPopped = fragmentManager.popBackStackImmediate("ListOfSkipsFragment", 0);
                if (!fragmentPopped) {
                    fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                    fragmentTransaction.addToBackStack(fragment.getFragmentName());
                }

            } else if (fragment.getFragmentName().equalsIgnoreCase("AdditionalInformationFragment")) {
                boolean fragmentPopped = fragmentManager.popBackStackImmediate("AdditionalInformationFragment", 0);
                if (!fragmentPopped) {
                    fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                    fragmentTransaction.addToBackStack(fragment.getFragmentName());
                }
            } else if (fragment.getFragmentName().equalsIgnoreCase("VideoListFragment")) {
                boolean fragmentPopped = fragmentManager.popBackStackImmediate("VideoListFragment", 0);
                if (!fragmentPopped) {
                    fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                    fragmentTransaction.addToBackStack("HomeFragment");
                }
            } else if (fragment.getFragmentName().equalsIgnoreCase("AddVideoFragment")) {
                fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(null);
            } else if (fragment.getFragmentName().equalsIgnoreCase("LabListFragment")) {
                fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(null);
            } else if (fragment.getFragmentName().equalsIgnoreCase("PdfBinder")) {
                fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(null);
            } else if (fragment.getFragmentName().equalsIgnoreCase("SettingsFragment")) {
                fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(null);
            } else {
                fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getFragmentName());
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        fragmentManager = getSupportFragmentManager();
       /* int backStackCount = fragmentManager.getBackStackEntryCount();
        if (backStackCount == 1) {
            finish();
        } else if (backStackCount > 1) {
            fragmentManager.popBackStackImmediate();
        }*/

        //**
        // for dialog if current visible fragment is add video fragment
        //**
        if ((fragment instanceof AddVideoFragment || fragment instanceof EditVideoFragment)
                && fragment.isVisible()
                && fragment instanceof ShouldDialogueShow &&
                ((ShouldDialogueShow) fragment).isDataChange()) {

            DialogueUtil.showConfirmDialog(HomeActivity.this, R.string.confirm, fragment instanceof AddVideoFragment ?
                    R.string.msg_leave_add_video : R.string.msg_leave_edit_video, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onBackPressBackStackHandler();
                }
            });
        } else {
            onBackPressBackStackHandler();
        }


        try {
            LabDetailsFragment labDetailsfragment = (LabDetailsFragment) getSupportFragmentManager().findFragmentByTag("LabDetailsFragment");
            if (labDetailsfragment != null && labDetailsfragment.isVisible()) {
                labTabHeaderLayout.setDynamicText(Title.LAB_DETAILS);
            }
            ListOfSkipsFragment listofskipfragment = (ListOfSkipsFragment) getSupportFragmentManager().findFragmentByTag("ListOfSkipsFragment");
            if (listofskipfragment != null && listofskipfragment.isVisible()) {
                labTabHeaderLayout.setDynamicText(Title.LIST_OF_SKIPS);
            }
            AdditionalInformationFragment additionalfragment = (AdditionalInformationFragment) getSupportFragmentManager().findFragmentByTag("AdditionalInformationFragment");
            if (additionalfragment != null && additionalfragment.isVisible()) {
                labTabHeaderLayout.setDynamicText(Title.ADDITIONAL_INFORMATION);
            }
            HomeFragment homefragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");
            if (homefragment != null && homefragment.isVisible()) {
                labTabHeaderLayout.setDynamicText(String.format(getString(R.string.welcome_dynamic_mentor_name),
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName()));
            }
        } catch (Exception ignored) {

        }
    }

    private void onBackPressBackStackHandler() {
        int backStackCount = fragmentManager.getBackStackEntryCount();

        if (backStackCount == 1) {
            finish();
        } else if (backStackCount > 1) {
            Log.d("previous position ", "" + previousPosition);
            fragmentManager.popBackStackImmediate();
            updateCurrentVisibile();
        }


    }


    public void lockDrawerLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockDrawerLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void setNameOfTheLoggedInUser(String loggedInUserName) {
        ((TextViewCustom) myHeader.findViewById(R.id.profile_name)).setText(loggedInUserName);
    }

    public void clearAllTheFragmentFromStack() {
        try {
            fragmentManager = getSupportFragmentManager();
            while (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }
        } catch (Exception ignored) {

        }

    }



    private int getPreviousPosition(Fragment fragment) {
        if (fragment instanceof LoginFragment) {
            return Fragments.LOGIN;
        } else if (fragment instanceof ForgotPasswordFragment) {
            return Fragments.FORGOT_PASSWORD;
        } else if (fragment instanceof HomeFragment) {
            return Fragments.HOME;
        } else if (fragment instanceof SettingsFragment) {
            return Fragments.SETTINGS;
        } else if (fragment instanceof LabListFragment) {
            return Fragments.LAB_LIST;
        } else if (fragment instanceof LabDetailsFragment) {
            return Fragments.LAB_DETAILS_LIST;
        } else if (fragment instanceof MentorProfileFragment) {
            return Fragments.MENTOR_PROFILE;
        } else if (fragment instanceof StudentProfileFragment) {
            return Fragments.STUDENT_PROFILE;
        } else if (fragment instanceof StudentStatsDetailsFragment) {
            return Fragments.STUDENT_STATS_DETAILS;
        } else if (fragment instanceof StudentLabDetailsFragment) {
            return Fragments.STUDENT_LAB_DETAILS;
        } else if (fragment instanceof VideoListFragment) {
            return Fragments.VIDEO_LIST;
        } else if (fragment instanceof EditVideoFragment) {
            return Fragments.EDIT_VIDEO;
        } else if (fragment instanceof AddVideoFragment) {
            return Fragments.ADD_VIDEO;
        } else if (fragment instanceof ListOfSkipsFragment) {
            return Fragments.LIST_OF_SKIPS;
        } else if (fragment instanceof AdditionalInformationFragment) {
            return Fragments.ADDITIONAL_INFORMATION;
        } else if (fragment instanceof ViewVideoFragment) {
            return Fragments.VIEW_VIDEO;
        } else if (fragment instanceof PdfBinder) {
            return Fragments.BINDER;
        }


        return -1;
    }

    public void clearAllTheFragmentFromStack(boolean b) {
        try {
            AddVideoFragment addVideofragment = (AddVideoFragment) getSupportFragmentManager().findFragmentByTag("AddVideoFragment");
            if ((addVideofragment != null && addVideofragment.isVisible())) {
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                trans.remove(addVideofragment);
                trans.commit();
                getSupportFragmentManager().popBackStack();
                replaceFragment(Fragments.VIDEO_LIST, new Bundle());
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
            AddVideoFragment labListFragment = (AddVideoFragment) getSupportFragmentManager().findFragmentByTag("AddVideoFragment");
            labListFragment.myOnKeyDown(keyCode);


            //and so on...
        }
        return super.onKeyDown(keyCode, event);
    }
}
