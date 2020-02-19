package org.wizbots.labtab.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.activity.HomeActivity;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;
import org.wizbots.labtab.customview.TextViewCustom;
import org.wizbots.labtab.model.Mentor;

public class MentorProfileFragment extends ParentFragment implements View.OnClickListener {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;
    private HomeActivity homeActivityContext;
    private Mentor mentorProfile;

    public MentorProfileFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mentor_profile, container, false);
        homeActivityContext = (HomeActivity) context;
        mentorProfile = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor();
        initView();
        initListeners();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return MentorProfileFragment.class.getSimpleName();
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(Title.MENTOR_PROFILE);
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
        ((TextViewCustom) rootView.findViewById(R.id.tv_first_name)).setText(mentorProfile.getFirst_name());
        ((TextViewCustom) rootView.findViewById(R.id.tv_last_name)).setText(mentorProfile.getLast_name());
        ((TextViewCustom) rootView.findViewById(R.id.tv_email)).setText(mentorProfile.getEmail());
        ((TextViewCustom) rootView.findViewById(R.id.tv_username)).setText(mentorProfile.getUsername());
        ((TextViewCustom) rootView.findViewById(R.id.tv_gender)).setText(mentorProfile.getGender());
        ((TextViewCustom) rootView.findViewById(R.id.tv_state)).setText(mentorProfile.getState());
        ((TextViewCustom) rootView.findViewById(R.id.tv_state_address)).setText(mentorProfile.getStreet_address());
        ((TextViewCustom) rootView.findViewById(R.id.tv_city)).setText(mentorProfile.getCity());
        ((TextViewCustom) rootView.findViewById(R.id.tv_zip_code)).setText(mentorProfile.getZipCode());
        ((TextViewCustom) rootView.findViewById(R.id.tv_phone1)).setText(mentorProfile.getPhone1());
        ((TextViewCustom) rootView.findViewById(R.id.tv_phone2)).setText(mentorProfile.getPhone2());
        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
    }

    public void initListeners() {
    }

    @Override
    public void onClick(View view) {
    }
}
