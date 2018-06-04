package org.wizbots.labtab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.R;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.customview.LabTabHeaderLayout;

public class PdfBinder extends ParentFragment {

    private LabTabHeaderLayout labTabHeaderLayout;
    private Toolbar toolbar;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView  = inflater.inflate(R.layout.fragment_binder,container,false);
        initView();
        return rootView;
    }

    @Override
    public String getFragmentName() {
        return PdfBinder.class.getSimpleName();
    }

    public void initView() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar_lab_tab);
        labTabHeaderLayout = (LabTabHeaderLayout) toolbar.findViewById(R.id.lab_tab_header_layout);
        labTabHeaderLayout.getDynamicTextViewCustom().setText(R.string.BINDER);
//        homeActivityContext.setNameOfTheLoggedInUser(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getFullName());
        labTabHeaderLayout.getMenuImageView().setVisibility(View.VISIBLE);
        labTabHeaderLayout.getMenuImageView().setImageResource(R.drawable.ic_menu);
        labTabHeaderLayout.getSyncImageView().setImageResource(R.drawable.ic_synced);
    }
}
