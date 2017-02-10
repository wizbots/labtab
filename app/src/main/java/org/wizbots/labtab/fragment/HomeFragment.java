package org.wizbots.labtab.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.requesters.ProjectsMetaDataRequester;
import org.wizbots.labtab.util.BackgroundExecutor;

public class HomeFragment extends ParentFragment implements View.OnClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private HomeActivity homeActivityContext;

    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeActivityContext = (HomeActivity) context;
        if (LabTabApplication.getInstance().getMetaDatas() == null) {
            BackgroundExecutor.getInstance().execute(new ProjectsMetaDataRequester());
        }
        initView();
        initListeners();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return HomeFragment.class.getSimpleName();
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(
                String.format(homeActivityContext.getString(R.string.welcome_dynamic_mentor_name),
                        LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName()));
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
    }

    public void initListeners() {
        rootView.findViewById(R.id.cv_lab_list).setOnClickListener(this);
        rootView.findViewById(R.id.cv_go_to).setOnClickListener(this);
        rootView.findViewById(R.id.cv_video_list).setOnClickListener(this);
        rootView.findViewById(R.id.cv_my_profile).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_lab_list:
                homeActivityContext.replaceFragment(Fragments.LAB_LIST, new Bundle());
                break;
            case R.id.cv_go_to:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ToastTexts.GO_TO_WIZBOTS_COM));
                if (browserIntent.resolveActivity(homeActivityContext.getPackageManager()) != null) {
                    startActivity(browserIntent);
                }
                break;
            case R.id.cv_video_list:
                homeActivityContext.replaceFragment(Fragments.VIDEO_LIST, new Bundle());
                break;
            case R.id.cv_my_profile:
                homeActivityContext.replaceFragment(Fragments.MENTOR_PROFILE, new Bundle());
                break;
        }
    }
}
